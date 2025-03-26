package com.example.harumeonglog.domain.event.controller.dto.response;

import com.example.harumeonglog.domain.event.domain.Event;
import com.example.harumeonglog.domain.event.domain.enums.EventCategory;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

public class EventResponse {
    @Getter
    @Builder
    public static class EventCreateResponse {
        private Long eventId;
        public static EventCreateResponse from(Event event) {
            return EventCreateResponse.builder()
                    .eventId(event.getId())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class EventUpdateResponse {
        private Long eventId;
        private String title;
        private String details;
        private LocalDate date;
        private Boolean isRepeated;
        private LocalDate expiredDate;
        private Boolean hasNotice;
        private EventCategory category;
        public static EventUpdateResponse from(Event event) {
            return EventUpdateResponse.builder()
                    .eventId(event.getId())
                    .title(event.getTitle())
                    .details(event.getDetails())
                    .date(event.getDate())
                    .isRepeated(event.getIsRepeated())
                    .expiredDate(event.getExpiredDate())
                    .hasNotice(event.getHasNotice())
                    .category(event.getCategory())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class EventListResponse {
        private List<EventDetailResponse> events;
    }

    @Getter
    @Builder
    public static class EventDetailResponse {
        private Long id;
        private String title;
        private String details;
        private LocalDate date;
        private Boolean isRepeated;
        private LocalDate expiredDate;
        private Boolean hasNotice;
        private EventCategory category;
        public static EventDetailResponse from(Event event) {
            return EventDetailResponse.builder()
                    .id(event.getId())
                    .title(event.getTitle())
                    .details(event.getDetails())
                    .date(event.getDate())
                    .isRepeated(event.getIsRepeated())
                    .expiredDate(event.getExpiredDate())
                    .hasNotice(event.getHasNotice())
                    .category(event.getCategory())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class EventCompleteResponse {
        private Long eventId;

        public static EventCompleteResponse from(Event event) {
            return EventCompleteResponse.builder()
                    .eventId(event.getId())
                    .build();
        }
    }
}
