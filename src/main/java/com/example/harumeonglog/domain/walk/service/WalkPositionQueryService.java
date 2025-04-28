package com.example.harumeonglog.domain.walk.service;

import com.example.harumeonglog.domain.walk.entity.Track;
import com.example.harumeonglog.domain.walk.entity.WalkPosition;

public interface WalkPositionQueryService {
    WalkPosition getLastPosition(Track track);
}
