package com.example.harumeonglog.domain.walk.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

public class MemberWalkRequest {

    @Getter
    public static class PetMemberRequest {
        private final List<Long> petId;

        public PetMemberRequest(
                @JsonProperty("petId") List<Long> petId
        ) {
            this.petId = petId;
        }
    }
}
