package com.example.harumeonglog.domain.walk.service;

import com.example.harumeonglog.domain.walk.entity.Track;

public interface TrackQueryService {
    Track getTrackWithFetchedWalk(Long trackId);
}
