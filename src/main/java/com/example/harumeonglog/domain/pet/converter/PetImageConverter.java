package com.example.harumeonglog.domain.pet.converter;

import com.example.harumeonglog.domain.pet.dto.response.PetImageResponse;
import com.example.harumeonglog.domain.pet.entity.Pet;
import com.example.harumeonglog.domain.pet.entity.PetImage;
import com.example.harumeonglog.global.util.S3Util;
import org.springframework.data.domain.Slice;

import java.util.List;


public class PetImageConverter {
    public static PetImage toPetImage(Pet pet, String imageKey) {
        return PetImage.builder()
                .imageKey(imageKey)
                .pet(pet)
                .build();
    }

    public static PetImageResponse.AddImagesResponse toAddImagesResponse(List<Long> petImages) {
        return PetImageResponse.AddImagesResponse.builder()
                .imageIds(petImages)
                .build();
    }

    public static PetImageResponse.GetImagesResponse toGetImagesResponse(Slice<PetImage> imageSlice, S3Util s3Util) {
        List<PetImageResponse.GetImagesResponse.ImageInfo> imageInfos = imageSlice.getContent().stream()
                .map(image -> PetImageResponse.GetImagesResponse.ImageInfo.builder()
                        .imageId(image.getId())
                        .imageKey(s3Util.getUrlFromKey(image.getImageKey()))
                        .createdAt(image.getCreatedAt())
                        .build())
                .toList();

        Long nextCursor = imageSlice.hasNext() ?
                imageInfos.get(imageInfos.size() - 1).getImageId() : null;

        return PetImageResponse.GetImagesResponse.builder()
                .images(imageInfos)
                .cursor(nextCursor)
                .hasNext(imageSlice.hasNext())
                .build();
    }

    public static PetImageResponse.GetImageResponse toGetImageResponse(PetImage petImage, String image) {
        return PetImageResponse.GetImageResponse.builder()
                .imageId(petImage.getId())
                .imageKey(image)
                .createdAt(petImage.getCreatedAt())
                .build();
    }
}
