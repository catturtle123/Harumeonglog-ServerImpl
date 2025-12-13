package com.example.harumeonglog.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class InvitationResponse {

    @Getter
    @Builder
    public static class InviteResponse {
        private Long invitationId;
        private String image;
        private Long petId;
        private String petName;
        private String role;
        private Long senderId;
        private String senderName;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    public static class InvitationListResponse {
        private List<InviteResponse> invitations;
        private Integer size;
        private boolean hasNext;
        private Long nextCursor;
    }
}
