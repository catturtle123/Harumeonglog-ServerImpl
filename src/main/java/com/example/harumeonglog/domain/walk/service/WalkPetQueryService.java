package com.example.harumeonglog.domain.walk.service;

import com.example.harumeonglog.domain.walk.entity.WalkPet;

import java.util.List;

public interface WalkPetQueryService {
    List<WalkPet> findByWalk(Long walkId);
}
