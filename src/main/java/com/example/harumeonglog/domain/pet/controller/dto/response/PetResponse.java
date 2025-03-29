package com.example.harumeonglog.domain.pet.controller.dto.response;


import com.example.harumeonglog.domain.pet.domain.enums.Gender;
import com.example.harumeonglog.domain.pet.domain.enums.MemberPetRole;
import com.example.harumeonglog.domain.pet.domain.enums.PetSize;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;


public class PetResponse {

    @Builder
    @Getter
    public static class AddPetResponse {
        private Long petId;
    }
    @Getter
    @Builder
    public static class ChangePetInfoResponse {
        private Long petId;
        private String name;
        private PetSize size;
        private String type;
        private Gender gender;
        private LocalDate birth;
        private String mainImage;
    }
    @Getter
    @Builder
    public static class GetPetsResponse {
        private List<PetInfo> pets;
        private Long cursor; // 다음 페이지로 이동할 커서 (null이면 마지막 페이지)
        private Boolean hasNext;
    }

    @Getter
    @Builder
    public static class PetInfo {
        private String role;
        private Long petId;
        private String name;
        private PetSize size;
        private String type;
        private Gender gender;
        private LocalDate birth;
        private String mainImage;
        private List<PeopleInfo> people;
    }

    @Getter
    @Builder
    public static class PeopleInfo{
        private Long id;
        private String name;
        private String role;
    }

    @Getter
    @Builder
    public static class ChangeCurrentPetResponse {
        private Long petId;
        private String name;
    }
    @Getter
    @Builder
    public static class SearchMemberResponse {
        private List<MemberInfo> members;
        private Long cursor;
        private Boolean hasNext;

        @Getter
        @Builder
        public static class MemberInfo {
            private Long memberId;
            private String name;
        }
    }
}