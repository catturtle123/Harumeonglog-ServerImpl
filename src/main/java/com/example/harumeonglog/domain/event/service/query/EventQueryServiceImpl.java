package com.example.harumeonglog.domain.event.service.query;

import com.example.harumeonglog.domain.event.converter.EventConverter;
import com.example.harumeonglog.domain.event.dto.response.EventResponse;
import com.example.harumeonglog.domain.event.entity.Event;
import com.example.harumeonglog.domain.event.entity.enums.EventCategory;
import com.example.harumeonglog.domain.event.repository.EventRepository;
import com.example.harumeonglog.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventQueryServiceImpl implements EventQueryService{
    private final EventRepository eventRepository;

    @Override
    public EventResponse.EventDayResponse getDayEvents(LocalDate date, EventCategory category, Long cursor, Integer size, Member member) {

        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.ASC, "createdAt"));


        Slice<Event> eventSlice = (category == null) ?
                eventRepository.findByDateAndMemberAndDeletedAtIsNull(date, member, cursor, pageable) :
                eventRepository.findByDateAndCategoryAndMemberAndDeletedAtIsNull(date, category, member, cursor, pageable);


        return EventConverter.toEventDayResponse(eventSlice);
    }

    @Override
    public EventResponse.BaseEventResponse getEvent(Long eventId) {
        return null;
    }
}
