package com.example.harumeonglog.domain.event.service.command;

import com.example.harumeonglog.domain.event.converter.EventConverter;
import com.example.harumeonglog.domain.event.dto.request.EventRequest;
import com.example.harumeonglog.domain.event.dto.response.EventResponse;
import com.example.harumeonglog.domain.event.entity.Event;
import com.example.harumeonglog.domain.event.repository.EventRepository;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.repository.MemberRepository;
import com.example.harumeonglog.domain.pet.entity.Pet;
import com.example.harumeonglog.domain.pet.repository.PetRepository;
import com.example.harumeonglog.global.error.code.PetErrorCode;
import com.example.harumeonglog.global.error.exception.PetException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EventCommandServiceImpl implements EventCommandService {
    private final EventRepository eventRepository;
    private final MemberRepository memberRepository;
    private final PetRepository petRepository;

    @Override
    public EventResponse.EventCreateResponse createEvent(EventRequest.EventCreateRequest request, Member member) {
        Pet pet = petRepository.findById(member.getCurrentPetId())
                .orElseThrow(() -> new PetException(PetErrorCode.NOT_FOUND));

        // 원본 일정 생성 및 저장
        Event originalEvent = EventConverter.toEntity(request, member, pet);
        Event savedOriginalEvent = eventRepository.save(originalEvent);

        // 반복 일정 처리
        if (Boolean.TRUE.equals(request.getIsRepeated()) &&
                request.getExpiredDate() != null &&
                request.getRepeatDays() != null &&
                !request.getRepeatDays().isEmpty()) {

            List<Event> repeatedEvents = generateRepeatedEvents(request, member, pet, savedOriginalEvent.getId());

            // 모든 반복 일정 저장
            if (!repeatedEvents.isEmpty()) {
                eventRepository.saveAll(repeatedEvents);
            }
        }

        return EventConverter.toEventCreateResponse(savedOriginalEvent);
    }


    @Override
    public EventResponse.BaseEventResponse updateEvent(Long eventId, EventRequest.EventUpdateRequest request) {
        return null;
    }

    @Override
    public void deleteEvent(Long eventId) {

    }

    @Override
    public EventResponse.EventCompleteResponse completeEvent(Long eventId) {
        return null;
    }


    private List<Event> generateRepeatedEvents(EventRequest.EventCreateRequest request, Member member,
                                               Pet pet, Long originalEventId) {
        List<Event> repeatedEvents = new ArrayList<>();
        LocalDate currentDate = request.getDate().plusDays(1); // 시작일 다음 날부터

        while (!currentDate.isAfter(request.getExpiredDate())) {
            if (request.getRepeatDays().contains(EventConverter.toRepeatDay(currentDate.getDayOfWeek()))) {
                // 반복 일정 생성
                Event repeatedEvent = EventConverter.toRepeatedEvent(
                        request, member, pet, currentDate, originalEventId);
                repeatedEvents.add(repeatedEvent);
            }
            currentDate = currentDate.plusDays(1);
        }

        return repeatedEvents;
    }


}
