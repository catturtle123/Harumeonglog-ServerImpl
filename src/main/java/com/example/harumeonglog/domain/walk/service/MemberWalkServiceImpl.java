package com.example.harumeonglog.domain.walk.service;


import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.entity.Pet;
import com.example.harumeonglog.domain.walk.dto.request.MemberWalkRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberWalkServiceImpl implements MemberWalkService {
    @Override
    public List<Pet> getPets(Member member) {
        return List.of();
    }

    @Override
    public List<Member> getMembers(MemberWalkRequest.PetMemberRequest dto) {
        return List.of();
    }
}
