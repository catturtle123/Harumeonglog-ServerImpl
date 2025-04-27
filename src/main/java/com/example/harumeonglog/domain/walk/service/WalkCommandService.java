package com.example.harumeonglog.domain.walk.service;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.walk.dto.request.WalkRequest;
import com.example.harumeonglog.domain.walk.dto.response.WalkResponse;

public interface WalkCommandService {
    WalkResponse.WalkStartResponse startWalk(Member member, WalkRequest.WalkStartRequest request);
    WalkResponse.WalkPauseResponse pauseWalk(Long walkId);
    WalkResponse.WalkResumeResponse resumeWalk(Long walkId);
    WalkResponse.WalkEndResponse endWalk(Long walkId);
    WalkResponse.WalkShareResponse shareWalk(Long id);
    WalkResponse.WalkLikeResponse likeWalk(Member member, Long walkId);
}
