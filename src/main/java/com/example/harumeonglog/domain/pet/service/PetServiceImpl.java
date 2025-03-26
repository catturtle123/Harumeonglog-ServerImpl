package com.example.harumeonglog.domain.pet.service;

import com.example.harumeonglog.domain.pet.controller.dto.request.PetRequest;
import com.example.harumeonglog.domain.pet.controller.dto.response.PetResponse;
import com.example.harumeonglog.domain.pet.controller.port.PetService;
import org.springframework.stereotype.Service;

@Service
public class PetServiceImpl implements PetService {

    @Override
    public PetResponse.AddPetResponse addPet(PetRequest.AddPetRequest request) {
        return null;
    }

    @Override
    public PetResponse.ChangePetInfoResponse changePetInfo(Long petId, PetRequest.ChangePetInfoRequest request) {
        return null;
    }

    @Override
    public PetResponse.GetPetsResponse getPets(Long cursor, int size) {
        return null;
    }

    @Override
    public PetResponse.ChangeCurrentPetResponse changeCurrentPet(PetRequest.ChangeCurrentPetRequest request) {
        return null;
    }

    @Override
    public void deletePet(Long petId) {

    }

    @Override
    public void invite(Long petId, PetRequest.InviteRequest request) {

    }

    @Override
    public PetResponse.SearchMemberResponse searchMember(PetRequest.SearchMemberRequest request) {
        return null;
    }

    @Override
    public PetResponse.GetMembersResponse getMembers(Long petId) {
        return null;
    }
}
