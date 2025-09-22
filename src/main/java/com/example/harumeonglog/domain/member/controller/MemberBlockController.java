package com.example.harumeonglog.domain.member.controller;

import com.example.harumeonglog.domain.member.dto.request.MemberBlockRequest;
import com.example.harumeonglog.domain.member.dto.response.MemberBlockResponse;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.QMember;
import com.example.harumeonglog.domain.member.service.MemberBlockCommandService;
import com.example.harumeonglog.domain.member.service.MemberBlockQueryService;
import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.global.security.annotation.AuthenticatedMember;
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

    @PostMapping
    public CustomResponse<MemberBlockResponse.MemberBlockInfoResponse> blockOrCancelMember(@AuthenticatedMember Member member, @RequestBody MemberBlockRequest.UpdateMemberBlockRequest request) {
        return CustomResponse.ok(memberBlockCommandService.updateBlock(member, request));
    }

    @GetMapping("/{reportedId}")
    public CustomResponse<MemberBlockResponse.MemberBlockInfoResponse> isBlock(@AuthenticatedMember Member member, @PathVariable Long reportedId) {
        return CustomResponse.ok(memberBlockQueryService.isBlock(member.getId(), reportedId));
    }
}
