package com.example.harumeonglog.domain.pet.service.query;

import com.example.harumeonglog.domain.member.dto.response.InvitationResponse;
import com.example.harumeonglog.domain.member.entity.Invitation;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.repository.InvitationRepository;
import com.example.harumeonglog.domain.member.repository.MemberRepository;
import com.example.harumeonglog.domain.pet.converter.MemberPetConverter;
import com.example.harumeonglog.domain.pet.converter.PetConverter;
import com.example.harumeonglog.domain.pet.dto.response.PetResponse;
import com.example.harumeonglog.domain.pet.entity.MemberPet;
import com.example.harumeonglog.domain.pet.entity.Pet;
import com.example.harumeonglog.domain.pet.repository.MemberPetRepository;
import com.example.harumeonglog.domain.pet.repository.PetRepository;
import com.example.harumeonglog.global.error.code.PetErrorCode;
import com.example.harumeonglog.global.error.exception.PetException;
import com.example.harumeonglog.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PetQueryServiceImpl implements PetQueryService {
    private final MemberPetRepository memberPetRepository;
    private final MemberRepository memberRepository;
    private final S3Util s3Util;
    private final PetRepository petRepository;
    private final InvitationRepository invitationRepository;


    @Override
    public PetResponse.GetPetsResponse getPets(Long cursor, int size, Member member) {
        // 페이징 처리된 멤버펫 관계 조회
        Slice<MemberPet> memberPetSlice = fetchMemberPetSliceWithCursor(cursor, size, member);

        List<MemberPet> currentMemberPets = memberPetSlice.getContent();
        List<Long> petIds = currentMemberPets.stream()
                .map(mp -> mp.getPet().getId())
                .collect(Collectors.toList());

        List<MemberPet> relatedMemberPets = petIds.isEmpty() ?
                Collections.emptyList() :
                memberPetRepository.findByPetIdsAndNotMemberId(petIds, member.getId());

        return MemberPetConverter.toGetPetsResponse(memberPetSlice, currentMemberPets, relatedMemberPets, s3Util);
    }

    @Override
    public PetResponse.PetListPreviewResponse getChangePet(Long cursor, int size, Member member) {
        // 페이징 처리된 멤버펫 관계 조회 (재사용)
        Slice<MemberPet> memberPetSlice = fetchMemberPetSliceWithCursor(cursor, size, member);

        return MemberPetConverter.toGetChangePetResponse(memberPetSlice, s3Util);
    }

    @Override
    public PetResponse.SearchMemberResponse searchMember(String email, Member member, Long cursor, int size) {
        Pageable pageable = PageRequest.of(0, size);
        Slice<Member> memberSlice;

        if (cursor == null || cursor == 0L) {
            memberSlice = memberRepository.findByEmailContaining(email, member.getId(), pageable);
        } else {
            memberSlice = memberRepository.findByEmailContainingAndCursor(email, member.getId(), cursor, pageable);
        }

        return MemberPetConverter.toSearchMemberResponse(memberSlice, s3Util);
    }

    @Override
    public PetResponse.MainPetResponse getMainPet(Member member) {
        if(member.getCurrentPetId() == null) {
            throw new PetException(PetErrorCode.CURRENT_PET_NOT_FOUND);
        }

        Pet pet = petRepository.findById(member.getCurrentPetId()).orElseThrow(() -> new PetException(PetErrorCode.NOT_FOUND));
        String mainImage = s3Util.getUrlFromKey(pet.getMainImage());
        return PetConverter.toMainPetResponse(pet, mainImage);
    }

    @Override
    public InvitationResponse.InvitationListResponse getInvite(Long cursor, Integer size, Member member) {

        if (cursor == 0) {
            cursor = Long.MAX_VALUE;
        }

        Slice<Invitation> invitationSlice = invitationRepository.findByReceiverAndIdLessThanOrderByIdDesc(
                member, cursor, PageRequest.of(0, size));

        return PetConverter.toInvitationListResponse(invitationSlice, s3Util);
    }

    private Slice<MemberPet> fetchMemberPetSliceWithCursor(Long cursor, int size, Member member) {
        Pageable pageable = PageRequest.of(0, size);

        if (cursor == null || cursor == 0L) {
            return memberPetRepository.findFirstPageByMemberId(member.getId(), pageable);
        } else {
            return memberPetRepository.findByMemberAndCursor(member.getId(), cursor, pageable);
        }
    }


}