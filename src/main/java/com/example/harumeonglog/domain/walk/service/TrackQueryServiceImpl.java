package com.example.harumeonglog.domain.walk.service;

import com.example.harumeonglog.domain.walk.entity.Track;
import com.example.harumeonglog.domain.walk.repository.TrackRepository;
import com.example.harumeonglog.global.error.code.TrackErrorCode;
import com.example.harumeonglog.global.error.exception.TrackException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrackQueryServiceImpl implements TrackQueryService {

    private final TrackRepository trackRepository;

    @Override
    public Track getTrackWithFetchedWalk(Long trackId) {
        return trackRepository.findWithWalk(trackId).orElseThrow(() ->
                new TrackException(TrackErrorCode.NOT_FOUND));
    }
}
