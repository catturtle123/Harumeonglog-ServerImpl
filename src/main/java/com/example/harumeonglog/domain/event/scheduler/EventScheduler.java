package com.example.harumeonglog.domain.event.scheduler;

import com.example.harumeonglog.domain.event.entity.Event;
import com.example.harumeonglog.domain.event.repository.EventRepository;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.Setting;
import com.example.harumeonglog.domain.member.entity.enums.NoticeType;
import com.example.harumeonglog.domain.member.repository.SettingRepository;
import com.example.harumeonglog.global.firebase.service.FcmService;
import com.example.harumeonglog.global.outbox.service.OutBoxService;
import com.example.harumeonglog.global.util.FcmUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventScheduler {

    private final EventRepository eventRepository;
    private final FcmService fcmService;
    private final SettingRepository settingRepository;
    private final OutBoxService outBoxService;
    private final FcmUtil fcmUtil;

    private static final int REMINDER_MINUTES = 10;

    // 매일 아침 8시에 오늘의 일정 요약을 전송
    @Scheduled(cron = "0 0 8 * * ?")
    @Transactional(readOnly = true)
    public void sendDailyEventSummary() {
        log.info("오늘의 일정 요약 처리를 시작합니다: {}", LocalDateTime.now());

        try {
            LocalDate today = LocalDate.now();
            List<Event> todayEvents = eventRepository.findByDateAndDeletedAtIsNull(today);

            if (todayEvents.isEmpty()) {
                log.info("오늘 전송할 일정이 없습니다.");
                return;
            }

            // 회원별로 일정을 그룹화
            Map<Member, List<Event>> eventsByMember = todayEvents.stream()
                    .collect(Collectors.groupingBy(Event::getMember));

            // 해당 회원들의 설정을 일괄 조회하여 N+1 문제 해결
            List<Member> members = eventsByMember.keySet().stream().toList();
            Map<Member, Setting> settingsByMember = getSettingsByMembers(members);

            // 각 회원별로 알림 전송
            eventsByMember.forEach((member, events) ->
                    sendDailySummaryToMember(member, events, settingsByMember.get(member)));

            log.info("오늘의 일정 요약 처리를 완료했습니다. 대상 회원 수: {}", eventsByMember.size());

        } catch (Exception e) {
            log.error("오늘의 일정 요약 처리 중 오류가 발생했습니다: {}", e.getMessage(), e);
        }
    }

    // 매분마다 다가오는 일정 확인
    @Scheduled(cron = "0 * * * * ?")
    @Transactional // 읽기 전용 제거
    public void sendEventReminders() {
        log.info("다가오는 일정 알림을 확인합니다: {}", LocalDateTime.now());

        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime reminderTime = now.plusMinutes(REMINDER_MINUTES);

            // 다음 설정된 시간 내 알림 설정이 되고 아직 알림을 보내지 않은 일정 조회
            List<Event> upcomingEvents = eventRepository.findByDateAndTimeBetweenAndHasNoticeTrueAndIsNoticedFalseAndDeletedAtIsNull(
                    now.toLocalDate(),
                    now.toLocalTime(),
                    reminderTime.toLocalTime()
            );

            if (upcomingEvents.isEmpty()) {
                log.debug("다가오는 일정이 없습니다.");
                return;
            }

            // 해당 회원들의 설정을 일괄 조회
            List<Member> members = upcomingEvents.stream()
                    .map(Event::getMember)
                    .distinct()
                    .toList();
            Map<Member, Setting> settingsByMember = getSettingsByMembers(members);

            // 각 일정별로 알림 전송
            upcomingEvents.forEach(event ->
                    sendEventReminderToMember(event, settingsByMember.get(event.getMember())));

            log.info("다가오는 일정 알림 처리를 완료했습니다. 처리된 일정 수: {}", upcomingEvents.size());

        } catch (Exception e) {
            log.error("다가오는 일정 알림 처리 중 오류가 발생했습니다: {}", e.getMessage(), e);
        }
    }

    private void sendDailySummaryToMember(Member member, List<Event> events, Setting setting) {
        try {
            if (setting == null) {
                log.warn("회원 {}의 설정 정보를 찾을 수 없습니다.", member.getId());
                return;
            }

            if (!Boolean.TRUE.equals(setting.getMorningAlarm())) {
                log.info("회원 {}의 morningAlarm이 비활성화되어 요약 알림을 보내지 않습니다.", member.getId());
                return;
            }

            String summaryMessage = createDailySummaryMessage(events);
            String title = "오늘의 일정";

            fcmService.sendPushNotification(member, title, summaryMessage, NoticeType.EVENT);

            String payload = fcmUtil.createFcmPayload(member.getId(), title, summaryMessage, NoticeType.EVENT, null);
            outBoxService.saveFCMEvent(payload);

            log.info("회원 {}에게 {}개의 일정 요약을 전송했습니다.", member.getId(), events.size());

        } catch (Exception e) {
            log.error("회원 {}에게 일정 요약 전송에 실패했습니다: {}", member.getId(), e.getMessage(), e);
        }
    }

    private void sendEventReminderToMember(Event event, Setting setting) {
        try {
            if (setting == null) {
                log.warn("회원 {}의 설정 정보를 찾을 수 없습니다.", event.getMember().getId());
                return;
            }

            if (!isValidForReminder(event, setting)) {
                log.debug("일정 {} 알림 전송 조건 미충족", event.getId());
                return;
            }

            // 알림 전송 처리
            event.updateIsNoticed(Boolean.TRUE);

            String message = createReminderMessage(event);
            String title = "일정 알림: " + event.getTitle();

            fcmService.sendPushNotification(event.getMember(), title, message, NoticeType.EVENT);

            String payload = fcmUtil.createFcmPayload(event.getMember().getId(), title, message, NoticeType.EVENT, event.getId());
            outBoxService.saveFCMEvent(payload);

            log.info("일정 {}에 대한 알림을 회원 {}에게 전송했습니다.", event.getId(), event.getMember().getId());

        } catch (Exception e) {
            log.error("일정 {}에 대한 알림 전송에 실패했습니다: {}", event.getId(), e.getMessage(), e);
        }
    }

    private Map<Member, Setting> getSettingsByMembers(List<Member> members) {
        try {
            List<Setting> settings = settingRepository.findByMemberIn(members);
            return settings.stream()
                    .collect(Collectors.toMap(Setting::getMember, setting -> setting));
        } catch (Exception e) {
            log.error("회원 설정 정보 조회 중 오류가 발생했습니다: {}", e.getMessage(), e);
            return new HashMap<>();
        }
    }

    private boolean isValidForReminder(Event event, Setting setting) {
        return event.getDeletedAt() == null
                && Boolean.TRUE.equals(event.getHasNotice())
                && Boolean.TRUE.equals(setting.getEventAlarm())
                && !Boolean.TRUE.equals(event.getIsNoticed());
    }

    private String createDailySummaryMessage(List<Event> events) {
        StringBuilder message = new StringBuilder("오늘의 일정\n");
        events.forEach(event ->
                message.append(String.format("[%s] %s\n",
                        formatTime(event.getTime()),
                        event.getTitle()))
        );
        return message.toString();
    }

    private String createReminderMessage(Event event) {
        return String.format("[%s] %s",
                formatTime(event.getTime()),
                event.getTitle());
    }

    private String formatTime(Object time) {
        return time != null ? time.toString() : "시간 미정";
    }


}