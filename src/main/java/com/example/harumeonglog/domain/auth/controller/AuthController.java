package com.example.harumeonglog.domain.auth.controller;

import com.example.harumeonglog.domain.auth.dto.request.AuthRequest;
import com.example.harumeonglog.domain.auth.dto.response.AuthResponse;
import com.example.harumeonglog.domain.auth.service.AuthCommandService;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.global.security.annotation.AuthenticatedMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController {

    private final AuthCommandService authCommandService;

    @Operation(summary = "로그인 API by 서정모", description = "소셜 로그인")
    @Parameter(name = "provider", description = "소셜 로그인 종류 (KAKAO, APPLE), APPLE은 현재 미완성 상태 요청 X")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "AUTH400", description = "토큰이 유효하지 않습니다.")
    })
    @PostMapping("/{provider}/login")
    public CustomResponse<AuthResponse.AuthLoginResponse> login(@PathVariable String provider, @RequestBody AuthRequest.AuthLoginRequest request) {
        AuthResponse.AuthLoginResponse response = authCommandService.login(provider, request);
        return CustomResponse.ok(response);
    }


    @Operation(summary = "로그아웃 API by 서정모", description = "로그아웃")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "로그아웃에 성공했습니다.")
    })
    @PostMapping("/logout")
    public CustomResponse<String> logout(@AuthenticatedMember Member member) {
        String response = authCommandService.logout(member);
        return CustomResponse.ok(response);
    }


    @Operation(summary = "Access Token 재발급 API by 서정모", description = "Access Token 재발급 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    @PostMapping("/reissue")
    public CustomResponse<AuthResponse.AuthAccessReissueResponse> reissueAccessToken(@RequestBody AuthRequest.AuthAccessReissueRequest request) {
        AuthResponse.AuthAccessReissueResponse response = authCommandService.reissueAccess(request);
        return CustomResponse.ok(response);
    }
}
