package com.example.harumeonglog.global.outbox.scheduler;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.enums.NoticeType;
import com.example.harumeonglog.domain.member.repository.MemberRepository;
import com.example.harumeonglog.global.error.code.MemberErrorCode;
import com.example.harumeonglog.global.error.exception.JsonDeserializationException;
import com.example.harumeonglog.global.error.exception.MemberException;
import com.example.harumeonglog.global.outbox.dto.FcmPayload;
import com.example.harumeonglog.global.outbox.entity.OutBox;
import com.example.harumeonglog.global.outbox.entity.enums.EventType;
import com.example.harumeonglog.global.outbox.service.OutBoxService;
import com.example.harumeonglog.global.common.util.JsonUtils;
import com.example.harumeonglog.global.firebase.service.FcmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
}
