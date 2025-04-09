package com.example.harumeonglog.domain.event.service;

import com.example.harumeonglog.domain.event.controller.dto.request.EventRequest;
import com.example.harumeonglog.domain.event.controller.dto.response.EventResponse;
import com.example.harumeonglog.domain.event.controller.port.EventService;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService {
    @Override
    public EventResponse.EventCreateResponse createEvent(EventRequest.EventCreateRequest request) {
        return null;
    }

    @Override
    public EventResponse.EventDayResponse getDayEvents(String date) {
        return null;
    }

    @Override
    public EventResponse.BaseEventResponse getEvent(Long eventId) {
        return null;
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
