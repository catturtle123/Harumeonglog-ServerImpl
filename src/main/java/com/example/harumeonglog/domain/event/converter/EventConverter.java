package com.example.harumeonglog.domain.event.converter;

import com.example.harumeonglog.domain.event.dto.request.EventRequest;
import com.example.harumeonglog.domain.event.dto.response.EventResponse;
import com.example.harumeonglog.domain.event.entity.*;
import com.example.harumeonglog.domain.event.entity.enums.RepeatDay;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.entity.Pet;
import com.example.harumeonglog.global.error.code.EventErrorCode;
import com.example.harumeonglog.global.error.exception.EventException;
import org.springframework.data.domain.Slice;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class EventConverter {

    public static Event toEntity(EventRequest.EventCreateRequest request, Member member, Pet pet) {
        return toEntityWithAttributes(request, member, pet, request.getDate(),
                request.getIsRepeated(), request.getExpiredDate(), request.getRepeatDays(),
                true, null);
    }

    public static Event toRepeatedEvent(EventRequest.EventCreateRequest request, Member member, Pet pet,
                                        LocalDate newDate, Long originalEventId) {
        // 반복 이벤트는 반복 속성을 가지지 않음
        return toEntityWithAttributes(
                request, member, pet, newDate,
                false, null, null, // 반복 속성 제거
                false, originalEventId); // 원본 이벤트가 아님을 표시
    }

    public static Event toEntityWithAttributes(
            EventRequest.EventCreateRequest request, Member member, Pet pet,
            LocalDate date, Boolean isRepeated, LocalDate expiredDate,
            List<RepeatDay> repeatDays, Boolean isOriginalEvent, Long originalEventId) {

        switch (request.getCategory()) {
            case BATH:
                return BathEvent.builder()
                        .date(date)
                        .title(request.getTitle())
                        .isRepeated(isRepeated)
                        .expiredDate(expiredDate)
                        .hasNotice(request.getHasNotice())
                        .repeatDays(repeatDays)
                        .done(false)
                        .category(request.getCategory())
                        .member(member)
                        .pet(pet)
                        .isOriginalEvent(isOriginalEvent)
                        .originalEventId(originalEventId)
                        .build();

            case GENERAL:
                return GeneralEvent.builder()
                        .date(date)
                        .title(request.getTitle())
                        .isRepeated(isRepeated)
                        .expiredDate(expiredDate)
                        .hasNotice(request.getHasNotice())
                        .repeatDays(repeatDays)
                        .done(false)
                        .category(request.getCategory())
                        .member(member)
                        .pet(pet)
                        .details(request.getDetails())
                        .isOriginalEvent(isOriginalEvent)
                        .originalEventId(originalEventId)
                        .build();

            case HOSPITAL:
                return HospitalEvent.builder()
                        .date(date)
                        .title(request.getTitle())
                        .isRepeated(isRepeated)
                        .expiredDate(expiredDate)
                        .hasNotice(request.getHasNotice())
                        .repeatDays(repeatDays)
                        .done(false)
                        .category(request.getCategory())
                        .member(member)
                        .pet(pet)
                        .hospitalName(request.getHospitalName())
                        .department(request.getDepartment())
                        .cost(request.getCost())
                        .details(request.getDetails())
                        .isOriginalEvent(isOriginalEvent)
                        .originalEventId(originalEventId)
                        .build();

            case MEDICINE:
                return MedicineEvent.builder()
                        .date(date)
                        .title(request.getTitle())
                        .isRepeated(isRepeated)
                        .expiredDate(expiredDate)
                        .hasNotice(request.getHasNotice())
                        .repeatDays(repeatDays)
                        .done(false)
                        .category(request.getCategory())
                        .member(member)
                        .pet(pet)
                        .medicineName(request.getMedicineName())
                        .details(request.getDetails())
                        .isOriginalEvent(isOriginalEvent)
                        .originalEventId(originalEventId)
                        .build();

            case WALK:
                return WalkEvent.builder()
                        .date(date)
                        .title(request.getTitle())
                        .isRepeated(isRepeated)
                        .expiredDate(expiredDate)
                        .hasNotice(request.getHasNotice())
                        .repeatDays(repeatDays)
                        .done(false)
                        .category(request.getCategory())
                        .member(member)
                        .pet(pet)
                        .distance(request.getDistance())
                        .duration(request.getDuration())
                        .isOriginalEvent(isOriginalEvent)
                        .originalEventId(originalEventId)
                        .build();

            default:
                throw new EventException(EventErrorCode.INVALID_CATEGORY);
        }
    }

    public static EventResponse.EventCreateResponse toEventCreateResponse(Event event) {
        return EventResponse.EventCreateResponse.builder()
                .eventId(event.getId())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .build();
    }



    public static EventResponse.EventPreviewResponse toEventDto(Event event) {
        return EventResponse.EventPreviewResponse.builder()
                .id(event.getId())
                .done(event.getDone())
                .title(event.getTitle())
                .build();
    }

    public static EventResponse.EventDayResponse toEventDayResponse(Slice<Event> eventSlice) {
        List<EventResponse.EventPreviewResponse> content = eventSlice.getContent().stream()
                .map(EventConverter::toEventDto)
                .collect(Collectors.toList());

        Long nextCursor = content.isEmpty() ? null : content.get(content.size() - 1).getId();

        return EventResponse.EventDayResponse.builder()
                .events(content)
                .cursor(nextCursor)
                .hasNext(eventSlice.hasNext())
                .build();
    }


    public static RepeatDay toRepeatDay(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> RepeatDay.MON;
            case TUESDAY -> RepeatDay.TUE;
            case WEDNESDAY -> RepeatDay.WED;
            case THURSDAY -> RepeatDay.THU;
            case FRIDAY -> RepeatDay.FRI;
            case SATURDAY -> RepeatDay.SAT;
            case SUNDAY -> RepeatDay.SUN;
        };
    }


}
