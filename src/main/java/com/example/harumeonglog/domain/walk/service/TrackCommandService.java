package com.example.harumeonglog.domain.walk.service;

import com.example.harumeonglog.domain.walk.entity.Track;
import com.example.harumeonglog.domain.walk.entity.Walk;

public interface TrackCommandService {
    Track createNewTrack(Walk walk);
}
