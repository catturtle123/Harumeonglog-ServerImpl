package com.example.harumeonglog.domain.pet.controller;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.dto.request.PetImageRequest;
import com.example.harumeonglog.domain.pet.dto.request.PetImageRequest.DeleteImagesRequest;
import com.example.harumeonglog.domain.pet.dto.response.PetImageResponse;
import com.example.harumeonglog.domain.pet.service.command.PetImageCommandService;
import com.example.harumeonglog.domain.pet.service.query.PetImageQueryService;
import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.global.security.annotation.AuthenticatedMember;
import com.example.harumeonglog.global.validation.annotation.CheckCursorValidation;
import com.example.harumeonglog.global.validation.annotation.CheckSizeValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/pets")
@Tag(name = "PetImage", description = "Pet 이미지 관련 API")
public class PetImageController {

    private final PetImageCommandService petImageCommandService;
    private final PetImageQueryService petImageQueryService;

    @Operation(summary = "펫 이미지 저장 API by 백종우", description = "이미 업로드된 이미지 key들을 저장합니다. 메인 이미지 저장 이외의 이미지를 저장합니다.")
    @ApiResponse(responseCode = "COMMON201", description = "저장 성공")
    @PostMapping("/images")
    public CustomResponse<PetImageResponse.AddImagesResponse> addImages(
            @RequestBody PetImageRequest.AddImageRequest request){
        return CustomResponse.created(petImageCommandService.addImage(request));
    }

    @Operation(summary = "펫 이미지 목록 조회 API by 백종우", description = "펫 이미지 목록을 커서 기반으로 조회합니다.")
    @ApiResponse(responseCode = "COMMON200", description = "조회 성공")
    @GetMapping("/{petId}/images")
    public CustomResponse<PetImageResponse.GetImagesResponse> getImages(
            @PathVariable Long petId,
            @RequestParam(required = false) @CheckCursorValidation Long cursor,
            @RequestParam(defaultValue = "10") @CheckSizeValidation Integer size,
            @AuthenticatedMember Member member) {
        return CustomResponse.ok(petImageQueryService.getImages(petId, member, cursor, size));
    }

//    @GetMapping("/recent")
//    public CustomResponse<PetImageResponse.RecentImagesResponse> recentImages() {
//        return CustomResponse.ok(petImageQueryService.recentImages());
//    }

    @Operation(summary = "단일 이미지 조회 API by 백종우", description = "특정 이미지 ID에 해당하는 펫 이미지를 조회합니다.")
    @ApiResponse(responseCode = "COMMON200", description = "조회 성공")
    @GetMapping("/images/{imageId}")
    public CustomResponse<PetImageResponse.GetImageResponse> getImage(
            @PathVariable Long imageId,
            @AuthenticatedMember Member member) {
        return CustomResponse.ok(petImageQueryService.getImage(imageId, member));
    }

    @Operation(summary = "단일 이미지 삭제 API by 백종우", description = "특정 이미지 ID의 펫 이미지를 삭제합니다.")
    @ApiResponse(responseCode = "COMMON200", description = "삭제 성공")
    @DeleteMapping("/images/{imageId}")
    public CustomResponse<String> deleteImage(
            @PathVariable Long imageId,
            @AuthenticatedMember Member member) {
        petImageCommandService.deleteImage(imageId, member);
        return CustomResponse.ok("이미지 삭제 완료");
    }

    @Operation(summary = "다중 이미지 삭제 API by 백종우", description = "펫 ID와 이미지 ID 목록을 통해 여러 이미지를 삭제합니다.")
    @ApiResponse(responseCode = "COMMON200", description = "삭제 성공")
    @DeleteMapping("/{petId}/images")
    public CustomResponse<String> deleteImages(
            @PathVariable Long petId,
            @RequestBody DeleteImagesRequest request,
            @AuthenticatedMember Member member) {
        petImageCommandService.deleteImages(petId, request, member);
        return CustomResponse.ok("이미지 삭제 완료");
    }

}