package com.example.harumeonglog.global.s3.controller.specification;


import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.global.s3.dto.request.S3RequestDTO;
import com.example.harumeonglog.global.s3.dto.response.S3ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "S3", description = "S3 이미지 업로드 관련 API")
public interface S3ControllerSpecification {


    @Operation(summary = "S3 단일 이미지 PresignedUrl 발급 by 백종우", description = "domain에 해당하는 S3 단일 이미지의 PresignedUrl을 발급합니다.")
    @ApiResponse(responseCode = "COMMON200", description = "발급성공")
    @PostMapping("/presigned-urls")
    CustomResponse<S3ResponseDTO.S3ResponsePreviewDTO> getPresignedUrl(
            @RequestBody S3RequestDTO.GeneratePresignedUrlRequest request);

    @Operation(summary = "S3 복수 이미지 PresignedUrl 발급 by 백종우", description = "domain에 해당하는 S3 복수 이미지의 PresignedUrl을 발급합니다.")
    @ApiResponse(responseCode = "COMMON200", description = "발급성공")
    @PostMapping("/presigned-urls/batch")
    CustomResponse<S3ResponseDTO.S3ResponseListDTO> getPresignedUrls(
            @RequestBody S3RequestDTO.GeneratePresignedUrlsRequest request);
}
