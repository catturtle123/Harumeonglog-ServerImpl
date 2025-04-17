package com.example.harumeonglog.domain.pet.service.command;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.dto.request.PetRequest;
import com.example.harumeonglog.domain.pet.dto.response.PetResponse;
import org.springframework.web.multipart.MultipartFile;

public interface PetCommandService {
    PetResponse.AddPetResponse addPet(PetRequest.AddPetRequest request, MultipartFile mainImage, Member member);
    PetResponse.ChangePetInfoResponse changePetInfo(Long petId, PetRequest.ChangePetInfoRequest request);
    PetResponse.ChangeCurrentPetResponse changeCurrentPet(PetRequest.ChangeCurrentPetRequest request);
    void deletePet(Long petId);
    void invite(Long petId, PetRequest.InviteRequest request);
}
