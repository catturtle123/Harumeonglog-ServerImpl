package com.example.harumeonglog.domain.comment.domain;

import com.example.harumeonglog.domain.member.domain.Member;
import com.example.harumeonglog.domain.post.domain.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Comment {

    private Long id;

    private String content;

    private Long commentLikeNum;

    private Long commentReportNum;

    private LocalDateTime deletedAt;

    private Member member;

    private Post post;

    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;
}
