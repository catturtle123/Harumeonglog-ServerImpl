package com.example.harumeonglog.domain.walk.service;

import com.example.harumeonglog.domain.walk.dto.request.WalkRequest;
import com.example.harumeonglog.domain.walk.dto.response.WalkResponse;
import com.example.harumeonglog.domain.walk.entity.Walk;


public interface WalkService {
    Walk createWalk(WalkRequest.WalkCreateRequest walkCreateRequest);
    Walk shareWalk(Long id);
    WalkResponse.WalkSearchListResponse getWalkList(String sort, Long cursor, int offset);
    WalkResponse.WalkDetailResponse getWalkDetails(Long walkId);
    Walk likeWalk(Long walkId);
}
