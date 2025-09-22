package com.example.harumeonglog.domain.member.converter;

import com.example.harumeonglog.domain.member.dto.response.MemberBlockResponse;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.MemberBlock;

public class MemberBlockConverter {

    public static MemberBlock toEntity(Member reporter, Member reported) {
        return MemberBlock.builder()
                .reporter(reporter)
                .reported(reported)
                .build();
    }

    public static MemberBlockResponse.MemberBlockInfoResponse toMemberBlockInfoResponse(Long reporterId, Long reportedId, Boolean isBlock) {
        return MemberBlockResponse.MemberBlockInfoResponse.builder()
                .reporterId(reporterId)
                .reportedId(reportedId)
                .isBlock(isBlock)
                .build();
    }
}
