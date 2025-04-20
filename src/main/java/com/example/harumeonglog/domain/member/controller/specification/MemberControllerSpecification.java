package com.example.harumeonglog.domain.member.controller.specification;

import com.example.harumeonglog.domain.member.converter.MemberConverter;
import com.example.harumeonglog.domain.member.converter.SettingConverter;
import com.example.harumeonglog.domain.member.dto.request.MemberRequest;
import com.example.harumeonglog.domain.member.dto.request.SettingRequest;
import com.example.harumeonglog.domain.member.dto.response.MemberResponse;
import com.example.harumeonglog.domain.member.dto.response.SettingResponse;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.Setting;
import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.global.security.annotation.AuthenticatedMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface MemberControllerSpecification {

    @Operation(summary = "사용자 정보 API by 서정모", description = "사용자 정보 가져오는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    CustomResponse<MemberResponse.MemberInfoResponse> getInfo(Member member);

    @Operation(summary = "환경 설정 정보 API by 서정모", description = "환경 설정 정보 가져오는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    CustomResponse<SettingResponse.SettingInfoResponse> getSettingInfo(Member member);

    @Operation(summary = "사용자 삭제 API by 서정모", description = "사용자를 soft-delete하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    CustomResponse<Void> deleteMember(Member member);

    @Operation(summary = "사용자 정보 수정 API by 서정모", description = "사용자 정보 수정하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    CustomResponse<MemberResponse.MemberInfoUpdateResponse> updateInfo(Member member, MemberRequest.MemberInfoUpdateRequest request);

    @Operation(summary = "환경 설정 수정하는 API by 서정모", description = "환경 설정 수정하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    CustomResponse<SettingResponse.SettingUpdateResponse> updateSetting(Member member, SettingRequest.SettingUpdateRequest request);
}
