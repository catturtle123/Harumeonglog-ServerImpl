package com.example.harumeonglog.domain.walk.controller.port;

import com.example.harumeonglog.domain.walk.controller.dto.request.WalkRequest;
import com.example.harumeonglog.domain.walk.controller.dto.response.WalkResponse;
import com.example.harumeonglog.domain.walk.domain.Walk;

public interface WalkService {
    Walk createWalk(WalkRequest.WalkCreateRequest walkCreateRequest);
    Walk shareWalk(Long id);
    WalkResponse.WalkSearchListResponse getWalkList(String sort, Long cursor, int offset);
    WalkResponse.WalkDetailResponse getWalkDetails(Long walkId);
    Walk likeWalk(Long walkId);
}
