package com.example.harumeonglog.global.common.scheduler;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.enums.NoticeType;
import com.example.harumeonglog.domain.member.repository.MemberRepository;
import com.example.harumeonglog.global.common.entity.OutBox;
import com.example.harumeonglog.global.common.service.OutBoxService;
import com.example.harumeonglog.global.firebase.service.FcmService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;

    private static final int MAX_RETRY_COUNT = 3;

    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void processOutBoxAboutFCM() {
        List<OutBox> events = outBoxService.findTop100(MAX_RETRY_COUNT);

        for (OutBox event : events) {
            try {
                JsonNode payload = objectMapper.readTree(event.getPayload());

                Long memberId = payload.get("receiverId").asLong();  // 알림 받을 사람
                String title = payload.get("title").asText();
                String body = payload.get("body").asText();
                String noticeType = payload.get("noticeType").asText();

                Member receiver = memberRepository.findById(memberId)
                        .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

                fcmService.sendPushNotification(receiver, title, body, NoticeType.valueOf(noticeType));

                event.markProcessed(); // OutBox processed = true 처리
                log.info("OutBox 처리 성공: id={}", event.getId());

            } catch (Exception e) {
                log.error("OutBox 처리 실패: id={}", event.getId(), e);
                event.increaseRetryCount();
            }
        }
    }
}
