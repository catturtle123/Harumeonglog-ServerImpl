package com.example.harumeonglog.domain.walk.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class WalkResponse {

    @Getter
    @Builder
    public static class WalkStartResponse {

        private Long walkId;
        private Long trackId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    public static class WalkPauseResponse {
        private Long walkId;
        private String status;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    public static class WalkResumeResponse {
        private Long walkId;
        private Long trackId;
        private String status;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    public static class WalkEndResponse {
        private Long walkId;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    public static class WalkShareResponse {
        private Long walkId;
        private Boolean isShared;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    public static class WalkSearchResponse {
        private Long id;
        private String title;
        private Long walkLikeNum;
        private Double distance;
        private long time;
        private String memberNickname;
        private Boolean isLike;

    }

    @Getter
    @Builder
    public static class WalkSearchListResponse {
        private List<WalkSearchResponse> items;
        private boolean hasNext;
        private Long cursor;
    }

    @Getter
    @Builder
    public static class WalkDetailResponse {
        private Long id;
        private String title;
        private Long walkLikeNum;
        private Double distance;
        private long time;
        private String memberNickname;
        private Boolean isLike;
        private List<Track> tracks;

    }

    @Getter
    @Builder
    public static class WalkLikeResponse {
        private Long walkId;
        private Long walkLikeNum;
        private LocalDateTime updatedAt;

    }

    @Getter
    @Builder
    public static class WalkAvailableMemberListResponse {
        private List<WalkAvailableMemberResponse> members;
        private int size;
    }

    @Getter
    @Builder
    public static class WalkAvailableMemberResponse {
        private Long memberId;
        private String nickname;
        private String image;

    }

    @Getter
    @Builder
    public static class WalkAvailablePetListResponse {
        private List<WalkAvailablePetInfoResponse> pets;
        private int size;
    }

    @Getter
    @Builder
    public static class WalkAvailablePetInfoResponse {
        private Long petId;
        private String name;
        private String image;
    }

    @Getter
    @Builder
    public static class PositionCreateResponse {
        private Long trackId;
        private Long positionId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    public static class WalkUpdateResponse {
        private Long walkId;
        private String title;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    public static class Track {
        private Long trackId;
        private List<Position> positions;
    }

    @Getter
    @Builder
    public static class Position {
        private Double latitude;
        private Double longitude;

    }
}
