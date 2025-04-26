package com.example.harumeonglog.domain.event.service.query;

import com.example.harumeonglog.domain.event.dto.response.EventResponse;
import org.springframework.stereotype.Service;

@Service
public class EventQueryServiceImpl implements EventQueryService{
    @Override
    public EventResponse.EventDayResponse getDayEvents(String date) {
        return null;
    }

    @Override
    public EventResponse.BaseEventResponse getEvent(Long eventId) {
        return null;
    }
}
