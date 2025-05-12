package com.example.harumeonglog.domain.member.controller;

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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
@Tag(name = "Member", description = "Member 관련 API")
public class MemberController {

    private final MemberCommandService memberCommandService;
    private final SettingCommandService settingCommandService;
    private final SettingQueryService settingQueryService;

    @Operation(summary = "사용자 정보 API by 서정모", description = "사용자 정보 가져오는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    @GetMapping("/info")
    public CustomResponse<MemberResponse.MemberInfoResponse> getInfo(@AuthenticatedMember Member member) {
        return CustomResponse.ok(MemberConverter.toMemberInfoResponse(member));
    }

    @Operation(summary = "환경 설정 정보 API by 서정모", description = "환경 설정 정보 가져오는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    @GetMapping("/setting")
    public CustomResponse<SettingResponse.SettingInfoResponse> getSettingInfo(@AuthenticatedMember Member member) {
        return CustomResponse.ok(settingQueryService.getSetting(member));
    }

    @Operation(summary = "사용자 삭제 API by 서정모", description = "사용자를 soft-delete하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    @DeleteMapping
    public CustomResponse<Void> deleteMember(@AuthenticatedMember Member member) {
        memberCommandService.softDeleteMember(member);
        return CustomResponse.ok(null);
    }

    @Operation(summary = "사용자 정보 수정 API by 서정모", description = "사용자 정보 수정하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    @PatchMapping("/info")
    public CustomResponse<MemberResponse.MemberInfoUpdateResponse> updateInfo(@AuthenticatedMember Member member, @RequestBody MemberRequest.MemberInfoUpdateRequest request) {
        MemberResponse.MemberInfoUpdateResponse response = memberCommandService.updateInfo(member, request);
        return CustomResponse.ok(response);
    }

    @Operation(summary = "환경 설정 수정하는 API by 서정모", description = "환경 설정 수정하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    @PatchMapping("/setting")
    public CustomResponse<SettingResponse.SettingUpdateResponse> updateSetting(@AuthenticatedMember Member member, @RequestBody SettingRequest.SettingUpdateRequest request) {
        SettingResponse.SettingUpdateResponse response = settingCommandService.updateSetting(member, request);
        return CustomResponse.ok(response);
    }

    @Operation(summary = "FCM 저장 API by 김준환", description = "로그인 후 써주시면 됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    @PatchMapping("/fcm-tokens")
    public CustomResponse<Void> saveFCM(
            @AuthenticatedMember Member member,
            @RequestBody MemberRequest.FCMRequest fcmRequest
    ) {
        memberCommandService.saveFCM(member, fcmRequest);
        return CustomResponse.ok(null);
    }
}
