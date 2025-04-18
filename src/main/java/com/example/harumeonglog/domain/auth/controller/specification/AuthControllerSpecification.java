package com.example.harumeonglog.domain.auth.controller.specification;

import com.example.harumeonglog.domain.auth.dto.request.AuthRequest;
import com.example.harumeonglog.domain.auth.dto.response.AuthResponse;
import com.example.harumeonglog.global.common.response.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthControllerSpecification {
    @Operation(summary = "로그인 API by 서정모", description = "소셜 로그인")
    @Parameter(name = "provider", description = "소셜 로그인 종류 (KAKAO, APPLE), APPLE은 현재 미완성 상태 요청 X")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "AUTH400", description = "토큰이 유효하지 않습니다.")
    })
    CustomResponse<AuthResponse.AuthLoginResponse> login(String provider, AuthRequest.AuthLoginRequest request);

    @Operation(summary = "로그아웃 API by 서정모", description = "로그아웃")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "로그아웃에 성공했습니다.")
    })
    CustomResponse<AuthResponse.AuthLogoutResponse> logout();

    @Operation(summary = "Access Token 재발급 API by 서정모", description = "Access Token 재발급 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    CustomResponse<AuthResponse.AuthAccessReissueResponse> reissueAccessToken(@RequestBody AuthRequest.AuthAccessReissueRequest request);
}
