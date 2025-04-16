package com.example.harumeonglog.domain.pet.dto.request;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class PetImageRequest {
    @Getter
    @Setter
    public static class AddImagesRequest {
        private List<String> imageKeys; // 다중 이미지 키
    }
    @Getter
    @Setter
    public static class DeleteImagesRequest {
        private List<Long> imageIds; // 삭제할 이미지 ID 목록
    }
}
