package com.example.harumeonglog.domain.member.dto.request;

import lombok.Builder;
import lombok.Getter;

public class MemberBlockRequest {

    @Getter
    @Builder
    public static class UpdateMemberBlockRequest {
        private final Long reportedId;
    }
}
