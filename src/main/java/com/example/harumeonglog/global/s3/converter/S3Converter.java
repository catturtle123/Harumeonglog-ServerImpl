package com.example.harumeonglog.global.s3.converter;

import com.example.harumeonglog.global.s3.dto.response.S3ResponseDTO;

import java.util.List;

public class S3Converter {

    public static S3ResponseDTO.S3ResponsePreviewDTO toS3ResponsePreviewDTO(String prsignedUrl, String imageKey) {
        return S3ResponseDTO.S3ResponsePreviewDTO.builder()
                .presignedUrl(prsignedUrl)
                .imageKey(imageKey)
                .build();
    }


    public static S3ResponseDTO.EntityImageResponse toEntityImageResponse(Long entityId, List<S3ResponseDTO.S3ResponsePreviewDTO> images) {
        return S3ResponseDTO.EntityImageResponse.builder()
                .entityId(entityId)
                .images(images)
                .build();
    }

    public static S3ResponseDTO.S3ResponseListDTO toS3ResponseDTO(List<S3ResponseDTO.EntityImageResponse> entities) {
        return S3ResponseDTO.S3ResponseListDTO.builder()
                .entities(entities)
                .build();
    }
}


