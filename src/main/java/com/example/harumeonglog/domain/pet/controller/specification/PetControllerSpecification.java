package com.example.harumeonglog.domain.pet.controller.specification;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.dto.request.PetRequest;
import com.example.harumeonglog.domain.pet.dto.response.PetResponse;
import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.global.security.annotation.AuthenticatedMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Pet", description = "Pet 관련 API")
public interface PetControllerSpecification {

    @Operation(summary = "펫 추가 API by 백종우", description = "펫을 등록합니다. 등록하기 전에 사진을 등록해주세요. 메인 이미지는 해당 API에서 처리합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON201", description = "펫 등록 성공")
    })
    @PostMapping
    CustomResponse<PetResponse.AddPetResponse> addPet(
                                                                      @RequestBody PetRequest.AddPetRequest request,
                                                                      @AuthenticatedMember Member member);

    @Operation(summary = "펫 정보 수정 API by 백종우", description = "펫 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "수정 성공")
    })
    @PatchMapping("/{petId}")
    CustomResponse<PetResponse.ChangePetInfoResponse> updatePetInfo(
            @PathVariable Long petId,
            @RequestBody PetRequest.ChangePetInfoRequest request,
            @AuthenticatedMember Member member
    );

    @Operation(summary = "펫 목록 조회 API by 백종우", description = "자신이 속한 펫 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "조회 성공")
    })
    @GetMapping
    CustomResponse<PetResponse.GetPetsResponse> getPets(
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticatedMember Member member
    );

    @Operation(summary = "현재 펫 변경 시 보유 펫 조회 API by 백종우",description = "대표 펫 변경 시 보유 펫 조회")
    @GetMapping("/active")
    CustomResponse<PetResponse.PetListPreviewResponse> getActivePets(
            @RequestParam(required = false) Long cursor, // 커서 (마지막 펫 ID)
            @RequestParam(defaultValue = "10") int size,  // 페이지 크기
            @AuthenticatedMember Member member) ;

    @Operation(summary = "현재 펫 변경 API by 백종우", description = "대표 펫을 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "변경 성공")
    })
    @PatchMapping("/{petId}/status/active")
    CustomResponse<String> updateCurrentPet(
            @PathVariable Long petId,
            @AuthenticatedMember Member member
    );

    @Operation(summary = "펫 삭제 API by 백종우", description = "펫을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "삭제 성공")
    })
    @PatchMapping("/{petId}")
    CustomResponse<String> deletePet(@PathVariable Long petId,
                                     @AuthenticatedMember Member member);

    @Operation(summary = "펫 초대 API by 백종우", description = "다른 사용자를 펫에 초대합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "초대 성공")
    })
    @PostMapping("/{petId}/members")
    CustomResponse<String> invite(
            @PathVariable Long petId,
            @RequestBody PetRequest.InviteListRequest request,
            @AuthenticatedMember Member member
    );

    @Operation(summary = "멤버 검색 API by 백종우", description = "이메일로 사용자를 검색합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "검색 성공")
    })
    @GetMapping("/members")
    CustomResponse<PetResponse.SearchMemberResponse> searchMember(
            @RequestParam String email,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticatedMember Member member
    );

    @Operation(summary = "현재 펫 정보 조회 API by 백종우", description = "현재 선택된 펫 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "검색 성공")
    })
    @GetMapping("/active/primary")
    CustomResponse<PetResponse.MainPetResponse> getCurrentPet(@AuthenticatedMember Member member);
}
