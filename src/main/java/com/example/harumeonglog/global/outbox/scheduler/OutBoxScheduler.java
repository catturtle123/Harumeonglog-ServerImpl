package com.example.harumeonglog.global.outbox.scheduler;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.enums.NoticeType;
import com.example.harumeonglog.domain.member.repository.MemberRepository;
import com.example.harumeonglog.global.common.util.JsonUtils;
import com.example.harumeonglog.global.error.code.MemberErrorCode;
import com.example.harumeonglog.global.error.exception.JsonDeserializationException;
import com.example.harumeonglog.global.error.exception.MemberException;
import com.example.harumeonglog.global.firebase.service.FcmService;
import com.example.harumeonglog.global.outbox.dto.FcmPayload;
import com.example.harumeonglog.global.outbox.entity.OutBox;
import com.example.harumeonglog.global.outbox.entity.enums.EventType;
import com.example.harumeonglog.global.outbox.repository.OutBoxRepository;
import com.example.harumeonglog.global.outbox.service.OutBoxService;
import com.example.harumeonglog.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class OutBoxScheduler {

    private final OutBoxService outBoxService;
    private final MemberRepository memberRepository;
    private final FcmService fcmService;
    private final S3Util s3Util;

    private static final int MAX_RETRY_COUNT = 3;
    private final OutBoxRepository outBoxRepository;

    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void processOutBoxAboutFCM() {

        List<OutBox> successOutBox = new ArrayList<>();
        List<OutBox> failedOutBox = new ArrayList<>();

        List<OutBox> events = outBoxService.findTop100(MAX_RETRY_COUNT, EventType.FCM, 100);

        for (OutBox event : events) {
            try {
                FcmPayload fcmPayload = JsonUtils.fromJson(event.getPayload(), FcmPayload.class);

                Member receiver = memberRepository.findById(fcmPayload.getReceiverId())
                        .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

                fcmService.sendPushNotification(
                        receiver,
                        fcmPayload.getTitle(),
                        fcmPayload.getBody(),
                        NoticeType.valueOf(fcmPayload.getNoticeType())
                );

                log.info("OutBox 처리 성공: id={}", event.getId());

                successOutBox.add(event);

            } catch (JsonDeserializationException e) {
                log.error("JSON 역직렬화 실패 (id={}): {}", event.getId(), event.getPayload(), e);
                failedOutBox.add(event);
            } catch (MemberException e) {
                log.error("알림 수신자 없음 (id={}): {}", event.getId(), e.getMessage(), e);
                failedOutBox.add(event);
            } catch (Exception e) {
                log.error("FCM 전송 실패 또는 알 수 없는 에러 (id={}): {}", event.getId(), e.getMessage(), e);
                failedOutBox.add(event);
            }
        }
        if (!successOutBox.isEmpty()) {
            outBoxService.updateSuccessFCMOutBox(successOutBox);
        }

        if (!failedOutBox.isEmpty()) {
            outBoxService.updateFailedFCMOutBox(failedOutBox);
        }
    }

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void processOutBoxAboutS3() {
        List<OutBox> events = outBoxRepository.findByEventTypeAndProcessedFalse(EventType.S3);
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
