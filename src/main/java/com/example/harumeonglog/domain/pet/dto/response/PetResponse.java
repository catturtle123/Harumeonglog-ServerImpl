package com.example.harumeonglog.domain.pet.dto.response;


import com.example.harumeonglog.domain.pet.entity.enums.Gender;
import com.example.harumeonglog.domain.pet.entity.enums.PetSize;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public class PetResponse {

    @Builder
    @Getter
    public static class AddPetResponse {
        private Long petId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
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
        private LocalDateTime updatedAt;

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
        private String image;
    }

    @Getter
    @Builder
    public static class PetPreviewResponse {
        private Long petId;
        private String name;
        private String mainImage;
    }

    @Getter
    @Builder
    public static class PetListPreviewResponse {
        List<PetPreviewResponse> pets;
        private Long cursor;
        private Boolean hasNext;
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
            private String email;
            private String name;
            private String image;
        }
    }

    @Getter
    @Builder
    public static class MainPetResponse{
        private Long petId;
        private String name;
        private String mainImage;
        private Gender gender;
        private LocalDate birth;
    }
}