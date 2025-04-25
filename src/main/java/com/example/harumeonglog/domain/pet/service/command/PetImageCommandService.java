package com.example.harumeonglog.domain.pet.service.command;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.dto.request.PetImageRequest;
import com.example.harumeonglog.domain.pet.dto.response.PetImageResponse;

public interface PetImageCommandService {
    PetImageResponse.AddImagesResponse addImage(PetImageRequest.AddImageRequest request);
    void deleteImage(Long imageId, Member member);
    void deleteImages(Long petId, PetImageRequest.DeleteImagesRequest request, Member member);
}
