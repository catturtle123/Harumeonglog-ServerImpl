package com.example.harumeonglog.domain.walk.service;

import com.example.harumeonglog.domain.walk.entity.Track;
import com.example.harumeonglog.domain.walk.entity.WalkPosition;

public interface WalkPositionCommandService {
    WalkPosition createWalkPosition(Track track, Double latitude, Double longitude);
}
