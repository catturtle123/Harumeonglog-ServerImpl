package com.example.harumeonglog.domain.event.controller.dto.request;

import com.example.harumeonglog.domain.event.domain.enums.EventCategory;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;

public class EventRequest {

    @Getter
    public static class EventCreateRequest {

        private final String title;
        private final String details;
        private final LocalDate date;
        private final Boolean isRepeated;
        private final LocalDate expiredDate;
        private final Boolean hasNotice;
        private final EventCategory category;

        public EventCreateRequest(
                @JsonProperty("title") String title,
                @JsonProperty("details") String details,
                @JsonProperty("date") LocalDate date,
                @JsonProperty("isRepeated") Boolean isRepeated,
                @JsonProperty("expiredDate") LocalDate expiredDate,
                @JsonProperty("hasNotice") Boolean hasNotice,
                @JsonProperty("category") EventCategory category
        ) {
            this.title = title;
            this.details = details;
            this.date = date;
            this.isRepeated = isRepeated;
            this.expiredDate = expiredDate;
            this.hasNotice = hasNotice;
            this.category = category;
        }
    }

    @Getter
    public static class EventUpdateRequest {

        private final String title;
        private final String details;
        private final LocalDate date;
        private final Boolean isRepeated;
        private final LocalDate expiredDate;
        private final Boolean hasNotice;
        private final EventCategory category;

        public EventUpdateRequest(
                @JsonProperty("title") String title,
                @JsonProperty("details") String details,
                @JsonProperty("date") LocalDate date,
                @JsonProperty("isRepeated") Boolean isRepeated,
                @JsonProperty("expiredDate") LocalDate expiredDate,
                @JsonProperty("hasNotice") Boolean hasNotice,
                @JsonProperty("category") EventCategory category
        ) {
            this.title = title;
            this.details = details;
            this.date = date;
            this.isRepeated = isRepeated;
            this.expiredDate = expiredDate;
            this.hasNotice = hasNotice;
            this.category = category;
        }
    }
}
