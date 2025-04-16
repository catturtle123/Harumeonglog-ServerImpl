package com.example.harumeonglog.domain.walk.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class WalkResponse {

    @Getter
    @Builder
    public static class WalkCreateResponse {

        private Long walkId;


    }

    @Getter
    @Builder
    public static class WalkShareResponse {
        private Long walkId;
        private Double distance;
        private Integer time;
        private Boolean isShared;

    }

    @Getter
    @Builder
    public static class WalkSearchResponse {
        private Long id;
        private String title;
        private Long walkLikeNum;
        private double distance;
        private int time;
        private String memberNickname;
        private Boolean isLike;

    }

    @Getter
    @Builder
    public static class WalkSearchListResponse {
        private List<WalkSearchResponse> items;
        private boolean hasNext;
        private Long cursor;

        public static WalkSearchListResponse from(List<WalkSearchResponse> items, boolean hasNext) {
            return WalkSearchListResponse.builder()
                    .items(items)
                    .hasNext(hasNext)
                    .cursor(items.get(items.size() - 1).getId())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class WalkDetailResponse {
        private Long id;
        private String title;
        private Long walkLikeNum;
        private double distance;
        private int time;
        private String memberNickname;
        private Boolean isLike;
        private List<Track> tracks;

    }

    @Getter
    @Builder
    public static class WalkLikeResponse {
        private Long walkId;
        private Long walkLikeNum;

    }

    @Getter
    @Builder
    public static class Track {
        private Long trackId;
        private List<Position> positions;

        public static Track from(Track track, List<Position> positions) {
            return Track.builder()
                    .trackId(track.getTrackId())
                    .positions(positions)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class Position {
        private Double latitude;
        private Double longitude;

    }
}
