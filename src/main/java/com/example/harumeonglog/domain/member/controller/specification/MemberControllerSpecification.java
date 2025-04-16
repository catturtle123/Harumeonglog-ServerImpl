package com.example.harumeonglog.domain.member.controller.specification;

import com.example.harumeonglog.domain.member.dto.request.MemberRequest;
import com.example.harumeonglog.domain.member.dto.response.MemberResponse;
import com.example.harumeonglog.global.common.response.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface MemberControllerSpecification {
    @Operation(summary = "로그인 API by 서정모", description = "소셜 로그인")
    @Parameter(name = "provider", description = "소셜 로그인 종류 (KAKAO, APPLE), APPLE은 현재 미완성 상태 요청 X")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "AUTH400", description = "토큰이 유효하지 않습니다.")
    })
    CustomResponse<MemberResponse.MemberLoginResponse> login(String provider, MemberRequest.MemberLoginRequest request) ;
}
