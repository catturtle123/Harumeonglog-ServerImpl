package com.example.harumeonglog.domain.walk.service;

import com.example.harumeonglog.domain.walk.entity.Track;
import com.example.harumeonglog.domain.walk.entity.WalkPosition;
import com.example.harumeonglog.domain.walk.repository.WalkPositionRepository;
import com.example.harumeonglog.global.error.code.WalkErrorCode;
import com.example.harumeonglog.global.error.exception.WalkException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalkPositionQueryServiceImpl implements WalkPositionQueryService {

    private final WalkPositionRepository walkPositionRepository;

    @Override
    public WalkPosition getLastPosition(Track track) {
        return walkPositionRepository.findTopByTrackOrderByCreatedAtDesc(track).orElseThrow(() ->
                new WalkException(WalkErrorCode.CANNOT_FIND_START_POSITION));
    }
}
