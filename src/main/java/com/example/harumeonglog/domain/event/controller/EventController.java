package com.example.harumeonglog.domain.event.controller;


import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.domain.event.dto.request.EventRequest;
import com.example.harumeonglog.domain.event.dto.response.EventResponse;
import com.example.harumeonglog.domain.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<CustomResponse<EventResponse.EventCreateResponse>> createEvent(
            @RequestBody EventRequest.EventCreateRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CustomResponse.created(eventService.createEvent(request)));
    }

    @GetMapping
    public CustomResponse<EventResponse.EventDayResponse> getDayEvent(@RequestParam("date") String date){
        return CustomResponse.ok(eventService.getDayEvents(date));
    }

    @GetMapping("/{eventId}")
    public CustomResponse<EventResponse.BaseEventResponse> getEvent(@PathVariable Long eventId){
        return CustomResponse.ok(eventService.getEvent(eventId));
    }

    @PutMapping("/{eventId}")
    public CustomResponse<EventResponse.BaseEventResponse> updateEvent(
            @PathVariable Long eventId,
            @RequestBody EventRequest.EventUpdateRequest request){
        return CustomResponse.ok(eventService.updateEvent(eventId, request));
    }

    @DeleteMapping("/{eventId}")
    public CustomResponse<String> deleteEvent(@PathVariable Long eventId){
        eventService.deleteEvent(eventId);
        return CustomResponse.ok("일정 삭제가 완료되었습니다.");
    }

    @PatchMapping("/{eventId}")
    public CustomResponse<EventResponse.EventCompleteResponse> completeEvent(@PathVariable Long eventId){
        return CustomResponse.ok(eventService.completeEvent(eventId));
    }
}
