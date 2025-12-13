package com.example.harumeonglog.domain.walk.service;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.entity.MemberPet;
import com.example.harumeonglog.domain.pet.entity.Pet;
import com.example.harumeonglog.domain.pet.repository.MemberPetRepository;
import com.example.harumeonglog.domain.walk.converter.WalkConverter;
import com.example.harumeonglog.domain.walk.dto.request.WalkRequest;
import com.example.harumeonglog.domain.walk.dto.response.WalkResponse;
import com.example.harumeonglog.domain.walk.entity.MemberWalk;
import com.example.harumeonglog.domain.walk.entity.Walk;
import com.example.harumeonglog.domain.walk.entity.enums.WalkStatus;
import com.example.harumeonglog.domain.walk.enums.WalkSort;
import com.example.harumeonglog.domain.walk.repository.MemberWalkRepository;
import com.example.harumeonglog.domain.walk.repository.WalkLikeRepository;
import com.example.harumeonglog.domain.walk.repository.WalkRepository;
import com.example.harumeonglog.global.error.code.WalkErrorCode;
import com.example.harumeonglog.global.error.exception.WalkException;
import com.example.harumeonglog.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalkQueryServiceImpl implements WalkQueryService {

    private final WalkRepository walkRepository;
    private final MemberPetRepository memberPetRepository;
    private final WalkLikeRepository walkLikeRepository;
    private final MemberWalkRepository memberWalkRepository;
    private final S3Util s3Util;

    @Override
    public WalkResponse.WalkAvailablePetListResponse getAvailablePets(Member member) {
        List<Pet> availablePets = memberPetRepository.findByMember(member.getId()).stream().map(MemberPet::getPet).toList();
        return WalkConverter.toWalkAvailablePetListResponse(availablePets, s3Util);
    }

    @Override
    public WalkResponse.WalkAvailableMemberListResponse getAvailableMembers(WalkRequest.AvailableMemberRequest dto) {
        Set<Member> members = new LinkedHashSet<>();
        dto.getPetId().forEach(petId ->
            members.addAll(memberPetRepository.findByPet(petId).stream().map(MemberPet::getMember).toList())
        );
        return WalkConverter.toWalkAvailableMemberListResponse(members, s3Util);
    }

    @Override
    public WalkResponse.WalkSearchListResponse getWalkList(Member member, String sort, Long cursor, int offset) {
        Pageable pageable = PageRequest.of(0, offset);

        Long initialCursorValue = 0L;
        Slice<Walk> walks;
        if (sort.equalsIgnoreCase(WalkSort.DISTANCE.name())) {
            walks = cursor.equals(initialCursorValue) ? walkRepository.findAllByIsSharedIsTrueOrderByDistanceDescIdDesc(pageable) :
                    walkRepository.findAllByDistanceDesc(cursor, pageable);
        }
        else if (sort.equalsIgnoreCase(WalkSort.TIME.name())) {
            walks = cursor.equals(initialCursorValue) ? walkRepository.findAllByIsSharedIsTrueOrderByTimeDescIdDesc(pageable) :
                    walkRepository.findAllByTimeDesc(cursor, pageable);
        }
        else if (sort.equalsIgnoreCase(WalkSort.RECOMMEND.name())) {
            walks = cursor.equals(initialCursorValue) ? walkRepository.findAllByIsSharedIsTrueOrderByWalkLikeNumDescIdDesc(pageable) :
                    walkRepository.findAllByWalkLikeNumDesc(cursor, pageable);
        }
        else {
            throw new WalkException(WalkErrorCode.UNSUPPORTED_SORT);
        }

        return buildWalkSearchListResponse(member, walks);
    }

    @Override
    public WalkResponse.WalkDetailResponse getWalkDetails(Member member, Long walkId) {
        Walk walk = findById(walkId);

        return WalkConverter.toWalkDetailResponse(walk, memberWalkRepository.findTopByWalkOrderByCreatedAtAsc(walk).getMember().getNickname(), walkLikeRepository.existsByMemberAndWalk(member, walk));
    }

    @Override
    public Walk findById(Long walkId) {
        return walkRepository.findById(walkId).orElseThrow(() -> new WalkException(WalkErrorCode.NOT_FOUND));
    }

    @Override
    public boolean hasStatus(Walk walk, WalkStatus... walkStatus) {
        return Arrays.stream(walkStatus).anyMatch(status -> status.equals(walk.getStatus()));
    }

    public WalkResponse.WalkSearchListResponse buildWalkSearchListResponse(Member member, Slice<Walk> walkSlice) {
        List<Walk> walks = walkSlice.getContent();
        boolean hasNext = walkSlice.hasNext();
        Long cursor = null;
        if (hasNext && !walks.isEmpty()) {
            cursor = walks.get(walks.size() - 1).getId();
        }

        List<Long> walkIds = walks.stream().map(Walk::getId).toList();
        List<WalkResponse.WalkSearchResponse> responses = new ArrayList<>();
        Map<Long, String> nicknames = new HashMap<>();
        for (MemberWalk memberWalk : memberWalkRepository.findMemberNicknameByWalks(walkIds)) {
            nicknames.put(memberWalk.getWalk().getId(), memberWalk.getMember().getNickname());
        }
        List<Long> walkLikeIds = walkLikeRepository.findByWalksAndMemberId(walkIds, member.getId())
                .stream().map(walkLike -> walkLike.getWalk().getId()).toList();
        walks.forEach(walk ->
                responses.add(WalkConverter.toWalkSearchResponse(
                        walk,
                        nicknames.getOrDefault(walk.getId(), "알 수 없음"),
                        walkLikeIds.contains(walk.getId())
                ))
        );

        return WalkConverter.toWalkSearchListResponse(responses, cursor, hasNext);
    }
}
