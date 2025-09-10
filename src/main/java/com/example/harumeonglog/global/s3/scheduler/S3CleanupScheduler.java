package com.example.harumeonglog.global.s3.scheduler;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.example.harumeonglog.domain.member.repository.MemberRepository;
import com.example.harumeonglog.domain.pet.repository.PetImageRepository;
import com.example.harumeonglog.domain.pet.repository.PetRepository;
import com.example.harumeonglog.domain.post.repository.PostImageRepository;
import com.example.harumeonglog.global.data.S3ConfigData;
import com.example.harumeonglog.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
@Component
@Slf4j
@RequiredArgsConstructor
public class S3CleanupScheduler {

    private final PetRepository petRepository;
    private final MemberRepository memberRepository;
    private final PostImageRepository postImageRepository;
    private final PetImageRepository petImageRepository;
    private final AmazonS3 amazonS3Client;
    private final S3ConfigData s3ConfigData;
    private final S3Util s3Util;

    // 매일 새벽 3시에 실행
    @Scheduled(cron = "0 0 3 * * ?")
    public void cleanupS3Images() {
        log.info("Starting S3 cleanup process...");

        // 1. S3의 모든 이미지 키 목록 가져오기
        Set<String> s3ImageKeys = getAllS3ImageKeys();
        log.info("Found {} images in S3", s3ImageKeys.size());

        // 2. DB에 저장된 모든 이미지 키 목록 가져오기
        Set<String> dbImageKeys = getAllDbImageKeys();
        log.info("Found {} image references in DB", dbImageKeys.size());

        // 3. S3에는 있지만 DB에 없는 이미지 키 찾기 (orphaned S3 files)
        Set<String> orphanedS3Keys = new HashSet<>(s3ImageKeys);
        orphanedS3Keys.removeAll(dbImageKeys);

        // 4. DB에는 있지만 S3에 없는 이미지 키 찾기 (orphaned DB records)
        Set<String> orphanedDbKeys = new HashSet<>(dbImageKeys);
        orphanedDbKeys.removeAll(s3ImageKeys);

        // 5. 불필요한 S3 이미지 삭제
        if (!orphanedS3Keys.isEmpty()) {
            log.info("Deleting {} orphaned files from S3", orphanedS3Keys.size());
            deleteOrphanedS3Files(orphanedS3Keys);
        }

        // 6. 불필요한 DB 이미지 레코드 삭제
        if (!orphanedDbKeys.isEmpty()) {
            log.info("Deleting {} orphaned image records from DB", orphanedDbKeys.size());
            deleteOrphanedDbRecords(orphanedDbKeys);
        }

        log.info("S3 cleanup process completed");
    }

    private void deleteOrphanedS3Files(Set<String> orphanedS3Keys) {
        int successCount = 0;
        int failCount = 0;

        for (String imageKey : orphanedS3Keys) {
            try {
                s3Util.deleteFile(imageKey);
                successCount++;
                log.debug("Successfully deleted S3 file: {}", imageKey);
            } catch (Exception e) {
                failCount++;
                log.error("Failed to delete S3 file: {}", imageKey, e);
                // 개별 파일 삭제 실패시에도 계속 진행
            }
        }

        log.info("S3 file deletion completed - Success: {}, Failed: {}", successCount, failCount);
    }

    private void deleteOrphanedDbRecords(Set<String> orphanedDbKeys) {
        int totalDeleted = 0;

        // PetImage 테이블에서 orphaned 레코드 삭제
        int petImageDeleted = petImageRepository.deleteByImageKeyIn(orphanedDbKeys);
        totalDeleted += petImageDeleted;
        log.info("Deleted {} orphaned records from PetImage table", petImageDeleted);

        // Pet 테이블에서 orphaned 이미지 키 null로 업데이트
        int petUpdated = petRepository.updateImageKeyToNullByImageKeyIn(orphanedDbKeys);
        totalDeleted += petUpdated;
        log.info("Updated {} orphaned image keys to null in Pet table", petUpdated);

        // PostImage 테이블에서 orphaned 레코드 삭제
        int postImageDeleted = postImageRepository.deleteByImageKeyIn(orphanedDbKeys);
        totalDeleted += postImageDeleted;
        log.info("Deleted {} orphaned records from PostImage table", postImageDeleted);

        // Member 테이블에서 orphaned 이미지 키 null로 업데이트
        int memberUpdated = memberRepository.updateImageKeyToNullByImageKeyIn(orphanedDbKeys);
        totalDeleted += memberUpdated;
        log.info("Updated {} orphaned image keys to null in Member table", memberUpdated);

        log.info("Total DB records processed: {}", totalDeleted);
    }

    private Set<String> getAllS3ImageKeys() {
        Set<String> keys = new HashSet<>();

        ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(s3ConfigData.getBucket());

        ListObjectsV2Result result;
        do {
            result = amazonS3Client.listObjectsV2(request);

            result.getObjectSummaries().stream()
                    .map(S3ObjectSummary::getKey)
                    .filter(key -> key != null && !key.trim().isEmpty()) // null/empty key 필터링
                    .forEach(keys::add);

            request.setContinuationToken(result.getNextContinuationToken());
        } while (result.isTruncated());

        return keys;
    }

    private Set<String> getAllDbImageKeys() {
        // 모든 도메인의 이미지 키를 통합
        Set<String> allImageKeys = new HashSet<>();

        // PetImage 이미지 키 추가
        allImageKeys.addAll(petImageRepository.findAllImageKeys());

        // Pet 이미지 키 추가
        allImageKeys.addAll(petRepository.findAllImageKeys());

        // Post 이미지 키 추가
        allImageKeys.addAll(postImageRepository.findAllImageKeys());

        // Member 이미지 키 추가 (프로필 이미지 등)
        allImageKeys.addAll(memberRepository.findAllImageKeys());

        // null/empty 값 제거
        allImageKeys.removeIf(key -> key == null || key.trim().isEmpty());

        return allImageKeys;
    }
}