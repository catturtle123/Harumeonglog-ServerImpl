package com.example.harumeonglog.domain.pet.service.command;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.dto.request.PetRequest;
import com.example.harumeonglog.domain.pet.dto.response.PetResponse;
import org.springframework.web.multipart.MultipartFile;

public interface PetCommandService {
    PetResponse.AddPetResponse addPet(PetRequest.AddPetRequest request, Member member);
    PetResponse.ChangePetInfoResponse changePetInfo(Long petId, PetRequest.ChangePetInfoRequest request, Member member);
    void changeCurrentPet(PetRequest.ChangeCurrentPetRequest request, Member member);
    void deletePet(Long petId, Member member);
    void invite(Long petId, PetRequest.InviteListRequest request, Member member);
}
