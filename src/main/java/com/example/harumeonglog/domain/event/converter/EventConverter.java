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
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class EventConverter {

    public static Event toEvent(EventRequest.EventRequestDTO request, Member member, Pet pet) {
        return toEntityWithAttributes(request, member, pet, request.getDate(),
                request.getIsRepeated(), request.getExpiredDate(), request.getRepeatDays(),
                true, null);
    }

    public static Event toRepeatedEvent(EventRequest.EventRequestDTO request, Member member, Pet pet,
                                        LocalDate newDate, Long originalEventId) {
        return toEntityWithAttributes(
                request, member, pet, newDate,
                false, null, null,
                false, originalEventId);
    }



    public static EventResponse.EventCreateResponse toEventCreateResponse(Event event) {
        return EventResponse.EventCreateResponse.builder()
                .eventId(event.getId())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .build();
    }



    public static EventResponse.EventPreviewResponse toEventPreviewResponse(Event event) {
        return EventResponse.EventPreviewResponse.builder()
                .id(event.getId())
                .done(event.getDone())
                .title(event.getTitle())
                .build();
    }

    public static EventResponse.EventDayResponse toEventDayResponse(Slice<Event> eventSlice) {
        List<EventResponse.EventPreviewResponse> content = eventSlice.getContent().stream()
                .map(EventConverter::toEventPreviewResponse)
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


    public static EventResponse.EventDatesResponse toEventDatesResponse(List<LocalDate> dates) {
        return EventResponse.EventDatesResponse.builder()
                .dates(dates)
                .build();
    }

    public static EventResponse.BaseEventResponse toBaseEventResponse(Event event) {
        switch (event.getCategory()) {
            case BATH:
                var bathBuilder = EventResponse.BathEventDetailResponse.builder();
                setCommonResponseFields(bathBuilder, event);
                return bathBuilder.build();

            case GENERAL:
                var generalBuilder = EventResponse.GeneralEventDetailResponse.builder();
                setCommonResponseFields(generalBuilder, event);
                generalBuilder.details(((GeneralEvent) event).getDetails());
                return generalBuilder.build();

            case HOSPITAL:
                var hospitalBuilder = EventResponse.HospitalEventDetailResponse.builder();
                setCommonResponseFields(hospitalBuilder, event);
                HospitalEvent hospitalEvent = (HospitalEvent) event;
                hospitalBuilder
                        .hospitalName(hospitalEvent.getHospitalName())
                        .department(hospitalEvent.getDepartment())
                        .cost(hospitalEvent.getCost())
                        .details(hospitalEvent.getDetails());
                return hospitalBuilder.build();

            case MEDICINE:
                var medicineBuilder = EventResponse.MedicineEventDetailResponse.builder();
                setCommonResponseFields(medicineBuilder, event);
                MedicineEvent medicineEvent = (MedicineEvent) event;
                medicineBuilder
                        .medicineName(medicineEvent.getMedicineName())
                        .details(medicineEvent.getDetails());
                return medicineBuilder.build();

            case WALK:
                var walkBuilder = EventResponse.WalkEventDetailResponse.builder();
                setCommonResponseFields(walkBuilder, event);
                WalkEvent walkEvent = (WalkEvent) event;
                walkBuilder
                        .distance(walkEvent.getDistance())
                        .duration(walkEvent.getDuration())
                        .details(walkEvent.getDetails());
                return walkBuilder.build();

            default:
                throw new EventException(EventErrorCode.INVALID_CATEGORY);
        }
    }


    private static void setCommonResponseFields(EventResponse.BaseEventResponse.BaseEventResponseBuilder<?, ?> builder, Event event) {
        builder
                .id(event.getId())
                .title(event.getTitle())
                .date(event.getDate())
                .isRepeated(event.getIsRepeated())
                .expiredDate(event.getExpiredDate())
                .hasNotice(event.getHasNotice())
                .category(event.getCategory())
                .time(event.getTime())
                .repeatDays(event.getRepeatDays())
                .updatedAt(event.getUpdatedAt());
    }

    private static Event toEntityWithAttributes(
            EventRequest.EventRequestDTO request, Member member, Pet pet,
            LocalDate date, Boolean isRepeated, LocalDate expiredDate,
            List<RepeatDay> repeatDays, Boolean isOriginalEvent, Long originalEventId) {

        BiFunction<Object, Event.EventBuilder<?, ?>, Event.EventBuilder<?, ?>> setCommonFields =
                (builder, commonBuilder) -> {
                    return ((Event.EventBuilder<?, ?>) builder)
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
                            .time(request.getTime())
                            .isOriginalEvent(isOriginalEvent)
                            .originalEventId(originalEventId);
                };

        switch (request.getCategory()) {
            case BATH:
                return setCommonFields.apply(BathEvent.builder(), null).build();

            case GENERAL:
                return ((GeneralEvent.GeneralEventBuilder) setCommonFields.apply(
                        GeneralEvent.builder(), null))
                        .details(request.getDetails())
                        .build();

            case HOSPITAL:
                return ((HospitalEvent.HospitalEventBuilder) setCommonFields.apply(
                        HospitalEvent.builder(), null))
                        .hospitalName(request.getHospitalName())
                        .department(request.getDepartment())
                        .cost(request.getCost())
                        .details(request.getDetails())
                        .build();

            case MEDICINE:
                return ((MedicineEvent.MedicineEventBuilder) setCommonFields.apply(
                        MedicineEvent.builder(), null))
                        .medicineName(request.getMedicineName())
                        .details(request.getDetails())
                        .build();

            case WALK:
                return ((WalkEvent.WalkEventBuilder) setCommonFields.apply(
                        WalkEvent.builder(), null))
                        .distance(request.getDistance())
                        .duration(request.getDuration())
                        .details(request.getDetails())
                        .build();

            default:
                throw new EventException(EventErrorCode.INVALID_CATEGORY);
        }
    }

}
