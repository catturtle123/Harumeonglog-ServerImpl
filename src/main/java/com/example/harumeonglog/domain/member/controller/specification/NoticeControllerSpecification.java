package com.example.harumeonglog.domain.member.controller.specification;

import com.example.harumeonglog.domain.member.dto.response.NoticeResponse;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.global.security.annotation.AuthenticatedMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Notice", description = "Notice 관련 Controller")
public interface NoticeControllerSpecification {

    @Operation(summary = "notice 목록 조회 API by 김준환", description = "notice 목록을 무한스크롤로 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    @GetMapping("/notices")
    CustomResponse<NoticeResponse.NoticeListResponse> getNotices(
            @AuthenticatedMember Member member,
            @RequestParam(name = "cursor") Long cursor,
            @RequestParam(name = "size") Integer size
    );

    @Operation(summary = "notice 삭제 API by 김준환", description = "notice를 삭제합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "NOTICE404", description = "알림을 찾지 못했습니다.")
    })
    @DeleteMapping("/notices/{noticeId}")
    CustomResponse<Void> deleteNotice(@PathVariable Long noticeId);
}
