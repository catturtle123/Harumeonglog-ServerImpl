package com.example.harumeonglog.domain.pet.service;

import com.example.harumeonglog.domain.pet.controller.dto.request.PetImageRequest;
import com.example.harumeonglog.domain.pet.controller.dto.response.PetImageResponse;
import com.example.harumeonglog.domain.pet.controller.port.PetImageService;
import org.springframework.stereotype.Service;

@Service
public class PetImageServiceImpl implements PetImageService {
    @Override
    public PetImageResponse.AddImagesResponse addImages(Long petId, PetImageRequest.AddImagesRequest request) {
        return null;
    }

    @Override
    public PetImageResponse.GetImagesResponse getImages(Long petId, Long cursor, int size) {
        return null;
    }

    @Override
    public PetImageResponse.RecentImagesResponse recentImages() {
        return null;
    }

    @Override
    public PetImageResponse.GetImageResponse getImage(Long imageId) {
        return null;
    }

    @Override
    public void deleteImage(Long imageId) {

    }

    @Override
    public void deleteImages(Long petId, PetImageRequest.DeleteImagesRequest request) {

    }
}
