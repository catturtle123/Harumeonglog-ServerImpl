package com.example.harumeonglog.domain.walk.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

public class WalkRequest {

    @Getter
    public static class WalkCreateRequest {

        private final List<Long> petId;
        private final List<Long> memberId;
        private final String title;
        private final Double startLatitude;
        private final Double startLongitude;
        private final Double distance;
        private final Integer time;
        private final List<Track> tracks;

        public WalkCreateRequest(
                @JsonProperty("petId") List<Long> petId,
                @JsonProperty("memberId") List<Long> memberId,
                @JsonProperty("title") String title,
                @JsonProperty("startLatitude") Double startLatitude,
                @JsonProperty("startLongitude") Double startLongitude,
                @JsonProperty("distance") Double distance,
                @JsonProperty("time") Integer time,
                @JsonProperty("tracks") List<Track> tracks
        ) {
            this.petId = petId;
            this.memberId = memberId;
            this.title = title;
            this.startLatitude = startLatitude;
            this.startLongitude = startLongitude;
            this.distance = distance;
            this.time = time;
            this.tracks = tracks;
        }
    }

    @Getter
    public static class Track {
        private final List<Position> positions;

        public Track(
                @JsonProperty("positions") List<Position> positions
        ) {
            this.positions = positions;
        }
    }

    @Getter
    public static class Position {
        private final Double latitude;
        private final Double longitude;

        public Position(
                @JsonProperty("latitude") Double latitude,
                @JsonProperty("longitude") Double longitude
        ) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
}
