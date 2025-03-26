package com.example.harumeonglog.domain.event.controller.port;

import com.example.harumeonglog.domain.event.controller.dto.request.EventRequest;
import com.example.harumeonglog.domain.event.controller.dto.response.EventResponse;


public interface EventService {
    EventResponse.EventCreateResponse createEvent(EventRequest.EventCreateRequest request);
    EventResponse.EventListResponse getDayEvents(String date);
    EventResponse.EventDetailResponse getEvent(Long eventId);
    EventResponse.EventUpdateResponse updateEvent(Long eventId, EventRequest.EventUpdateRequest request);
    void deleteEvent(Long eventId);
    EventResponse.EventCompleteResponse completeEvent(Long eventId);
}
