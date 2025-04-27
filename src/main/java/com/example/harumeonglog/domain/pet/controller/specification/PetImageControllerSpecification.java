package com.example.harumeonglog.domain.pet.controller.specification;


import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.dto.request.PetImageRequest;
import com.example.harumeonglog.domain.pet.dto.response.PetImageResponse;
import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.global.security.annotation.AuthenticatedMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
@Tag(name = "PetImage", description = "Pet 이미지 관련 API")
public interface PetImageControllerSpecification {

    @Operation(summary = "펫 이미지 저장 API by 백종우", description = "이미 업로드된 이미지 key들을 저장합니다. 메인 이미지 저장 이외의 이미지를 저장합니다.")
    @ApiResponse(responseCode = "COMMON201", description = "저장 성공")
    @PostMapping("/images")
    CustomResponse<PetImageResponse.AddImagesResponse> addImages(
            @RequestBody PetImageRequest.AddImageRequest request
    );

    @Operation(summary = "펫 이미지 목록 조회 API by 백종우", description = "펫 이미지 목록을 커서 기반으로 조회합니다.")
    @ApiResponse(responseCode = "COMMON200", description = "조회 성공")
    @GetMapping("/{petId}/images")
    CustomResponse<PetImageResponse.GetImagesResponse> getImages(
            @PathVariable Long petId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticatedMember Member member
    );

    // 최근 이미지 조회는 주석 처리 유지
//    @Operation(summary = "최근 등록된 이미지 조회 API", description = "최근 등록된 펫 이미지 목록을 조회합니다.")
//    @ApiResponse(responseCode = "COMMON200", description = "조회 성공")
//    @GetMapping("/recent")
//    CustomResponse<PetImageResponse.RecentImagesResponse> recentImages();

    @Operation(summary = "단일 이미지 조회 API by 백종우", description = "특정 이미지 ID에 해당하는 펫 이미지를 조회합니다.")
    @ApiResponse(responseCode = "COMMON200", description = "조회 성공")
    @GetMapping("/images/{imageId}")
    CustomResponse<PetImageResponse.GetImageResponse> getImage(
            @PathVariable Long imageId,
            @AuthenticatedMember Member member
    );

    @Operation(summary = "단일 이미지 삭제 API by 백종우", description = "특정 이미지 ID의 펫 이미지를 삭제합니다.")
    @ApiResponse(responseCode = "COMMON200", description = "삭제 성공")
    @DeleteMapping("/images/{imageId}")
    CustomResponse<String> deleteImage(
            @PathVariable Long imageId,
            @AuthenticatedMember Member member
    );

    @Operation(summary = "다중 이미지 삭제 API by 백종우", description = "펫 ID와 이미지 ID 목록을 통해 여러 이미지를 삭제합니다.")
    @ApiResponse(responseCode = "COMMON200", description = "삭제 성공")
    @DeleteMapping("/{petId}/images")
    CustomResponse<String> deleteImages(
            @PathVariable Long petId,
            @RequestBody PetImageRequest.DeleteImagesRequest request,
            @AuthenticatedMember Member member
    );
}
