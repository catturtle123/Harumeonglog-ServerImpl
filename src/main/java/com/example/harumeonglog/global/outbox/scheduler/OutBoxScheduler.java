package com.example.harumeonglog.global.outbox.scheduler;

import com.example.harumeonglog.global.outbox.entity.OutBox;
import com.example.harumeonglog.global.outbox.entity.enums.EventType;
import com.example.harumeonglog.global.outbox.repository.OutBoxRepository;
import com.example.harumeonglog.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class OutBoxScheduler {

    private final S3Util s3Util;

    private static final int MAX_RETRY_COUNT = 3;
    private final OutBoxRepository outBoxRepository;

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void processOutBoxAboutS3() {
        List<OutBox> events = outBoxRepository.findByEventTypeAndProcessedFalseAndRetryCountLessThan(
                EventType.S3, MAX_RETRY_COUNT);
        events.forEach(event -> {
            try {
                String imageKey = event.getPayload();
                if (s3Util.isObjectExists(imageKey)) {
                    log.info("S3 파일 확인 성공: key={}", imageKey);
                    s3Util.deleteFile(imageKey);
                } else {
                    log.warn("S3 파일이 존재하지 않음: key={}", imageKey);
                    event.increaseRetryCount();
                    if(event.getRetryCount() >= MAX_RETRY_COUNT) {
                        log.error("최대 재시도 횟수 초과. OutBox 제거: key={}", imageKey);
                    }
                }
            } catch (Exception e) {
                log.error("S3 파일 확인 실패 (key={}): {}", event.getPayload(), e.getMessage(), e);
                event.increaseRetryCount();
            }
        });
    }
}
