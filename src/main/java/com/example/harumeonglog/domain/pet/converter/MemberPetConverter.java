package com.example.harumeonglog.domain.pet.converter;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.enums.MemberPetRole;
import com.example.harumeonglog.domain.pet.entity.MemberPet;
import com.example.harumeonglog.domain.pet.entity.Pet;

public class MemberPetConverter {
    public static MemberPet toMemberPet( Member member, Pet pet, MemberPetRole role){
        return MemberPet.builder()
                .pet(pet)
                .member(member)
                .role(role)
                .build();
    }
}
