package com.example.harumeonglog.domain.member.converter;

import com.example.harumeonglog.domain.member.dto.response.MemberResponse;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.global.util.S3Util;

public class MemberConverter {

    public static MemberResponse.MemberInfoResponse toMemberInfoResponse(Member member, S3Util s3Util) {
        return MemberResponse.MemberInfoResponse.builder()
                .memberId(member.getId())
                .image(s3Util.getUrlFromKey(member.getImage()))
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }

    public static MemberResponse.MemberInfoUpdateResponse toMemberInfoUpdateResponse(Member member, S3Util s3Util) {
        return MemberResponse.MemberInfoUpdateResponse.builder()
                .memberId(member.getId())
                .image(s3Util.getUrlFromKey(member.getImage()))
                .nickname(member.getNickname())
                .updatedAt(member.getUpdatedAt())
                .build();
    }

}
