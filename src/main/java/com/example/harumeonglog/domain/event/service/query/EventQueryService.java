package com.example.harumeonglog.domain.event.service.query;

import com.example.harumeonglog.domain.event.dto.response.EventResponse;

public interface EventQueryService {
    EventResponse.EventDayResponse getDayEvents(String date);
    EventResponse.BaseEventResponse getEvent(Long eventId);
}
