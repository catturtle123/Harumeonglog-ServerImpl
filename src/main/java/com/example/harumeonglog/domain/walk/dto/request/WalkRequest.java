package com.example.harumeonglog.domain.walk.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

public class WalkRequest {

    @Getter
    public static class WalkStartRequest {

        private final List<Long> petId;
        private final List<Long> memberId;
        private final Double startLatitude;
        private final Double startLongitude;

        public WalkStartRequest(
                @JsonProperty("petId") List<Long> petId,
                @JsonProperty("memberId") List<Long> memberId,
                @JsonProperty("startLatitude") Double startLatitude,
                @JsonProperty("startLongitude") Double startLongitude
        ) {
            this.petId = petId;
            this.memberId = memberId;
            this.startLatitude = startLatitude;
            this.startLongitude = startLongitude;
        }
    }

    @Getter
    public static class WalkResumeRequest {
        private final Double latitude;
        private final Double longitude;

        public WalkResumeRequest(
                @JsonProperty("latitude") Double latitude,
                @JsonProperty("longitude") Double longitude
        ) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    @Getter
    public static class AvailableMemberRequest {
        private final List<Long> petId;

        public AvailableMemberRequest(
                @JsonProperty("petId") List<Long> petId
        ) {
            this.petId = petId;
        }
    }

    @Getter
    public static class PositionRequest {
        private final Double latitude;
        private final Double longitude;

        public PositionRequest(
                @JsonProperty("latitude") Double latitude,
                @JsonProperty("longitude") Double longitude
        ) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
}
