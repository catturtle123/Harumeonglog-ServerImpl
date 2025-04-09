package com.example.harumeonglog.domain.pet.service;

import com.example.harumeonglog.domain.pet.controller.dto.request.PetRequest;
import com.example.harumeonglog.domain.pet.controller.dto.response.PetResponse;
import com.example.harumeonglog.domain.pet.controller.port.PetService;
import com.example.harumeonglog.domain.pet.domain.MemberPet;
import com.example.harumeonglog.domain.pet.domain.PetImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PetServiceImplTest {

    private PetService petService;

    @BeforeEach
    void init() {
        this.petService = new PetServiceImpl();
    }

    @Test
    @DisplayName("반려동물을 추가할 수 있다.")
    void addPet() {
        // given
        PetRequest.AddPetRequest request = new PetRequest.AddPetRequest();

        // when
        PetResponse.AddPetResponse response = petService.addPet(request);
        MemberPet memberPet = new MemberPet();
        PetImage petImage = new PetImage();


        // then
        assertThat(response).isNull();
    }

    @Test
    @DisplayName("반려동물 정보를 수정할 수 있다.")
    void changePetInfo() {
        // given
        Long petId = 1L;
        PetRequest.ChangePetInfoRequest request = new PetRequest.ChangePetInfoRequest();

        // when
        PetResponse.ChangePetInfoResponse response = petService.changePetInfo(petId, request);

        // then
        assertThat(response).isNull();
    }

    @Test
    @DisplayName("반려동물 목록을 조회할 수 있다.")
    void getPets() {
        // given
        Long cursor = 0L;
        int size = 10;

        // when
        PetResponse.GetPetsResponse response = petService.getPets(cursor, size);

        // then
        assertThat(response).isNull();
    }

    @Test
    @DisplayName("현재 반려동물을 변경할 수 있다.")
    void changeCurrentPet() {
        // given
        PetRequest.ChangeCurrentPetRequest request = new PetRequest.ChangeCurrentPetRequest();

        // when
        PetResponse.ChangeCurrentPetResponse response = petService.changeCurrentPet(request);

        // then
        assertThat(response).isNull();
    }

    @Test
    @DisplayName("반려동물을 삭제할 수 있다.")
    void deletePet() {
        // given
        Long petId = 1L;

        // when
        petService.deletePet(petId);

        // then
    }

    @Test
    @DisplayName("반려동물 그룹에 멤버를 초대할 수 있다.")
    void invite() {
        // given
        Long petId = 1L;
        PetRequest.InviteRequest request = new PetRequest.InviteRequest();

        // when
        petService.invite(petId, request);

        // then
    }

    @Test
    @DisplayName("이메일로 유저를 검색할 수 있다.")
    void searchMember() {
        // given
        String email = "test@example.com";
        Long cursor = 0L;
        int size = 10;

        // when
        PetResponse.SearchMemberResponse response = petService.searchMember(email, cursor, size);

        // then
        assertThat(response).isNull();
    }
}