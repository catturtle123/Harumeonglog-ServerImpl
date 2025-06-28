package com.example.harumeonglog.domain.walk.service;

import com.example.harumeonglog.domain.walk.entity.Walk;
import com.example.harumeonglog.domain.walk.entity.WalkPet;
import com.example.harumeonglog.domain.walk.repository.WalkPetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WalkPetQueryServiceImpl implements WalkPetQueryService {

    private final WalkPetRepository walkPetRepository;

    @Override
    public List<WalkPet> findByWalk(Long walkId) {
        return walkPetRepository.findByWalk(walkId);
    }
}
