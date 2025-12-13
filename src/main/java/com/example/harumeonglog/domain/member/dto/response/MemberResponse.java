package com.example.harumeonglog.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class MemberResponse {

    @Getter
    @Builder
    public static class MemberInfoResponse {
        private Long memberId;
        private String email;
        private String nickname;
        private String image;
    }

    @Getter
    @Builder
    public static class MemberInfoUpdateResponse {
        private Long memberId;
        private String image;
        private String nickname;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    public static class MemberTermsInfoResponse {
        private Long memberId;
        private Boolean didAgree;
    }

    @Getter
    @Builder
    public static class MemberTermsUpdateResponse {
        private Long memberId;
        private Boolean didAgree;
    }
}
