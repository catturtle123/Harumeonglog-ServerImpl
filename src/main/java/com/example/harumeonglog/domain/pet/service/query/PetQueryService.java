package com.example.harumeonglog.domain.pet.service.query;

import com.example.harumeonglog.domain.member.dto.response.InvitationResponse;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.dto.response.PetResponse;

public interface PetQueryService {
    PetResponse.GetPetsResponse getPets(Long cursor, int size, Member member);
    PetResponse.PetListPreviewResponse getChangePet(Long cursor, int size, Member member);
    PetResponse.SearchMemberResponse searchMember(String email, Member member, Long cursor, int size);
    PetResponse.MainPetResponse getMainPet(Member member);
    InvitationResponse.InvitationListResponse getInvite(Long cursor, Integer size, Member member);
}
