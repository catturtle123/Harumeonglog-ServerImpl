package com.example.harumeonglog.domain.pet.controller;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.controller.specification.PetImageControllerSpecification;
import com.example.harumeonglog.domain.pet.dto.request.PetImageRequest;
import com.example.harumeonglog.domain.pet.dto.request.PetImageRequest.DeleteImagesRequest;
import com.example.harumeonglog.domain.pet.dto.response.PetImageResponse;
import com.example.harumeonglog.domain.pet.service.command.PetImageCommandService;
import com.example.harumeonglog.domain.pet.service.query.PetImageQueryService;
import com.example.harumeonglog.global.common.response.CustomResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pets")
@Tag(name = "PetImage", description = "Pet 이미지 관련 API")
public class PetImageController implements PetImageControllerSpecification {

    private final PetImageCommandService petImageCommandService;
    private final PetImageQueryService petImageQueryService;

    @PostMapping("/images")
    public CustomResponse<PetImageResponse.AddImagesResponse> addImages(
            @RequestBody PetImageRequest.AddImageRequest request){
        return CustomResponse.created(petImageCommandService.addImage(request));
    }

    @GetMapping("/{petId}/images")
    public CustomResponse<PetImageResponse.GetImagesResponse> getImages(
            @PathVariable Long petId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal Member member) {
        return CustomResponse.ok(petImageQueryService.getImages(petId, member, cursor, size));
    }

//    @GetMapping("/recent")
//    public CustomResponse<PetImageResponse.RecentImagesResponse> recentImages() {
//        return CustomResponse.ok(petImageQueryService.recentImages());
//    }

    @GetMapping("/images/{imageId}")
    public CustomResponse<PetImageResponse.GetImageResponse> getImage(
            @PathVariable Long imageId,
            @AuthenticationPrincipal Member member) {
        return CustomResponse.ok(petImageQueryService.getImage(imageId, member));
    }

    @DeleteMapping("/images/{imageId}")
    public CustomResponse<String> deleteImage(
            @PathVariable Long imageId,
            @AuthenticationPrincipal Member member) {
        petImageCommandService.deleteImage(imageId, member);
        return CustomResponse.ok("이미지 삭제 완료");
    }

    @DeleteMapping("/{petId}/images")
    public CustomResponse<String> deleteImages(
            @PathVariable Long petId,
            @RequestBody DeleteImagesRequest request,
            @AuthenticationPrincipal Member member) {
        petImageCommandService.deleteImages(petId, request, member);
        return CustomResponse.ok("이미지 삭제 완료");
    }

}