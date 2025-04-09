package com.example.harumeonglog.domain.event.controller.port;

import com.example.harumeonglog.domain.event.controller.dto.request.EventRequest;
import com.example.harumeonglog.domain.event.controller.dto.response.EventResponse;


public interface EventService {
    EventResponse.EventCreateResponse createEvent(EventRequest.EventCreateRequest request);
    EventResponse.EventDayResponse getDayEvents(String date);
    EventResponse.BaseEventResponse getEvent(Long eventId);
    EventResponse.BaseEventResponse updateEvent(Long eventId, EventRequest.EventUpdateRequest request);
    void deleteEvent(Long eventId);
    EventResponse.EventCompleteResponse completeEvent(Long eventId);
}
