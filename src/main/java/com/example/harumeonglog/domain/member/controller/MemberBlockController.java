package com.example.harumeonglog.domain.member.controller;

import com.example.harumeonglog.domain.member.dto.request.MemberBlockRequest;
import com.example.harumeonglog.domain.member.dto.response.MemberBlockResponse;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.service.MemberBlockCommandService;
import com.example.harumeonglog.domain.member.service.MemberBlockQueryService;
import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.global.security.annotation.AuthenticatedMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member-block")
@Tag(name = "Member Block", description = "사용자 차단 관련 API")
public class MemberBlockController {

    private final MemberBlockCommandService memberBlockCommandService;
    private final MemberBlockQueryService memberBlockQueryService;
    @Operation(summary = "사용자 차단 혹은 해제 API by 서정모", description = "사용자를 차단하거나 차단한 사용자의 차단을 푸는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "MEMBER404", description = "차단한 사람을 찾지 못한 경우")
    })
    @PostMapping
    public CustomResponse<MemberBlockResponse.MemberBlockInfoResponse> blockOrCancelMember(@AuthenticatedMember Member member, @RequestBody MemberBlockRequest.UpdateMemberBlockRequest request) {
        return CustomResponse.ok(memberBlockCommandService.updateBlock(member, request));
    }

    @Operation(summary = "차단 여부 조회 API by 서정모", description = "차단 여부 가져오는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    @GetMapping("/{reportedId}")
    public CustomResponse<MemberBlockResponse.MemberBlockInfoResponse> isBlock(@AuthenticatedMember Member member, @PathVariable Long reportedId) {
        return CustomResponse.ok(memberBlockQueryService.isBlock(member.getId(), reportedId));
    }
}
