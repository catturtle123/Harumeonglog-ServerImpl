package com.example.harumeonglog.global.s3.scheduler;

import com.example.harumeonglog.domain.member.repository.MemberRepository;
import com.example.harumeonglog.domain.pet.repository.PetImageRepository;
import com.example.harumeonglog.domain.pet.repository.PetRepository;
import com.example.harumeonglog.domain.post.repository.PostImageRepository;
import com.example.harumeonglog.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    private final S3Util s3Util;

    // 매일 새벽 3시에 실행
    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void cleanupDbKeysNotInS3() {
        log.info("=== DB → S3 동기화 시작 ===");

        // 1. 모든 DB 이미지 키 가져오기
        Set<String> dbKeys = getAllDbImageKeys();
        log.info("DB에서 {}개의 키 확인", dbKeys.size());

        // 2. 실제 S3에 없는 키만 필터링
        Set<String> missingKeys = new HashSet<>();
        for (String key : dbKeys) {
            if (!s3Util.isObjectExists(key)) {
                missingKeys.add(key);
            }
        }
        log.info("S3에 없는 키 {}개 발견", missingKeys.size());

        // 3. DB 정리
        if (!missingKeys.isEmpty()) {
            cleanupDbReference(missingKeys);
        }

        log.info("=== DB → S3 동기화 완료 ===");
    }

    private Set<String> getAllDbImageKeys() {
        Set<String> allImageKeys = new HashSet<>();
        allImageKeys.addAll(memberRepository.findAllImageKeys());
        allImageKeys.addAll(petRepository.findAllImageKeys());
        allImageKeys.addAll(postImageRepository.findAllImageKeys());
        allImageKeys.addAll(petImageRepository.findAllImageKeys());
        allImageKeys.removeIf(key -> key == null || key.trim().isEmpty());
        return allImageKeys;
    }

    private void cleanupDbReference(Set<String> missingKeys) {
        int total = 0;

        // PostImage → 레코드 삭제
        int postDeleted = postImageRepository.deleteByImageKeyIn(missingKeys);
        total += postDeleted;
        log.info("PostImage {}건 삭제", postDeleted);

        // PetImage → 레코드 삭제
        int petImageDeleted = petImageRepository.deleteByImageKeyIn(missingKeys);
        total += petImageDeleted;
        log.info("PetImage {}건 삭제", petImageDeleted);

        // Pet.mainImage → null 처리
        int petUpdated = petRepository.updateImageKeyToNullByImageKeyIn(missingKeys);
        total += petUpdated;
        log.info("Pet.mainImage {}건 null 처리", petUpdated);

        // Member.profileImage → null 처리
        int memberUpdated = memberRepository.updateImageKeyToNullByImageKeyIn(missingKeys);
        total += memberUpdated;
        log.info("Member.profileImage {}건 null 처리", memberUpdated);

        log.info("DB 총 정리 건수: {}", total);
    }
}