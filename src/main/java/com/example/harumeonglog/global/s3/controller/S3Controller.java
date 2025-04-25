package com.example.harumeonglog.global.s3.controller;

import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.global.s3.controller.specification.S3ControllerSpecification;
import com.example.harumeonglog.global.s3.dto.request.S3RequestDTO;
import com.example.harumeonglog.global.s3.dto.response.S3ResponseDTO;
import com.example.harumeonglog.global.s3.service.S3Service;
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
public class S3Controller implements S3ControllerSpecification {

    private final S3Service s3Service;

    @PostMapping("/presigned-urls")
    public CustomResponse<S3ResponseDTO.S3ResponsePreviewDTO> getPresignedUrl(
            @RequestBody S3RequestDTO.GeneratePresignedUrlRequest request) {
        return CustomResponse.ok(s3Service.generatePresignedUrl(request));
    }

    @PostMapping("/presigned-urls/batch")
    public CustomResponse<S3ResponseDTO.S3ResponseListDTO> getPresignedUrls(
            @RequestBody S3RequestDTO.GeneratePresignedUrlsRequest request) {
        return CustomResponse.ok(s3Service.generatePresignedUrls(request));
    }
}