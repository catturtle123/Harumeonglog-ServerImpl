package com.example.harumeonglog.domain.walk.service;

import com.example.harumeonglog.domain.walk.entity.MemberWalk;

import java.util.List;

public interface MemberWalkQueryService {
    List<MemberWalk> findByWalk(Long walkId);
}
