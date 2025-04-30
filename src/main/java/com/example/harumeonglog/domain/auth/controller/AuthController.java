package com.example.harumeonglog.domain.auth.controller;

import com.example.harumeonglog.domain.auth.controller.specification.AuthControllerSpecification;
import com.example.harumeonglog.domain.auth.dto.request.AuthRequest;
import com.example.harumeonglog.domain.auth.dto.response.AuthResponse;
import com.example.harumeonglog.domain.auth.service.AuthCommandService;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.global.security.annotation.AuthenticatedMember;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController implements AuthControllerSpecification {

    private final AuthCommandService authCommandService;

    @PostMapping("/{provider}/login")
    public CustomResponse<AuthResponse.AuthLoginResponse> login(@PathVariable String provider, @RequestBody AuthRequest.AuthLoginRequest request) {
        AuthResponse.AuthLoginResponse response = authCommandService.login(provider, request);
        return CustomResponse.ok(response);
    }


    @PostMapping("/logout")
    public CustomResponse<String> logout(@AuthenticatedMember Member member) {
        String response = authCommandService.logout(member);
        return CustomResponse.ok(response);
    }

    @PostMapping("/reissue")
    public CustomResponse<AuthResponse.AuthAccessReissueResponse> reissueAccessToken(@RequestBody AuthRequest.AuthAccessReissueRequest request) {
        AuthResponse.AuthAccessReissueResponse response = authCommandService.reissueAccess(request);
        return CustomResponse.ok(response);
    }
}
