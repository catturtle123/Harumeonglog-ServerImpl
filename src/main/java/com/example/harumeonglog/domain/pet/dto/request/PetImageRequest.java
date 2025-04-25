package com.example.harumeonglog.domain.pet.dto.request;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class PetImageRequest {
    @Getter
    @Setter
    public static class AddImagesPresignedUrlRequest {
        private List<String> filenames;
        private List<String> contentTypes;
        private Long petId;
    }
    @Getter
    @Setter
    public static class DeleteImagesRequest {
        private List<Long> imageIds; // 삭제할 이미지 ID 목록
    }
    @Getter
    @Builder
    public static class AddImageRequest {
        private Long petId;
        private List<String> imageKeys;
    }
}
