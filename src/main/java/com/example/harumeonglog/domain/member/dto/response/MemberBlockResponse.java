package com.example.harumeonglog.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;

public class MemberBlockResponse {

    @Getter
    @Builder
    public static class MemberBlockInfoResponse {
        private Long reporterId;
        private Long reportedId;
        private Boolean isBlock;
    }
}
