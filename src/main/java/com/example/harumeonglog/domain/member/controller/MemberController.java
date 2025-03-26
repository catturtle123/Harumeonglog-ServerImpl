package com.example.harumeonglog.domain.member.controller;

import com.example.harumeonglog.domain.common.controller.response.CustomResponse;
import com.example.harumeonglog.domain.member.controller.dto.request.MemberRequest;
import com.example.harumeonglog.domain.member.controller.dto.request.SettingRequest;
import com.example.harumeonglog.domain.member.controller.dto.response.MemberResponse;
import com.example.harumeonglog.domain.member.controller.dto.response.SettingResponse;
import com.example.harumeonglog.domain.member.controller.port.MemberService;
import com.example.harumeonglog.domain.member.controller.port.SettingService;
import com.example.harumeonglog.domain.member.domain.Member;
import com.example.harumeonglog.domain.member.domain.Setting;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final SettingService settingService;

    @PostMapping("/login")
    public CustomResponse<MemberResponse.MemberLoginResponse> login(@RequestBody MemberRequest.MemberLoginRequest request) {
        MemberResponse.MemberLoginResponse response = memberService.login(request);
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
        return CustomResponse.ok(MemberResponse.MemberInfoResponse.from(member));
    }

    @PatchMapping("/info")
    public CustomResponse<MemberResponse.MemberInfoUpdateResponse> updateInfo(MemberRequest.MemberInfoUpdateRequest request) {
        // TODO: Annotation으로 변경 필요
        Member member = Member.builder().build();
        Member updateMember = memberService.updateInfo(member, request);
        return CustomResponse.ok(MemberResponse.MemberInfoUpdateResponse.from(updateMember));
    }

    @PatchMapping("/setting")
    public CustomResponse<SettingResponse.SettingUpdateResponse> updateSetting(@RequestBody SettingRequest.SettingUpdateRequest request) {
        // TODO: Annotation으로 변경 필요
        Member member = Member.builder().build();
        Setting setting = settingService.updateSetting(member, request);
        return CustomResponse.ok(SettingResponse.SettingUpdateResponse.from(setting));
    }
}
