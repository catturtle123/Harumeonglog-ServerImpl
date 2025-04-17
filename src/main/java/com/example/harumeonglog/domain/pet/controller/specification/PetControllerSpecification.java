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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Pet", description = "Pet 관련 API")
public interface PetControllerSpecification {

    @Operation(summary = "펫 추가 API by 백종우", description = "펫을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON201", description = "펫 등록 성공")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CustomResponse<PetResponse.AddPetResponse>> addPet(@RequestPart("mainImage") MultipartFile mainImage,
                                                                      @RequestPart PetRequest.AddPetRequest request,
                                                                      @AuthenticatedMember Member member);

    @Operation(summary = "펫 정보 수정 API by 백종우", description = "펫 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "수정 성공")
    })
    @PutMapping("/{petId}")
    CustomResponse<PetResponse.ChangePetInfoResponse> changePetInfo(
            @PathVariable Long petId,
            @RequestBody PetRequest.ChangePetInfoRequest request
    );

    @Operation(summary = "펫 목록 조회 API by 백종우", description = "펫 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "조회 성공")
    })
    @GetMapping
    CustomResponse<PetResponse.GetPetsResponse> getPets(
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size
    );

    @Operation(summary = "대표 펫 변경 API by 백종우", description = "대표 펫을 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "변경 성공")
    })
    @PatchMapping("/current")
    CustomResponse<PetResponse.ChangeCurrentPetResponse> changeCurrentPet(
            @RequestBody PetRequest.ChangeCurrentPetRequest request
    );

    @Operation(summary = "펫 삭제 API by 백종우", description = "펫을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "삭제 성공")
    })
    @PatchMapping("/{petId}")
    CustomResponse<String> deletePet(@PathVariable Long petId);

    @Operation(summary = "펫 초대 API by 백종우", description = "다른 사용자를 펫에 초대합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "초대 성공")
    })
    @PostMapping("/{petId}/invite")
    CustomResponse<String> invite(
            @PathVariable Long petId,
            @RequestBody PetRequest.InviteRequest request
    );

    @Operation(summary = "멤버 검색 API by 백종우", description = "이메일로 사용자를 검색합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "검색 성공")
    })
    @GetMapping("/search-member")
    CustomResponse<PetResponse.SearchMemberResponse> searchMember(
            @RequestParam String email,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size
    );
}
