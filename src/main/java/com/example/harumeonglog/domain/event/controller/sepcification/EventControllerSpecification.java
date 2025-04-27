package com.example.harumeonglog.domain.event.controller.sepcification;

import com.example.harumeonglog.domain.event.dto.request.EventRequest;
import com.example.harumeonglog.domain.event.dto.response.EventResponse;
import com.example.harumeonglog.domain.event.entity.enums.EventCategory;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.global.security.annotation.AuthenticatedMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "Event API", description = "일정 관련 API 입니다.")
public interface EventControllerSpecification {

    @Operation(summary = "일정 추가 API by 백종우",
            description = "일정을 등록합니다. 카테고리별 필수 필드가 다르며, 반복 설정이 가능합니다. 카테고리(BATH, HOSPITAL, WALK, MEDICINE, GENERAL, " +
                    "공통필드는 category까지 이며, 각각 WALK(distance, duration, details), HOSPITAL(hospitalName, department, cost, details), MEDICINE(medicineName, details), GENERAL(details)필드가 필요합니다.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "일정 생성 요청 데이터",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = EventRequest.EventRequestDTO.class),
                    examples = @ExampleObject(
                            name = "진료 일정 예시",
                            value = """
                    {
                        "title": "정기 검진",
                        "date": "2025-04-26",
                        "isRepeated": true,
                        "expiredDate": "2025-12-31",
                        "repeatDays": ["MON", "WED"],
                        "hasNotice": true,
                        "time": "14:30:00",
                        "category": "HOSPITAL",
                        "hospitalName": "서울 동물병원",
                        "department": "내과",
                        "cost": 50000,
                        "details": "혈액 검사 예정"
                    }
                    """))))
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON201", description = "일정 등록 성공"),
            @ApiResponse(responseCode = "EVENT400", description = "올바르지 않은 카테고리입니다."),
            @ApiResponse(responseCode = "PET404", description = "반려견을 찾지 못했습니다."),
            @ApiResponse(responseCode = "MEMBERPET400", description = "해당 반려견 그룹에 속하지 않았습니다."),
            @ApiResponse(responseCode = "PETROLE400", description = "존재하지 않는 권한입니다."),
            @ApiResponse(responseCode = "AUTH401", description = "인증되지 않은 사용자")
    })
    @PostMapping
    CustomResponse<EventResponse.EventCreateResponse> createEvent(
            @RequestBody EventRequest.EventRequestDTO request,
            @AuthenticatedMember Member member);


    @Operation(summary = "날짜별 일정 조회 API by 백종우", description = "특정 날짜의 일정들을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "일정 조회 성공"),
            @ApiResponse(responseCode = "MEMBERPET400", description = "해당 반려견 그룹에 속하지 않았습니다."),
            @ApiResponse(responseCode = "PETROLE400", description = "존재하지 않는 권한입니다."),
            @ApiResponse(responseCode = "AUTH401", description = "인증되지 않은 사용자")
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
            @ApiResponse(responseCode = "COMMON200", description = "일정 조회 성공"),
            @ApiResponse(responseCode = "EVENT404", description = "일정을 찾지 못했습니다.")
    })
    @GetMapping("/{eventId}")
    CustomResponse<EventResponse.BaseEventResponse> getEvent(@PathVariable Long eventId);



    @Operation(summary = "한달에 일정 있는 날 조회 API by 백종우", description = "연, 월을 입력하여 해당 월에 일정이 있는 날을 반환합니다. ")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "조회 성공"),
            @ApiResponse(responseCode = "PET404", description = "반려견을 찾지 못했습니다."),
            @ApiResponse(responseCode = "MEMBERPET400", description = "해당 반려견 그룹에 속하지 않았습니다."),
            @ApiResponse(responseCode = "PETROLE400", description = "존재하지 않는 권한입니다."),
            @ApiResponse(responseCode = "AUTH401", description = "인증되지 않은 사용자")
    })
    @GetMapping("/monthly-dates")
    CustomResponse<EventResponse.EventDatesResponse> getEventDates(
            @AuthenticatedMember Member member,
            @RequestParam @Min(2000) @Max(9999) Integer year,
            @RequestParam @Min(1) @Max(12) Integer month);



    @Operation(summary = "일정 수정 API by 백종우", description = "특정 id의 일정을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "일정 수정 성공"),
            @ApiResponse(responseCode = "EVENT400", description = "올바르지 않은 카테고리입니다."),
            @ApiResponse(responseCode = "EVENT404", description = "일정을 찾지 못했습니다."),
            @ApiResponse(responseCode = "PET404", description = "반려견을 찾지 못했습니다."),
            @ApiResponse(responseCode = "MEMBERPET400", description = "해당 반려견 그룹에 속하지 않았습니다."),
            @ApiResponse(responseCode = "PETROLE400", description = "존재하지 않는 권한입니다."),
            @ApiResponse(responseCode = "AUTH401", description = "인증되지 않은 사용자")
    })
    @PutMapping("/{eventId}")
    CustomResponse<EventResponse.BaseEventResponse> updateEvent(
            @AuthenticatedMember Member member,
            @PathVariable Long eventId,
            @RequestBody EventRequest.EventRequestDTO request);


    @Operation(summary = "일정 삭제 API by 백종우", description = "특정 id의 일정을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "일정 삭제 성공"),
            @ApiResponse(responseCode = "EVENT404", description = "일정을 찾지 못했습니다."),
            @ApiResponse(responseCode = "MEMBERPET400", description = "해당 반려견 그룹에 속하지 않았습니다."),
            @ApiResponse(responseCode = "PETROLE400", description = "존재하지 않는 권한입니다."),
            @ApiResponse(responseCode = "AUTH401", description = "인증되지 않은 사용자")
    })
    @DeleteMapping("/{eventId}")
    CustomResponse<String> deleteEvent(@PathVariable Long eventId,
                                       @AuthenticatedMember Member member);

    @Operation(summary = "일정 체크 API by 백종우", description = "특정 id의 일정을 완료/미완료 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "일정 체크 성공"),
            @ApiResponse(responseCode = "EVENT404", description = "일정을 찾지 못했습니다."),
            @ApiResponse(responseCode = "MEMBERPET400", description = "해당 반려견 그룹에 속하지 않았습니다."),
            @ApiResponse(responseCode = "PETROLE400", description = "존재하지 않는 권한입니다."),
            @ApiResponse(responseCode = "AUTH401", description = "인증되지 않은 사용자")
    })
    @PatchMapping("/{eventId}")
    CustomResponse<EventResponse.EventPreviewResponse> completeEvent(@PathVariable Long eventId,
                                                                      @AuthenticatedMember Member member);
}
