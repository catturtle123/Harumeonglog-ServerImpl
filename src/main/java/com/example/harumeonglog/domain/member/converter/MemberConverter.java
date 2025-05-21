package com.example.harumeonglog.domain.member.converter;

import com.example.harumeonglog.domain.member.dto.response.MemberResponse;
import com.example.harumeonglog.domain.member.entity.Member;

public class MemberConverter {

    public static MemberResponse.MemberInfoResponse toMemberInfoResponse(Member member, String imageUrl) {
        return MemberResponse.MemberInfoResponse.builder()
                .memberId(member.getId())
                .image(imageUrl)
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }

    public static MemberResponse.MemberInfoUpdateResponse toMemberInfoUpdateResponse(Member member, String imageUrl) {
        return MemberResponse.MemberInfoUpdateResponse.builder()
                .memberId(member.getId())
                .image(imageUrl)
                .nickname(member.getNickname())
                .updatedAt(member.getUpdatedAt())
                .build();
    }

}
