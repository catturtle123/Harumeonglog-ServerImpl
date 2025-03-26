package com.example.harumeonglog.domain.pet.controller.port;

import com.example.harumeonglog.domain.pet.controller.dto.request.PetRequest;
import com.example.harumeonglog.domain.pet.controller.dto.response.PetResponse;

public interface PetService {
    PetResponse.AddPetResponse addPet(PetRequest.AddPetRequest request);
    PetResponse.ChangePetInfoResponse changePetInfo(Long petId, PetRequest.ChangePetInfoRequest request);
    PetResponse.GetPetsResponse getPets(Long cursor, int size);
    PetResponse.ChangeCurrentPetResponse changeCurrentPet(PetRequest.ChangeCurrentPetRequest request);
    void deletePet(Long petId);
    void invite(Long petId, PetRequest.InviteRequest request);
    PetResponse.SearchMemberResponse searchMember(PetRequest.SearchMemberRequest request);
    PetResponse.GetMembersResponse getMembers(Long petId);
}
