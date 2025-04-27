package com.example.harumeonglog.domain.walk.converter;

import com.example.harumeonglog.domain.walk.entity.Track;
import com.example.harumeonglog.domain.walk.entity.Walk;

public class TrackConverter {

    public static Track toTrack(Walk walk) {
        return Track.builder()
                .walk(walk)
                .build();
    }
}
