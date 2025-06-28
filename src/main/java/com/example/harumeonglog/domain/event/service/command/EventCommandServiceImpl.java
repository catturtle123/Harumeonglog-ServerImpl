package com.example.harumeonglog.domain.event.service.command;

import com.example.harumeonglog.domain.event.converter.EventConverter;
import com.example.harumeonglog.domain.event.dto.request.EventRequest;
import com.example.harumeonglog.domain.event.dto.response.EventResponse;
import com.example.harumeonglog.domain.event.entity.Event;
import com.example.harumeonglog.domain.event.repository.EventRepository;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.enums.MemberPetRole;
import com.example.harumeonglog.domain.pet.entity.MemberPet;
import com.example.harumeonglog.domain.pet.entity.Pet;
import com.example.harumeonglog.domain.pet.repository.MemberPetRepository;
import com.example.harumeonglog.domain.pet.repository.PetRepository;
import com.example.harumeonglog.global.error.code.PetErrorCode;
import com.example.harumeonglog.global.error.exception.EventException;
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
    private final MemberPetRepository memberPetRepository;
    private final PetRepository petRepository;

    @Override
    public EventResponse.EventCreateResponse createEvent(EventRequest.EventRequestDTO request, Member member) {
        Pet pet = petRepository.findById(member.getCurrentPetId())
                .orElseThrow(() -> new PetException(PetErrorCode.NOT_FOUND));

        checkMemberRole(member, pet);

        // 원본 일정 생성 및 저장
        Event originalEvent = EventConverter.toEvent(request, member, pet);
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
    public EventResponse.EventCreateResponse createEventAfterWalk(EventRequest.EventRequestDTO request, Member member, Pet pet) {
        Event originalEvent = EventConverter.toEvent(request, member, pet);
        Event savedOriginalEvent = eventRepository.save(originalEvent);
        return EventConverter.toEventCreateResponse(savedOriginalEvent);
    }

    @Override
    public EventResponse.BaseEventResponse updateEvent(Member member, Long eventId, EventRequest.EventRequestDTO request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventException(PetErrorCode.NOT_FOUND));

        checkMemberRole(member, event.getPet());

        // 카테고리 변경 여부에 따른 처리
        if (!event.getCategory().equals(request.getCategory())) {
            // 카테고리 변경이 있는 경우 - 엔티티 재생성
            event = recreateEventWithNewCategory(event, request, member);
        } else {
            // 기본 정보 업데이트
            event.update(request.getTitle(), request.getDate(), request.getHasNotice(), request.getTime(), request.getCategory());
        }

        // 반복 설정 변경 처리
        handleRepeatSettingChange(event, request, member);

        return EventConverter.toBaseEventResponse(event);
    }


    @Override
    public void deleteEvent(Member member, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventException(PetErrorCode.NOT_FOUND));

        checkMemberRole(member, event.getPet());

        event.softDelete();
    }

    @Override
    public EventResponse.EventPreviewResponse completeEvent(Member member, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventException(PetErrorCode.NOT_FOUND));

        checkMemberRole(member, event.getPet());

        event.check();

        return EventConverter.toEventPreviewResponse(event);
    }


    // 반복 일정 생성
    private List<Event> generateRepeatedEvents(EventRequest.EventRequestDTO request, Member member,
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

    private void checkMemberRole(Member member, Pet pet){
        MemberPet memberPet = memberPetRepository.findByMemberAndPet(member, pet)
                .orElseThrow(() -> new PetException(PetErrorCode.NOT_IN_GROUP));
        if(!memberPet.getRole().equals(MemberPetRole.OWNER)){
            throw new PetException(PetErrorCode.INVALID_ROLE);
        }
    }


    // 카테고리 변경 시 새로운 엔티티 교체
    private Event recreateEventWithNewCategory(Event oldEvent, EventRequest.EventRequestDTO request, Member member) {
        // 기존 엔티티 삭제
        eventRepository.delete(oldEvent);
        eventRepository.flush();

        // 새로운 카테고리의 엔티티 생성
        Event newEvent = EventConverter.toEvent(request, member, oldEvent.getPet());
        newEvent.updateId(oldEvent.getId()); // ID 유지

        // 새 엔티티 저장
        return eventRepository.save(newEvent);
    }

    // 반복 설정 처리
    private void handleRepeatSettingChange(Event event, EventRequest.EventRequestDTO request, Member member) {
        // 원본 일정인지 확인 (원본 일정만 반복 일정 관리 가능)
        boolean isOriginalEvent = event.getIsOriginalEvent() == null || event.getIsOriginalEvent();

        if (!isOriginalEvent) {
            // 반복 생성된 일정인 경우 반복 설정을 변경하지 않음
            return;
        }

        boolean wasRepeated = event.getIsRepeated() != null && event.getIsRepeated();
        boolean willBeRepeated = request.getIsRepeated() != null && request.getIsRepeated();

        if (!wasRepeated && willBeRepeated) {
            // 반복 설정 추가
            addRepeatSetting(event, request, member);
        } else if (wasRepeated && willBeRepeated) {
            // 반복 설정 수정
            updateRepeatSetting(event, request, member);
        } else if (wasRepeated && !willBeRepeated) {
            // 반복 설정 제거
            removeRepeatSetting(event);
        }
    }

    // 반복 일정 추가
    private void addRepeatSetting(Event event, EventRequest.EventRequestDTO request, Member member) {
        // 반복 일정 생성
        List<Event> repeatedEvents = generateRepeatedEvents(request, member, event.getPet(), event.getId());

        // 모든 반복 일정 저장
        if (!repeatedEvents.isEmpty()) {
            eventRepository.saveAll(repeatedEvents);
        }

        // 원본 일정의 반복 설정 업데이트
        event.updateRepeat(true, request.getExpiredDate(), request.getRepeatDays());
    }

    // 반복 일정 수정
    private void updateRepeatSetting(Event event, EventRequest.EventRequestDTO request, Member member) {
        // 기존 반복 일정 삭제
        eventRepository.deleteByOriginalEventId(event.getId());

        // 새로운 반복 일정 생성
        List<Event> repeatedEvents = generateRepeatedEvents(request, member, event.getPet(), event.getId());
        if (!repeatedEvents.isEmpty()) {
            eventRepository.saveAll(repeatedEvents);
        }

        // 원본 일정의 반복 설정 업데이트
        event.updateRepeat(true, request.getExpiredDate(), request.getRepeatDays());
    }

    // 반복 일정 제거
    private void removeRepeatSetting(Event event) {
        // 기존 반복 일정 삭제
        eventRepository.deleteByOriginalEventId(event.getId());

        // 원본 일정의 반복 설정 제거
        event.updateRepeat(false, null, null);
    }

}
