package com.example.harumeonglog.domain.pet.converter;

import com.example.harumeonglog.domain.pet.dto.request.PetRequest;
import com.example.harumeonglog.domain.pet.dto.response.PetResponse;
import com.example.harumeonglog.domain.pet.entity.Pet;

public class PetConverter {

    public static Pet toPet(PetRequest.AddPetRequest request){
        return Pet.builder()
                .name(request.getName())
                .size(request.getSize())
                .type(request.getType())
                .gender(request.getGender())
                .birth(request.getBirth())
                .mainImage(request.getMainImageKey())
                .build();
    }

    public static PetResponse.AddPetResponse toAddPetResponse(Pet pet){
        return PetResponse.AddPetResponse.builder()
                .petId(pet.getId())
                .createdAt(pet.getCreatedAt())
                .updatedAt(pet.getUpdatedAt())
                .build();
    }

    public static PetResponse.ChangePetInfoResponse toChangePetInfoResponse(Pet pet, String mainImage){
        return PetResponse.ChangePetInfoResponse.builder()
                .petId(pet.getId())
                .name(pet.getName())
                .size(pet.getSize())
                .type(pet.getType())
                .gender(pet.getGender())
                .birth(pet.getBirth())
                .mainImage(mainImage)
                .updatedAt(pet.getUpdatedAt())
                .build();
    }

    public static PetResponse.MainPetResponse toMainPetResponse(Pet pet, String mainImage){
        return PetResponse.MainPetResponse.builder()
                .petId(pet.getId())
                .name(pet.getName())
                .birth(pet.getBirth())
                .mainImage(mainImage)
                .gender(pet.getGender())
                .build();
    }
}
