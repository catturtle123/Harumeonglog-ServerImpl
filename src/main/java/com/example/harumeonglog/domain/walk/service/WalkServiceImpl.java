package com.example.harumeonglog.domain.walk.service;

import com.example.harumeonglog.domain.walk.controller.dto.request.WalkRequest;
import com.example.harumeonglog.domain.walk.controller.dto.response.WalkResponse;
import com.example.harumeonglog.domain.walk.controller.port.WalkService;
import com.example.harumeonglog.domain.walk.domain.Walk;
import org.springframework.stereotype.Service;

@Service
public class WalkServiceImpl implements WalkService {
    @Override
    public Walk createWalk(WalkRequest.WalkCreateRequest walkCreateRequest) {
        return null;
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
    public Walk shareWalk(Long id) {
        return null;
    }

    @Override
    public Walk likeWalk(Long walkId) {
        return null;
    }
}
