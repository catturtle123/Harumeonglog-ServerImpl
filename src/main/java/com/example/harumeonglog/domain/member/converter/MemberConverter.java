package com.example.harumeonglog.domain.member.converter;

import com.example.harumeonglog.domain.member.dto.response.MemberResponse;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.dto.response.PetResponse;

public class MemberConverter {

    public static MemberResponse.MemberInfoResponse toMemberInfoResponse(Member member) {
        return MemberResponse.MemberInfoResponse.builder()
                .memberId(member.getId())
                .image(member.getImage())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }
}
