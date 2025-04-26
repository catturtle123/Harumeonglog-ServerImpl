package com.example.harumeonglog.domain.event.service.command;

import com.example.harumeonglog.domain.event.dto.request.EventRequest;
import com.example.harumeonglog.domain.event.dto.response.EventResponse;
import com.example.harumeonglog.domain.member.entity.Member;


public interface EventCommandService {
    EventResponse.EventCreateResponse createEvent(EventRequest.EventCreateRequest request, Member member);
    EventResponse.BaseEventResponse updateEvent(Long eventId, EventRequest.EventUpdateRequest request);
    void deleteEvent(Long eventId);
    EventResponse.EventCompleteResponse completeEvent(Long eventId);
}
