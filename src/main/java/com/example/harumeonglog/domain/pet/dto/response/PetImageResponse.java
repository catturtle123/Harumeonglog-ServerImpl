package com.example.harumeonglog.domain.pet.dto.response;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class PetImageResponse {

    @Getter
    @Builder
    public static class AddImagesResponse {
        private List<Long> imageIds;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Builder
    @Getter
    public static class GetImagesResponse {
        private List<ImageInfo> images;
        private Long cursor;
        private boolean hasNext;

        @Getter
        @Builder
        public static class ImageInfo {
            private Long imageId;
            private String imageKey;
            private LocalDateTime createdAt;
        }
    }

    @Getter
    @Builder
    public static class RecentImagesResponse {
        private List<ImageInfo> images;

        @Getter
        @Builder
        public static class ImageInfo {
            private Long imageId;
            private String imageKey;
            private LocalDateTime createdAt;
        }
    }

    @Getter
    @Builder
    public static class GetImageResponse {
        private Long imageId;
        private String imageKey;
        private LocalDateTime createdAt;
    }

}