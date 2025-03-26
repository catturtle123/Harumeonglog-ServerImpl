package com.example.harumeonglog.domain.pet.controller;

import com.example.harumeonglog.domain.common.controller.response.CustomResponse;
import com.example.harumeonglog.domain.pet.controller.dto.request.PetImageRequest.AddImagesRequest;
import com.example.harumeonglog.domain.pet.controller.dto.request.PetImageRequest.DeleteImagesRequest;
import com.example.harumeonglog.domain.pet.controller.dto.response.PetImageResponse.*;
import com.example.harumeonglog.domain.pet.controller.port.PetImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pet-images")
public class PetImageController {

    private final PetImageService petImageService;

    @PostMapping("/{petId}")
    public ResponseEntity<CustomResponse<AddImagesResponse>> addImages(
            @PathVariable Long petId, @RequestBody AddImagesRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CustomResponse.created(petImageService.addImages(petId, request)));
    }

    @GetMapping("/{petId}")
    public CustomResponse<GetImagesResponse> getImages(
            @PathVariable Long petId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size) {
        return CustomResponse.ok(petImageService.getImages(petId, cursor, size));
    }

    @GetMapping("/recent")
    public CustomResponse<RecentImagesResponse> recentImages() {
        return CustomResponse.ok(petImageService.recentImages());
    }

    @GetMapping("/image/{imageId}")
    public CustomResponse<GetImageResponse> getImage(@PathVariable Long imageId) {
        return CustomResponse.ok(petImageService.getImage(imageId));
    }

    @DeleteMapping("/image/{imageId}")
    public CustomResponse<String> deleteImage(@PathVariable Long imageId) {
        return CustomResponse.ok("이미지 삭제 완료");
    }

    @DeleteMapping("/{petId}")
    public CustomResponse<String> deleteImages(
            @PathVariable Long petId, @RequestBody DeleteImagesRequest request) {
        return CustomResponse.ok("이미지 삭제 완료");
    }
}