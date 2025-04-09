package com.example.harumeonglog.domain.event.service;

import com.example.harumeonglog.domain.event.controller.dto.request.EventRequest;
import com.example.harumeonglog.domain.event.controller.dto.response.EventResponse;
import com.example.harumeonglog.domain.event.controller.port.EventService;
import com.example.harumeonglog.domain.event.domain.enums.EventCategory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class EventServiceImplTest {

    private EventService eventService;

    @BeforeEach
    void init() {
        this.eventService = new EventServiceImpl();
    }

    @Test
    @DisplayName("일정을 생성할 수 있다.")
    void createEvent() {
        // given
        EventRequest.EventCreateRequest request = new EventRequest.EventCreateRequest(
                "강아지 산책",
                LocalDate.of(2025, 4, 7),
                false,
                null,
                true,
                EventCategory.WALK,
                null,
                null,
                null,
                null,
                null,
                "2km",
                "30분"
        );

        // when
        EventResponse.EventCreateResponse response = eventService.createEvent(request);

        // then
        assertThat(response).isNull();
    }

    @Test
    @DisplayName("하루의 일정 목록을 불러올 수 있다.")
    void getDayEvents() {
        // given
        String date = "2025-04-07";

        // when
        EventResponse.EventDayResponse response = eventService.getDayEvents(date);

        // then
        assertThat(response).isNull();
    }

    @Test
    @DisplayName("특정 일정을 조회할 수 있다.")
    void getEvent() {
        // given
        Long eventId = 1L;

        // when
        EventResponse.BaseEventResponse response = eventService.getEvent(eventId);

        // then
        assertThat(response).isNull();
    }

    @Test
    @DisplayName("일정을 수정할 수 있다.")
    void updateEvent() {
        // given
        Long eventId = 1L;
        EventRequest.EventUpdateRequest request = new EventRequest.EventUpdateRequest(
                "강아지 산책 수정",
                LocalDate.of(2025, 4, 7),
                false,
                null,
                true,
                EventCategory.WALK,
                null,
                null,
                null,
                null,
                null,
                "3km",
                "40분"
        );

        // when
        EventResponse.BaseEventResponse response = eventService.updateEvent(eventId, request);
        // then
        assertThat(response).isNull();
    }

    @Test
    @DisplayName("일정을 삭제할 수 있다.")
    void deleteEvent() {
        // given
        Long eventId = 1L;

        // when
        eventService.deleteEvent(eventId);

        // then
    }

    @Test
    @DisplayName("일정을 완료할 수 있다.")
    void completeEvent() {
        // given
        Long eventId = 1L;

        // when
        EventResponse.EventCompleteResponse response = eventService.completeEvent(eventId);

        // then
        assertThat(response).isNull();
    }
}