package com.example.harumeonglog.domain.event.scheduler;

import com.example.harumeonglog.domain.event.entity.Event;
import com.example.harumeonglog.domain.event.repository.EventRepository;
import com.example.harumeonglog.domain.member.entity.Setting;
import com.example.harumeonglog.domain.member.entity.enums.NoticeType;
import com.example.harumeonglog.domain.member.repository.SettingRepository;
import com.example.harumeonglog.global.error.code.SettingErrorCode;
import com.example.harumeonglog.global.error.exception.SettingException;
import com.example.harumeonglog.global.firebase.service.FcmService;
import com.example.harumeonglog.global.outbox.service.OutBoxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventScheduler {

    private final EventRepository eventRepository;
    private final FcmService fcmService;
    private final SettingRepository settingRepository;
    private final OutBoxService outBoxService;

    // 매일 아침 8시에 오늘의 일정 요약을 전송
    @Scheduled(cron = "0 0 8 * * ?")
    public void sendDailyEventSummary() {

        log.info("오늘의 일정 요약 처리를 시작합니다: {}", LocalDateTime.now());

        LocalDate today = LocalDate.now();
        List<Event> todayEvents = eventRepository.findByDateAndDeletedAtIsNull(today);

        // 회원별로 일정을 그룹화
        todayEvents.stream()
                .collect(Collectors.groupingBy(Event::getMember))
                .forEach((member, events) -> {
                    try {
                        Setting setting = settingRepository.findByMember(member).orElseThrow(() -> new SettingException(SettingErrorCode.SETTING_NOT_FOUND));
                        if(setting.getMorningAlarm().equals(Boolean.TRUE)){
                            String summaryMessage = createDailySummaryMessage(events);
                            fcmService.sendPushNotification(
                                    member,
                                    "오늘의 일정",
                                    summaryMessage,
                                    NoticeType.EVENT
                            );
                            String payload = createFcmPayload(member.getId(), "오늘의 알림", summaryMessage, NoticeType.EVENT, null);
                            outBoxService.saveFCMEvent(payload);
                            log.info("회원 {}에게 {}개의 일정 요약을 전송했습니다.",
                                    member.getId(), events.size());
                        }
                        else{
                            log.info("회원 {}의 morningAlarm이 비활성화되어 요약 알림을 보내지 않음", member.getId());
                        }

                    } catch (Exception e) {
                        log.error("회원 {}에게 일정 요약 전송에 실패했습니다: {}",
                                member.getId(), e.getMessage());
                    }
                });
    }

    // 매분마다 다가오는 일정 확인
    @Scheduled(cron = "0 * * * * ?")
    public void sendEventReminders() {
        log.info("다가오는 일정 알림을 확인합니다: {}", LocalDateTime.now());

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tenMinutesLater = now.plusMinutes(10);

        // 다음 10분 내 알림 설정이 된 일정 조회
        List<Event> upcomingEvents = eventRepository.findByDateAndTimeBetweenAndHasNoticeTrueAndDeletedAtIsNull(
                now.toLocalDate(),
                now.toLocalTime(),
                tenMinutesLater.toLocalTime()
        );

        for (Event event : upcomingEvents) {
            try {

                Setting setting = settingRepository.findByMember(event.getMember()).orElseThrow(() -> new SettingException(SettingErrorCode.SETTING_NOT_FOUND));
                if (event.getDeletedAt() == null && event.getHasNotice() && setting.getEventAlarm().equals(Boolean.TRUE)) {
                    String message = createReminderMessage(event);
                    fcmService.sendPushNotification(
                            event.getMember(),
                            "일정 알림: " + event.getTitle(),
                            message,
                            NoticeType.EVENT
                    );
                    String payload = createFcmPayload(event.getMember().getId(),
                            "일정 알림: " + event.getTitle(), message, NoticeType.EVENT, event.getId());
                    outBoxService.saveFCMEvent(payload);
                    log.info("일정 {}에 대한 알림을 회원 {}에게 전송했습니다.",
                            event.getId(), event.getMember().getId());
                }
                else {
                    log.info("일정 {} 알림 전송 조건 미충족 (회원 {}의 eventAlarm: {}, hasNotice: {})",
                            event.getId(), event.getMember().getId(),
                            setting != null ? setting.getEventAlarm() : null, event.getHasNotice());
                }
            } catch (Exception e) {
                log.error("일정 {}에 대한 알림 전송에 실패했습니다: {}",
                        event.getId(), e.getMessage());
            }
        }
    }

    private String createDailySummaryMessage(List<Event> events) {
        StringBuilder message = new StringBuilder("오늘의 알림\n");
        events.forEach(event ->
                message.append(String.format("[%s] %s\n",
                        event.getTime().toString(),
                        event.getTitle()))
        );
        return message.toString();
    }

    private String createReminderMessage(Event event) {
        return String.format("[%s] %s",
                event.getTime().toString(),
                event.getTitle());
    }

    private String createFcmPayload(Long memberId, String title, String body, NoticeType noticeType, Long eventId) {
        // 간소화된 JSON 페이로드
        StringBuilder payload = new StringBuilder("{");
        payload.append(String.format("\"memberId\": %d", memberId));
        payload.append(String.format(",\"title\": \"%s\"", title));
        payload.append(String.format(",\"body\": \"%s\"", body));
        payload.append(String.format(",\"noticeType\": \"%s\"", noticeType));
        if (eventId != null) {
            payload.append(String.format(",\"eventId\": %d", eventId));
        }
        payload.append("}");
        return payload.toString();
    }
}
