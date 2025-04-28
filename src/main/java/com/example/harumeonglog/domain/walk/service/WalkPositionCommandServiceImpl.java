package com.example.harumeonglog.domain.walk.service;

import com.example.harumeonglog.domain.walk.converter.WalkPositionConverter;
import com.example.harumeonglog.domain.walk.entity.Track;
import com.example.harumeonglog.domain.walk.entity.WalkPosition;
import com.example.harumeonglog.domain.walk.repository.WalkPositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class WalkPositionCommandServiceImpl implements WalkPositionCommandService {

    private final WalkPositionRepository walkPositionRepository;

    @Override
    public WalkPosition createWalkPosition(Track track, Double latitude, Double longitude) {
        return walkPositionRepository.save(WalkPositionConverter.toWalkPosition(track, latitude, longitude));
    }
}
