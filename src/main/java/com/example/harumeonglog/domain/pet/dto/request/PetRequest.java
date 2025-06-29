package com.example.harumeonglog.domain.pet.dto.request;

import com.example.harumeonglog.domain.pet.entity.enums.Gender;
import com.example.harumeonglog.domain.pet.entity.enums.PetSize;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

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
        private String mainImageKey;
    }

    @Setter
    @Getter
    public static class ChangePetInfoRequest {
        private String name;
        private PetSize size;
        private String type;
        private Gender gender;
        private LocalDate birth;
        private String newMainImageKey;
    }

    @Setter
    @Getter
    public static class ChangeCurrentPetRequest {
        private Long petId;
    }

    @Setter
    @Getter
    public static class InviteRequest {
        private Long memberId;
        private String role;
    }

    @Setter
    @Getter
    public static class InviteListRequest {
        List<InviteRequest> requests;
    }

    @Setter
    @Getter
    public static class SearchMemberRequest {
        private String nickname;
    }

    @Getter
    public static class InviteResponseRequest{
        private String response;
    }
}