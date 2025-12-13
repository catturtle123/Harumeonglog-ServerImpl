package com.example.harumeonglog.global.s3.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class S3ResponseDTO {

    @Getter
    @Builder
    public static class S3ResponsePreviewDTO{
        public String presignedUrl;
        public String imageKey;
    }

    @Getter
    @Builder
    public static class S3ResponseListDTO{
        public List<EntityImageResponse> entities;
    }


    @Getter
    @Builder
    public static class EntityImageResponse {
        private Long entityId;
        private List<S3ResponsePreviewDTO> images;
    }
}
