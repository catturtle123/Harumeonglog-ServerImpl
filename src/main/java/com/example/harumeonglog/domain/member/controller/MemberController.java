package com.example.harumeonglog.domain.member.controller;

import com.example.harumeonglog.domain.member.controller.specification.MemberControllerSpecification;
import com.example.harumeonglog.domain.member.converter.MemberConverter;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.service.MemberCommandService;
import com.example.harumeonglog.domain.member.service.SettingCommandService;
import com.example.harumeonglog.domain.member.service.SettingQueryService;
import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.domain.member.dto.request.MemberRequest;
import com.example.harumeonglog.domain.member.dto.request.SettingRequest;
import com.example.harumeonglog.domain.member.dto.response.MemberResponse;
import com.example.harumeonglog.domain.member.dto.response.SettingResponse;
import com.example.harumeonglog.global.security.annotation.AuthenticatedMember;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
@Tag(name = "Member", description = "Member 관련 API")
public class MemberController implements MemberControllerSpecification {

    private final MemberCommandService memberCommandService;
    private final SettingCommandService settingCommandService;
    private final SettingQueryService settingQueryService;

    @GetMapping("/info")
    public CustomResponse<MemberResponse.MemberInfoResponse> getInfo(@AuthenticatedMember Member member) {
        return CustomResponse.ok(MemberConverter.toMemberInfoResponse(member));
    }

    @GetMapping("/setting")
    public CustomResponse<SettingResponse.SettingInfoResponse> getSettingInfo(@AuthenticatedMember Member member) {
        return CustomResponse.ok(settingQueryService.getSetting(member));
    }

    @DeleteMapping
    public CustomResponse<Void> deleteMember(@AuthenticatedMember Member member) {
        memberCommandService.softDeleteMember(member);
        return CustomResponse.ok(null);
    }

    @PatchMapping("/info")
    public CustomResponse<MemberResponse.MemberInfoUpdateResponse> updateInfo(@AuthenticatedMember Member member, @RequestBody MemberRequest.MemberInfoUpdateRequest request) {
        MemberResponse.MemberInfoUpdateResponse response = memberCommandService.updateInfo(member, request);
        return CustomResponse.ok(response);
    }

    @PatchMapping("/setting")
    public CustomResponse<SettingResponse.SettingUpdateResponse> updateSetting(@AuthenticatedMember Member member, @RequestBody SettingRequest.SettingUpdateRequest request) {
        SettingResponse.SettingUpdateResponse response = settingCommandService.updateSetting(member, request);
        return CustomResponse.ok(response);
    }

    @PatchMapping("/fcm-tokens")
    public CustomResponse<Void> saveFCM(
            @AuthenticatedMember Member member,
            @RequestBody MemberRequest.FCMRequest fcmRequest
    ) {
        memberCommandService.saveFCM(member, fcmRequest);
        return CustomResponse.ok(null);
    }
}
