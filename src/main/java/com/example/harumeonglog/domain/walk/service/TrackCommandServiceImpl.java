package com.example.harumeonglog.domain.walk.service;

import com.example.harumeonglog.domain.walk.converter.TrackConverter;
import com.example.harumeonglog.domain.walk.entity.Track;
import com.example.harumeonglog.domain.walk.entity.Walk;
import com.example.harumeonglog.domain.walk.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TrackCommandServiceImpl implements TrackCommandService {

    private final TrackRepository trackRepository;

    @Override
    public Track createNewTrack(Walk walk) {
        return trackRepository.save(TrackConverter.toTrack(walk));
    }
}
