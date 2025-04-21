package com.example.harumeonglog.domain.member.converter;

import com.example.harumeonglog.domain.member.dto.response.MemberResponse;
import com.example.harumeonglog.domain.member.entity.Member;

public class MemberConverter {

    public static MemberResponse.MemberInfoResponse toMemberInfoResponse(Member member) {
        return MemberResponse.MemberInfoResponse.builder()
                .memberId(member.getId())
                .image(member.getImage())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }

    public static MemberResponse.MemberInfoUpdateResponse toMemberInfoUpdateResponse(Member member) {
        return MemberResponse.MemberInfoUpdateResponse.builder()
                .memberId(member.getId())
                .image(member.getImage())
                .nickname(member.getNickname())
                .birth(member.getBirth())
                .updatedAt(member.getUpdatedAt())
                .build();
    }

}
