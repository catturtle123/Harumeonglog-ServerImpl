package com.example.harumeonglog.domain.walk.controller.dto.response;

import com.example.harumeonglog.domain.member.domain.Member;
import com.example.harumeonglog.domain.walk.domain.Walk;
import com.example.harumeonglog.domain.walk.domain.WalkPosition;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class WalkResponse {

    @Getter
    @Builder
    public static class WalkCreateResponse {

        private Long walkId;


        public static WalkCreateResponse from(Walk walk) {
            return WalkCreateResponse.builder()
                    .walkId(walk.getId())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class WalkShareResponse {
        private Long walkId;
        private Double distance;
        private Integer time;
        private Boolean isShared;

        public static WalkShareResponse from(Walk walk) {
            return WalkShareResponse.builder()
                    .walkId(walk.getId())
                    .distance(walk.getDistance())
                    .time(walk.getTime())
                    .isShared(walk.getIsShared())
                    .build();
        }
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

        public static WalkSearchResponse from(Walk walk, Member member, boolean isLike) {
            return WalkSearchResponse.builder()
                    .id(walk.getId())
                    .title(walk.getTitle())
                    .walkLikeNum(walk.getWalkLikeNum())
                    .distance(walk.getDistance())
                    .time(walk.getTime())
                    .memberNickname(member.getNickname())
                    .isLike(isLike)
                    .build();
        }
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

        public static WalkDetailResponse from(Walk walk, Member member, boolean isLke, List<Track> tracks) {
            return WalkDetailResponse.builder()
                    .id(walk.getId())
                    .title(walk.getTitle())
                    .walkLikeNum(walk.getWalkLikeNum())
                    .distance(walk.getDistance())
                    .time(walk.getTime())
                    .memberNickname(member.getNickname())
                    .isLike(isLke)
                    .tracks(tracks)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class WalkLikeResponse {
        private Long walkId;
        private Long walkLikeNum;

        public static WalkLikeResponse from(Walk walk) {
            return WalkLikeResponse.builder()
                    .walkId(walk.getId())
                    .walkLikeNum(walk.getWalkLikeNum())
                    .build();
        }
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

        public static Position from(WalkPosition walkPosition) {
            return Position.builder()
                    .latitude(walkPosition.getLatitude())
                    .longitude(walkPosition.getLongitude())
                    .build();
        }
    }
}
