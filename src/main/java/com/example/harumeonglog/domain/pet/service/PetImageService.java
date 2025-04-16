package com.example.harumeonglog.domain.pet.service;

import com.example.harumeonglog.domain.pet.dto.request.PetImageRequest;
import com.example.harumeonglog.domain.pet.dto.response.PetImageResponse;

public interface PetImageService {
    PetImageResponse.AddImagesResponse addImages(Long petId, PetImageRequest.AddImagesRequest request);
    PetImageResponse.GetImagesResponse getImages(Long petId, Long cursor, int size);
    PetImageResponse.RecentImagesResponse recentImages();
    PetImageResponse.GetImageResponse getImage(Long imageId);
    void deleteImage(Long imageId);
    void deleteImages(Long petId, PetImageRequest.DeleteImagesRequest request);
}
