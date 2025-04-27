package com.example.harumeonglog.domain.walk.service;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.entity.MemberPet;
import com.example.harumeonglog.domain.pet.entity.Pet;
import com.example.harumeonglog.domain.pet.repository.MemberPetRepository;
import com.example.harumeonglog.domain.walk.converter.WalkConverter;
import com.example.harumeonglog.domain.walk.dto.request.WalkRequest;
import com.example.harumeonglog.domain.walk.dto.response.WalkResponse;
import com.example.harumeonglog.domain.walk.entity.Walk;
import com.example.harumeonglog.domain.walk.entity.enums.WalkStatus;
import com.example.harumeonglog.domain.walk.repository.WalkRepository;
import com.example.harumeonglog.global.error.code.WalkErrorCode;
import com.example.harumeonglog.global.error.exception.WalkException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalkQueryServiceImpl implements WalkQueryService {

    private final WalkRepository walkRepository;
    private final MemberPetRepository memberPetRepository;

    @Override
    public WalkResponse.WalkAvailablePetListResponse getAvailablePets(Member member) {
        List<Pet> availablePets = memberPetRepository.findByMember(member.getId()).stream().map(MemberPet::getPet).toList();
        return WalkConverter.toWalkAvailablePetListResponse(availablePets);
    }

    @Override
    public WalkResponse.WalkAvailableMemberListResponse getAvailableMembers(WalkRequest.AvailableMemberRequest dto) {
        Set<Member> members = new HashSet<>();
        dto.getPetId().forEach(petId ->
            members.addAll(memberPetRepository.findByPet(petId).stream().map(MemberPet::getMember).toList())
        );
        return WalkConverter.toWalkAvailableMemberListResponse(members);
    }

    @Override
    public WalkResponse.WalkSearchListResponse getWalkList(String sort, Long cursor, int offset) {
        return null;
    }

    @Override
    public WalkResponse.WalkDetailResponse getWalkDetails(Long walkId) {
        return null;
    }

    @Override
    public Walk findById(Long walkId) {
        return walkRepository.findById(walkId).orElseThrow(() -> new WalkException(WalkErrorCode.NOT_FOUND));
    }

    @Override
    public boolean hasStatus(Walk walk, WalkStatus... walkStatus) {
        return Arrays.stream(walkStatus).anyMatch(status -> status.equals(walk.getStatus()));
    }
}
