package com.example.harumeonglog.domain.pet.service.command;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.dto.request.PetImageRequest;
import com.example.harumeonglog.domain.pet.dto.response.PetImageResponse;

import java.util.List;
import java.util.Map;

public interface PetImageCommandService {
    List<Map<String, String>> addImagesPresignedUrl(Member member, PetImageRequest.AddImagesPresignedUrlRequest images);
    PetImageResponse.AddImagesResponse addImage(PetImageRequest.AddImageRequest request);
    void deleteImage(Long imageId, Member member);
    void deleteImages(Long petId, PetImageRequest.DeleteImagesRequest request, Member member);
    Map<String,String> uploadImage(String filename, String contentType, Long petId);
}
