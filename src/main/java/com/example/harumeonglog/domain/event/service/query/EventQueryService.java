package com.example.harumeonglog.domain.event.service.query;

import com.example.harumeonglog.domain.event.dto.response.EventResponse;
import com.example.harumeonglog.domain.event.entity.enums.EventCategory;
import com.example.harumeonglog.domain.member.entity.Member;

import java.time.LocalDate;

public interface EventQueryService {
    EventResponse.EventDayResponse getDayEvents(LocalDate date, EventCategory category, Long cursor, Integer size, Member member);
    EventResponse.BaseEventResponse getEvent(Long eventId);
    EventResponse.EventDatesResponse getEventDates(Member member, Integer year, Integer month);
}
