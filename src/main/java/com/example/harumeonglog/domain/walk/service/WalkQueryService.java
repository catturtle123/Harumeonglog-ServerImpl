package com.example.harumeonglog.domain.walk.service;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.walk.dto.request.WalkRequest;
import com.example.harumeonglog.domain.walk.dto.response.WalkResponse;
import com.example.harumeonglog.domain.walk.entity.Walk;
import com.example.harumeonglog.domain.walk.entity.enums.WalkStatus;

public interface WalkQueryService {
    WalkResponse.WalkAvailablePetListResponse getAvailablePets(Member member);
    WalkResponse.WalkAvailableMemberListResponse getAvailableMembers(WalkRequest.AvailableMemberRequest dto);
    WalkResponse.WalkSearchListResponse getWalkList(String sort, Long cursor, int offset);
    WalkResponse.WalkDetailResponse getWalkDetails(Long walkId);

    Walk findById(Long walkId);
    boolean hasStatus(Walk walk, WalkStatus... walkStatus);
}
