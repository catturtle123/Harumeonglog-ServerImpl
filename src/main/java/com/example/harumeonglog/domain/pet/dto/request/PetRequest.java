package com.example.harumeonglog.domain.pet.dto.request;

import com.example.harumeonglog.domain.member.entity.enums.SocialType;
import com.example.harumeonglog.domain.pet.entity.enums.Gender;
import com.example.harumeonglog.domain.pet.entity.enums.PetSize;
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
    }

    @Setter
    @Getter
    public static class ChangePetInfoRequest {
        private String name;
        private PetSize size;
        private String type;
        private Gender gender;
        private LocalDate birth;
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