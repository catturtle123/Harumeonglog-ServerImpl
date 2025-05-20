package com.example.harumeonglog.domain.event.service.query;

import com.example.harumeonglog.domain.event.converter.EventConverter;
import com.example.harumeonglog.domain.event.dto.response.EventResponse;
import com.example.harumeonglog.domain.event.entity.Event;
import com.example.harumeonglog.domain.event.entity.enums.EventCategory;
import com.example.harumeonglog.domain.event.repository.EventRepository;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.entity.Pet;
import com.example.harumeonglog.domain.pet.repository.PetRepository;
import com.example.harumeonglog.global.error.code.EventErrorCode;
import com.example.harumeonglog.global.error.code.PetErrorCode;
import com.example.harumeonglog.global.error.exception.EventException;
import com.example.harumeonglog.global.error.exception.PetException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventQueryServiceImpl implements EventQueryService{
    private final EventRepository eventRepository;
    private final PetRepository petRepository;

    @Override
    public EventResponse.EventDayResponse getDayEvents(LocalDate date, EventCategory category, Long cursor, Integer size, Member member) {

        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.ASC, "createdAt"));

        Pet pet = checkCurrentPetId(member);

        Slice<Event> eventSlice = (category == null) ?
                eventRepository.findByDateAndPetAndDeletedAtIsNull(date, pet, cursor, pageable) :
                eventRepository.findByDateAndCategoryAndPetAndDeletedAtIsNull(date, category, pet, cursor, pageable);


        return EventConverter.toEventDayResponse(eventSlice);
    }

    @Override
    public EventResponse.BaseEventResponse getEvent(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new EventException(EventErrorCode.NOT_FOUND));

        return EventConverter.toBaseEventResponse(event);
    }

    @Override
    public EventResponse.EventDatesResponse getEventDates(Member member, Integer year, Integer month) {
        Pet pet = checkCurrentPetId(member);

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<LocalDate> dates = eventRepository.findDistinctDatesByMemberAndDateBetweenAndDeletedAtIsNull(pet, startDate, endDate);

        return EventConverter.toEventDatesResponse(dates);
    }

    private Pet checkCurrentPetId(Member member){
        if(member.getCurrentPetId() == null){
            throw new PetException(PetErrorCode.CURRENT_PET_NOT_FOUND);
        }
        return petRepository.findById(member.getCurrentPetId()).orElseThrow(() -> new PetException(PetErrorCode.NOT_FOUND));
    }
}
