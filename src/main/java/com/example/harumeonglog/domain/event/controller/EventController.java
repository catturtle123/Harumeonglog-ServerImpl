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
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController implements EventControllerSpecification {

    private final EventCommandService eventCommandService;
    private final EventQueryService eventQueryService;

    @PostMapping
    public CustomResponse<EventResponse.EventCreateResponse> createEvent(
            @RequestBody EventRequest.EventCreateRequest request,
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

    @PutMapping("/{eventId}")
    public CustomResponse<EventResponse.BaseEventResponse> updateEvent(
            @PathVariable Long eventId,
            @RequestBody EventRequest.EventUpdateRequest request){
        return CustomResponse.ok(eventCommandService.updateEvent(eventId, request));
    }

    @DeleteMapping("/{eventId}")
    public CustomResponse<String> deleteEvent(@PathVariable Long eventId){
        eventCommandService.deleteEvent(eventId);
        return CustomResponse.ok("일정 삭제가 완료되었습니다.");
    }

    @PatchMapping("/{eventId}")
    public CustomResponse<EventResponse.EventCompleteResponse> completeEvent(@PathVariable Long eventId){
        return CustomResponse.ok(eventCommandService.completeEvent(eventId));
    }
}
