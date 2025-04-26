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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 반려동물입니다."));

        Event event = EventConverter.toEntity(request, member, pet);

        Event savedEvent = eventRepository.save(event);

        return EventConverter.toEventCreateResponse(savedEvent);
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
}
