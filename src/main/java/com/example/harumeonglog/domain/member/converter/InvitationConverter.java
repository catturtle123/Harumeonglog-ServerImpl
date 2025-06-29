package com.example.harumeonglog.domain.member.converter;

import com.example.harumeonglog.domain.member.entity.Invitation;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.enums.MemberPetRole;
import com.example.harumeonglog.domain.pet.entity.Pet;

public class InvitationConverter {

    public static Invitation toInvitation(Pet pet, MemberPetRole role, Member sender, Member receiver) {
        return Invitation.builder()
                .role(role)
                .sender(sender)
                .receiver(receiver)
                .pet(pet)
                .build();
    }
}
