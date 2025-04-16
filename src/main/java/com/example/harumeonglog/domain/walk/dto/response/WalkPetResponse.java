package com.example.harumeonglog.domain.walk.dto.response;

import com.example.harumeonglog.domain.pet.entity.Pet;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class WalkPetResponse {
    @Getter
    @Builder
    public static class WalkPetListResponse {
        private List<WalkPetInfoResponse> pets;
        private int size;

        public static WalkPetListResponse from(List<Pet> pets) {
            return WalkPetListResponse.builder()
                    .pets(pets.stream().map(WalkPetInfoResponse::from).toList())
                    .size(pets.size())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class WalkPetInfoResponse {
        private Long petId;
        private String name;
        private String image;

        public static WalkPetInfoResponse from(Pet pet) {
            return WalkPetInfoResponse.builder()
                    .petId(pet.getId())
                    .name(pet.getName())
                    .image(pet.getMainImage())
                    .build();
        }
    }
}
