package com.example.harumeonglog.domain.event.controller.sepcification;

import com.example.harumeonglog.domain.event.dto.request.EventRequest;
import com.example.harumeonglog.domain.event.dto.response.EventResponse;
import com.example.harumeonglog.domain.event.entity.enums.EventCategory;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.global.security.annotation.AuthenticatedMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "일정 API", description = "일정 관련 API 입니다.")
public interface EventControllerSpecification {

    @Operation(summary = "일정 추가 API by 백종우", description = "일정을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON201", description = "일정 등록 성공")
    })
    @PostMapping
    CustomResponse<EventResponse.EventCreateResponse> createEvent(
            @RequestBody EventRequest.EventRequestDTO request,
            @AuthenticatedMember Member member);


    @Operation(summary = "날짜별 일정 조회 API by 백종우", description = "특정 날짜의 일정들을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "일정 조회 성공")
    })
    @GetMapping
    CustomResponse<EventResponse.EventDayResponse> getDayEvent(
            @AuthenticatedMember Member member,
            @RequestParam(defaultValue = "2025-04-26") LocalDate date,
            @RequestParam(required = false) EventCategory category,
            @RequestParam Integer size,
            @RequestParam Long cursor
            );


    @Operation(summary = "일정 단일 조회 API by 백종우", description = "특정 id의 일정을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "일정 조회 성공")
    })
    @GetMapping("/{eventId}")
    CustomResponse<EventResponse.BaseEventResponse> getEvent(@PathVariable Long eventId);

    @Operation(summary = "일정 수정 API by 백종우", description = "특정 id의 일정을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "일정 수정 성공")
    })
    @PutMapping("/{eventId}")
    CustomResponse<EventResponse.BaseEventResponse> updateEvent(
            @AuthenticatedMember Member member,
            @PathVariable Long eventId,
            @RequestBody EventRequest.EventRequestDTO request);

    @DeleteMapping("/{eventId}")
    CustomResponse<String> deleteEvent(@PathVariable Long eventId);

    @PatchMapping("/{eventId}")
    CustomResponse<EventResponse.EventCompleteResponse> completeEvent(@PathVariable Long eventId);
}
