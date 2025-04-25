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
import com.example.harumeonglog.global.error.code.S3ErrorCode;
import com.example.harumeonglog.global.error.exception.S3Exception;
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
        // 1. S3의 모든 이미지 키 목록 가져오기
        Set<String> s3ImageKeys = getAllS3ImageKeys();

        // 2. DB에 저장된 모든 이미지 키 목록 가져오기
        Set<String> dbImageKeys = getAllDbImageKeys();

        // 3. DB에 없지만 S3에는 있는 이미지 키 찾기
        s3ImageKeys.removeAll(dbImageKeys);

        // 4. 불필요한 S3 이미지 삭제
        s3ImageKeys
                .forEach(imageKey -> {
                    try {
                        s3Util.deleteFile(imageKey);
                    } catch (Exception e) {
                        throw new S3Exception(S3ErrorCode.DELETE_FAILED);
                    }
                });
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

        return allImageKeys;
    }
}