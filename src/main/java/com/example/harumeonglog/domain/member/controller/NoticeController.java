package com.example.harumeonglog.domain.member.controller;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.service.NoticeCommandService;
import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.domain.member.dto.response.NoticeResponse;
import com.example.harumeonglog.domain.member.service.NoticeQueryService;
import com.example.harumeonglog.global.security.annotation.AuthenticatedMember;
import com.example.harumeonglog.global.validation.annotation.CheckCursorValidation;
import com.example.harumeonglog.global.validation.annotation.CheckSizeValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1")
@Tag(name = "Notice", description = "Notice 관련 Controller")
public class NoticeController {

    private final NoticeQueryService noticeQueryService;
    private final NoticeCommandService noticeCommandService;

    @Operation(summary = "notice 목록 조회 API by 김준환", description = "notice 목록을 무한스크롤로 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    @GetMapping("/notices")
    public CustomResponse<NoticeResponse.NoticeListResponse> getNotices(
            @AuthenticatedMember Member member,
            @RequestParam(name = "cursor") @CheckCursorValidation Long cursor,
            @RequestParam(name = "size") @CheckSizeValidation Integer size
    ) {
        NoticeResponse.NoticeListResponse noticeListResponse = noticeQueryService.getNotices(member, size, cursor);
        return CustomResponse.ok(noticeListResponse);
    }

    @Operation(summary = "notice 삭제 API by 김준환", description = "notice를 삭제합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "NOTICE404", description = "알림을 찾지 못했습니다.")
    })
    @DeleteMapping("/notices/{noticeId}")
    public CustomResponse<Void> deleteNotice(@PathVariable Long noticeId) {
        noticeCommandService.deleteNotice(noticeId);
        return CustomResponse.ok(null);
    }

}
