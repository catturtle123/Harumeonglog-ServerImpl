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
        private List<GeneratePresignedUrlPreview> images;
        private S3Domain domain;
        private Long entityId;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GeneratePresignedUrlPreview {
        private String filename;
        private String contentType;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GeneratePresignedUrlRequest {
        private GeneratePresignedUrlPreview image;
        private S3Domain domain;
        private Long entityId;
    }
}