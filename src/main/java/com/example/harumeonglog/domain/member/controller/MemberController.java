package com.example.harumeonglog.domain.member.controller;

import com.example.harumeonglog.domain.member.controller.specification.MemberControllerSpecification;
import com.example.harumeonglog.domain.member.converter.MemberConverter;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.Setting;
import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.domain.member.dto.request.MemberRequest;
import com.example.harumeonglog.domain.member.dto.request.SettingRequest;
import com.example.harumeonglog.domain.member.dto.response.MemberResponse;
import com.example.harumeonglog.domain.member.dto.response.SettingResponse;
import com.example.harumeonglog.domain.member.service.MemberService;
import com.example.harumeonglog.domain.member.service.SettingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
@Tag(name = "Member", description = "Member 관련 API")
public class MemberController implements MemberControllerSpecification {

    private final MemberService memberService;
    private final SettingService settingService;

    @PostMapping("/{provider}/login")
    public CustomResponse<MemberResponse.MemberLoginResponse> login(@PathVariable String provider, @RequestBody MemberRequest.MemberLoginRequest request) {
        MemberResponse.MemberLoginResponse response = memberService.login(provider, request);
        return CustomResponse.ok(response);
    }

    @PostMapping("/logout")
    public CustomResponse<MemberResponse.MemberLogoutResponse> logout() {
        // TODO: Annotation으로 변경 필요
        Member member = Member.builder().build();
        MemberResponse.MemberLogoutResponse response = memberService.logout(member);
        return CustomResponse.ok(response);
    }

    @GetMapping("/info")
    public CustomResponse<MemberResponse.MemberInfoResponse> getInfo() {
        // TODO: Annotation으로 변경 필요
        Member member = Member.builder().build();
        return CustomResponse.ok(MemberConverter.toMemberInfoResponse(member));
    }

    @PatchMapping("/info")
    public CustomResponse<MemberResponse.MemberInfoUpdateResponse> updateInfo(MemberRequest.MemberInfoUpdateRequest request) {
        // TODO: Annotation으로 변경 필요
        Member member = Member.builder().build();
        Member updateMember = memberService.updateInfo(member, request);
        return CustomResponse.ok(MemberConverter.toMemberInfoUpdateResponse(updateMember));
    }

    @PatchMapping("/setting")
    public CustomResponse<SettingResponse.SettingUpdateResponse> updateSetting(@RequestBody SettingRequest.SettingUpdateRequest request) {
        // TODO: Annotation으로 변경 필요
        Member member = Member.builder().build();
        Setting setting = settingService.updateSetting(member, request);
        return CustomResponse.ok(SettingResponse.SettingUpdateResponse.from(setting));
    }
}
