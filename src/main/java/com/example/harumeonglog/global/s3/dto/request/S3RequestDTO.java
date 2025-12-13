package com.example.harumeonglog.global.s3.dto.request;

import com.example.harumeonglog.global.s3.enums.S3Domain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class S3RequestDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GeneratePresignedUrlsRequest {
        private S3Domain domain;
        private List<EntityImageRequest> entities;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EntityImageRequest {
        private Long entityId;
        private List<GeneratePresignedUrlPreview> images;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GeneratePresignedUrlPreview {
        private String filename;
        private String contentType;
    }

}