package com.example.harumeonglog.domain.pet.controller.dto.request;

import com.example.harumeonglog.domain.member.domain.enums.SocialType;
import com.example.harumeonglog.domain.pet.domain.enums.Gender;
import com.example.harumeonglog.domain.pet.domain.enums.PetSize;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PetRequest {


    @Setter
    @Getter
    public static class AddPetRequest {
        private String name;
        private PetSize size;
        private String type;
        private Gender gender;
        private LocalDate birth;
        private String mainImage;
    }

    @Setter
    @Getter
    public static class ChangePetInfoRequest {
        private String name;
        private PetSize size;
        private String type;
        private Gender gender;
        private LocalDate birth;
        private String mainImage;
    }

    @Setter
    @Getter
    public static class ChangeCurrentPetRequest {
        private Long petId;
    }

    @Setter
    @Getter
    public static class InviteRequest {
        private String email;
        private SocialType socialType;
    }

    @Setter
    @Getter
    public static class SearchMemberRequest {
        private String nickname;
    }
}