package com.example.harumeonglog.domain.walk.service;


import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.entity.Pet;
import com.example.harumeonglog.domain.walk.dto.request.MemberWalkRequest;

import java.util.List;

public interface MemberWalkService {
    List<Pet> getPets(Member member);
    List<Member> getMembers(MemberWalkRequest.PetMemberRequest dto);
}
