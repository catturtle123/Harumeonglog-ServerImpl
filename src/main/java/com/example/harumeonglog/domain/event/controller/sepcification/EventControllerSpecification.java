package com.example.harumeonglog.domain.event.controller.sepcification;

import com.example.harumeonglog.domain.event.dto.request.EventRequest;
import com.example.harumeonglog.domain.event.dto.response.EventResponse;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.global.common.response.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "일정 API", description = "일정 관련 API 입니다.")
public interface EventControllerSpecification {

    @Operation(summary = "일정 추가 API by 백종우", description = "일정을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON201", description = "일정 등록 성공")
    })
    @PostMapping
    CustomResponse<EventResponse.EventCreateResponse> createEvent(
            @RequestBody EventRequest.EventCreateRequest request,
            @AuthenticationPrincipal Member member);

    @GetMapping
    CustomResponse<EventResponse.EventDayResponse> getDayEvent(@RequestParam("date") String date);

    @GetMapping("/{eventId}")
    CustomResponse<EventResponse.BaseEventResponse> getEvent(@PathVariable Long eventId);

    @PutMapping("/{eventId}")
    CustomResponse<EventResponse.BaseEventResponse> updateEvent(
            @PathVariable Long eventId,
            @RequestBody EventRequest.EventUpdateRequest request);

    @DeleteMapping("/{eventId}")
    CustomResponse<String> deleteEvent(@PathVariable Long eventId);

    @PatchMapping("/{eventId}")
    CustomResponse<EventResponse.EventCompleteResponse> completeEvent(@PathVariable Long eventId);
}
