package com.example.harumeonglog.domain.event.service.command;

import com.example.harumeonglog.domain.event.dto.request.EventRequest;
import com.example.harumeonglog.domain.event.dto.response.EventResponse;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.entity.Pet;


public interface EventCommandService {
    EventResponse.EventCreateResponse createEvent(EventRequest.EventRequestDTO request, Member member);
    EventResponse.EventCreateResponse createEventAfterWalk(EventRequest.EventRequestDTO request, Member member, Pet pet);
    EventResponse.BaseEventResponse updateEvent(Member member, Long eventId, EventRequest.EventRequestDTO request);
    void deleteEvent(Member member, Long eventId);
    EventResponse.EventPreviewResponse completeEvent(Member member, Long eventId);
}
