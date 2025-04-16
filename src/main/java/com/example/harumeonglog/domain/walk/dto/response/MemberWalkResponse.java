package com.example.harumeonglog.domain.walk.dto.response;


import com.example.harumeonglog.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class MemberWalkResponse {

    @Getter
    @Builder
    public static class WalkMemberListResponse {
        private List<WalkMemberResponse> members;
        private int size;

        public static WalkMemberListResponse from(List<Member> members) {
            return WalkMemberListResponse.builder()
                    .members(members.stream().map(WalkMemberResponse::from).toList())
                    .size(members.size())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class WalkMemberResponse {
        private Long memberId;
        private String nickname;
        private String image;

        public static WalkMemberResponse from(Member member) {
            return WalkMemberResponse.builder()
                    .memberId(member.getId())
                    .nickname(member.getNickname())
                    .image(member.getImage())
                    .build();
        }
    }
}
