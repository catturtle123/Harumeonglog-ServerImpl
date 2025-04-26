package com.example.harumeonglog.domain.event.converter;

import com.example.harumeonglog.domain.event.dto.request.EventRequest;
import com.example.harumeonglog.domain.event.dto.response.EventResponse;
import com.example.harumeonglog.domain.event.entity.*;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.entity.Pet;
import com.example.harumeonglog.global.error.code.EventErrorCode;
import com.example.harumeonglog.global.error.exception.EventException;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

public class EventConverter {

    public static Event toEntity(EventRequest.EventCreateRequest request, Member member, Pet pet) {
        switch (request.getCategory()) {
            case BATH:
                return BathEvent.builder()
                        .date(request.getDate())
                        .title(request.getTitle())
                        .isRepeated(request.getIsRepeated())
                        .expiredDate(request.getExpiredDate())
                        .hasNotice(request.getHasNotice())
                        .done(false)
                        .category(request.getCategory())
                        .member(member)
                        .pet(pet)
                        .build();

            case GENERAL:
                return GeneralEvent.builder()
                        .date(request.getDate())
                        .title(request.getTitle())
                        .isRepeated(request.getIsRepeated())
                        .expiredDate(request.getExpiredDate())
                        .hasNotice(request.getHasNotice())
                        .done(false)
                        .category(request.getCategory())
                        .member(member)
                        .pet(pet)
                        .details(request.getDetails())
                        .build();

            case HOSPITAL:
                return HospitalEvent.builder()
                        .date(request.getDate())
                        .title(request.getTitle())
                        .isRepeated(request.getIsRepeated())
                        .expiredDate(request.getExpiredDate())
                        .hasNotice(request.getHasNotice())
                        .done(false)
                        .category(request.getCategory())
                        .member(member)
                        .pet(pet)
                        .hospitalName(request.getHospitalName())
                        .department(request.getDepartment())
                        .cost(request.getCost())
                        .details(request.getDetails())
                        .build();

            case MEDICINE:
                return MedicineEvent.builder()
                        .date(request.getDate())
                        .title(request.getTitle())
                        .isRepeated(request.getIsRepeated())
                        .expiredDate(request.getExpiredDate())
                        .hasNotice(request.getHasNotice())
                        .done(false)
                        .category(request.getCategory())
                        .member(member)
                        .pet(pet)
                        .medicineName(request.getMedicineName())
                        .details(request.getDetails())
                        .build();

            case WALK:
                return WalkEvent.builder()
                        .date(request.getDate())
                        .title(request.getTitle())
                        .isRepeated(request.getIsRepeated())
                        .expiredDate(request.getExpiredDate())
                        .hasNotice(request.getHasNotice())
                        .done(false)
                        .category(request.getCategory())
                        .member(member)
                        .pet(pet)
                        .distance(request.getDistance())
                        .duration(request.getDuration())
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
}
