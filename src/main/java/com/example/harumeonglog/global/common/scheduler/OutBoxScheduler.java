package com.example.harumeonglog.global.common.scheduler;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.enums.NoticeType;
import com.example.harumeonglog.domain.member.repository.MemberRepository;
import com.example.harumeonglog.global.common.dto.FcmPayload;
import com.example.harumeonglog.global.common.entity.OutBox;
import com.example.harumeonglog.global.common.entity.enums.EventType;
import com.example.harumeonglog.global.common.service.OutBoxService;
import com.example.harumeonglog.global.common.util.JsonUtils;
import com.example.harumeonglog.global.firebase.service.FcmService;
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

    private final OutBoxService outBoxService;
    private final MemberRepository memberRepository;
    private final FcmService fcmService;

    private static final int MAX_RETRY_COUNT = 3;

    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void processOutBoxAboutFCM() {
        List<OutBox> events = outBoxService.findTop100(MAX_RETRY_COUNT, EventType.FCM, 100);

        for (OutBox event : events) {
            try {
                FcmPayload fcmPayload = JsonUtils.fromJson(event.getPayload(), FcmPayload.class);

                Member receiver = memberRepository.findById(fcmPayload.getReceiverId())
                        .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

                fcmService.sendPushNotification(receiver, fcmPayload.getTitle(), fcmPayload.getBody(), NoticeType.valueOf(fcmPayload.getNoticeType()));

                event.markProcessed(); // OutBox processed = true 처리
                log.info("OutBox 처리 성공: id={}", event.getId());

            } catch (Exception e) {
                log.error("OutBox 처리 실패: id={}", event.getId(), e);
                event.increaseRetryCount();
            }
        }
    }
}
