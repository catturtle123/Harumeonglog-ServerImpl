package com.example.harumeonglog.domain.walk.converter;

import com.example.harumeonglog.domain.walk.entity.Track;
import com.example.harumeonglog.domain.walk.entity.WalkPosition;

public class WalkPositionConverter {

    public static WalkPosition toWalkPosition(Track track, Double latitude, Double longitude) {
        return WalkPosition.builder()
                .track(track)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
