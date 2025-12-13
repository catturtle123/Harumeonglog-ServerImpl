package com.example.harumeonglog.domain.post.converter;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.post.entity.Post;
import com.example.harumeonglog.domain.post.entity.PostReport;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostReportConverter {

    public static PostReport toPostReport(Post post, Member member) {
        return PostReport.builder()
                .post(post)
                .member(member)
                .build();
    }
}
