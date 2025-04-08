package com.example.harumeonglog.domain.pet.service;

import com.example.harumeonglog.domain.pet.controller.dto.request.PetImageRequest;
import com.example.harumeonglog.domain.pet.controller.dto.response.PetImageResponse;
import com.example.harumeonglog.domain.pet.controller.port.PetImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PetImageServiceImplTest {

    private PetImageService petImageService;

    @BeforeEach
    void init() {
        this.petImageService = new PetImageServiceImpl();
    }

    @Test
    @DisplayName("이미지를 추가할 수 있다.")
    void addImages() {
        // given
        Long petId = 1L;
        PetImageRequest.AddImagesRequest request = new PetImageRequest.AddImagesRequest(/* 예시로 빈 생성자 */);

        // when
        PetImageResponse.AddImagesResponse response = petImageService.addImages(petId, request);

        // then
        assertThat(response).isNull();
    }

    @Test
    @DisplayName("이미지 목록을 불러올 수 있다.")
    void getImages() {
        // given
        Long petId = 1L;
        Long cursor = 0L;
        int size = 10;

        // when
        PetImageResponse.GetImagesResponse response = petImageService.getImages(petId, cursor, size);

        // then
        assertThat(response).isNull();
    }

    @Test
    @DisplayName("최근 이미지를 조회할 수 있다.")
    void recentImages() {
        // when
        PetImageResponse.RecentImagesResponse response = petImageService.recentImages();

        // then
        assertThat(response).isNull();
    }

    @Test
    @DisplayName("이미지 상세 정보를 조회할 수 있다.")
    void getImage() {
        // given
        Long imageId = 1L;

        // when
        PetImageResponse.GetImageResponse response = petImageService.getImage(imageId);

        // then
        assertThat(response).isNull();
    }

    @Test
    @DisplayName("이미지를 삭제할 수 있다.")
    void deleteImage() {
        // given
        Long imageId = 1L;

        // when
        petImageService.deleteImage(imageId);

        // then
    }

    @Test
    @DisplayName("여러 이미지를 삭제할 수 있다.")
    void deleteImages() {
        // given
        Long petId = 1L;
        PetImageRequest.DeleteImagesRequest request = new PetImageRequest.DeleteImagesRequest(/* 예시로 빈 생성자 */);

        // when
        petImageService.deleteImages(petId, request);

        // then
    }
}