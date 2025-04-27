package com.example.harumeonglog.domain.event.controller;


import com.example.harumeonglog.domain.event.controller.sepcification.EventControllerSpecification;
import com.example.harumeonglog.domain.event.entity.enums.EventCategory;
import com.example.harumeonglog.domain.event.service.query.EventQueryService;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.domain.event.dto.request.EventRequest;
import com.example.harumeonglog.domain.event.dto.response.EventResponse;
import com.example.harumeonglog.domain.event.service.command.EventCommandService;
import com.example.harumeonglog.global.security.annotation.AuthenticatedMember;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
public class EventController implements EventControllerSpecification {

    private final EventCommandService eventCommandService;
    private final EventQueryService eventQueryService;

    @PostMapping
    public CustomResponse<EventResponse.EventCreateResponse> createEvent(
            @RequestBody EventRequest.EventRequestDTO request,
            @AuthenticatedMember Member member) {
        return CustomResponse.created(eventCommandService.createEvent(request, member));
    }

    @GetMapping
    public CustomResponse<EventResponse.EventDayResponse> getDayEvent(
            @AuthenticatedMember Member member,
            @RequestParam(defaultValue = "2025-04-26") LocalDate date,
            @RequestParam(required = false) EventCategory category,
            @RequestParam Integer size,
            @RequestParam Long cursor){
        return CustomResponse.ok(eventQueryService.getDayEvents(date, category, cursor, size, member));
    }

    @GetMapping("/{eventId}")
    public CustomResponse<EventResponse.BaseEventResponse> getEvent(@PathVariable Long eventId){
        return CustomResponse.ok(eventQueryService.getEvent(eventId));
    }

    @GetMapping("/monthly-dates")
    public CustomResponse<EventResponse.EventDatesResponse> getEventDates(
            @AuthenticatedMember Member member,
            @RequestParam @Min(2000) @Max(9999) Integer year,
            @RequestParam @Min(1) @Max(12) Integer month) {
        return CustomResponse.ok(eventQueryService.getEventDates(member, year, month));
    }

    @PutMapping("/{eventId}")
    public CustomResponse<EventResponse.BaseEventResponse> updateEvent(
            @AuthenticatedMember Member member,
            @PathVariable Long eventId,
            @RequestBody EventRequest.EventRequestDTO request){
        return CustomResponse.ok(eventCommandService.updateEvent(member, eventId, request));
    }

    @DeleteMapping("/{eventId}")
    public CustomResponse<String> deleteEvent(@PathVariable Long eventId,
                                              @AuthenticatedMember Member member){
        eventCommandService.deleteEvent(member, eventId);
        return CustomResponse.ok("일정 삭제가 완료되었습니다.");
    }

    @PatchMapping("/{eventId}")
    public CustomResponse<EventResponse.EventPreviewResponse> completeEvent(@PathVariable Long eventId,
                                                                             @AuthenticatedMember Member member){
        return CustomResponse.ok(eventCommandService.completeEvent(member, eventId));
    }
}
