package com.example.harumeonglog.domain.pet.converter;

import com.example.harumeonglog.domain.member.dto.response.InvitationResponse;
import com.example.harumeonglog.domain.member.entity.Invitation;
import com.example.harumeonglog.domain.pet.dto.request.PetRequest;
import com.example.harumeonglog.domain.pet.dto.response.PetResponse;
import com.example.harumeonglog.domain.pet.entity.Pet;
import com.example.harumeonglog.global.util.S3Util;
import org.springframework.data.domain.Slice;

import java.util.List;

public class PetConverter {

    public static Pet toPet(PetRequest.AddPetRequest request){
        return Pet.builder()
                .name(request.getName())
                .size(request.getSize())
                .type(request.getType())
                .gender(request.getGender())
                .birth(request.getBirth())
                .mainImage(request.getMainImageKey())
                .build();
    }

    public static PetResponse.AddPetResponse toAddPetResponse(Pet pet){
        return PetResponse.AddPetResponse.builder()
                .petId(pet.getId())
                .createdAt(pet.getCreatedAt())
                .updatedAt(pet.getUpdatedAt())
                .build();
    }

    public static PetResponse.ChangePetInfoResponse toChangePetInfoResponse(Pet pet, String mainImage){
        return PetResponse.ChangePetInfoResponse.builder()
                .petId(pet.getId())
                .name(pet.getName())
                .size(pet.getSize())
                .type(pet.getType())
                .gender(pet.getGender())
                .birth(pet.getBirth())
                .mainImage(mainImage)
                .updatedAt(pet.getUpdatedAt())
                .build();
    }

    public static PetResponse.MainPetResponse toMainPetResponse(Pet pet, String mainImage){
        return PetResponse.MainPetResponse.builder()
                .petId(pet.getId())
                .name(pet.getName())
                .birth(pet.getBirth())
                .mainImage(mainImage)
                .gender(pet.getGender())
                .build();
    }

    public static InvitationResponse.InviteResponse toInvitationResponse(Invitation invitation, S3Util s3Util) {
        return InvitationResponse.InviteResponse.builder()
                .invitationId(invitation.getId())
                .image(s3Util.getUrlFromKey(invitation.getPet().getMainImage()))
                .petId(invitation.getPet().getId())
                .petName(invitation.getPet().getName())
                .role(invitation.getRole().name())
                .senderId(invitation.getReceiver().getId())
                .senderName(invitation.getSender().getNickname())
                .createdAt(invitation.getCreatedAt())
                .build();
    }

    public static InvitationResponse.InvitationListResponse toInvitationListResponse(Slice<Invitation> invitationSlice, S3Util s3Util) {
        List<Invitation> invitationList = invitationSlice.toList();

        Long nextCursor = null;
        if (!invitationList.isEmpty() && invitationSlice.hasNext()) {
            nextCursor = invitationList.get(invitationList.size() - 1).getId();
        }

        List<InvitationResponse.InviteResponse> invitationResponseList = invitationList.stream()
                .map(invitation -> toInvitationResponse(invitation, s3Util))
                .toList();

        return InvitationResponse.InvitationListResponse.builder()
                .invitations(invitationResponseList)
                .size(invitationList.size())
                .hasNext(invitationSlice.hasNext())
                .nextCursor(nextCursor)
                .build();
    }

}
