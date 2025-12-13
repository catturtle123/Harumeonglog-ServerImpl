package com.example.harumeonglog.global.s3.controller;

import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.global.s3.dto.request.S3RequestDTO;
import com.example.harumeonglog.global.s3.dto.response.S3ResponseDTO;
import com.example.harumeonglog.global.s3.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/s3")
@Tag(name = "S3", description = "S3 이미지 업로드 관련 API")
public class S3Controller {

    private final S3Service s3Service;

    @Operation(summary = "S3 이미지 PresignedUrl 발급 by 백종우",
            description = "단일/복수 엔티티에 대한 단일/복수 이미지의 PresignedUrl을 발급합니다.")
    @ApiResponse(responseCode = "COMMON200", description = "발급성공")
    @PostMapping("/presigned-urls")
    public CustomResponse<S3ResponseDTO.S3ResponseListDTO> getPresignedUrls(
            @RequestBody S3RequestDTO.GeneratePresignedUrlsRequest request) {
        return CustomResponse.ok(s3Service.generatePresignedUrls(request));
    }
}