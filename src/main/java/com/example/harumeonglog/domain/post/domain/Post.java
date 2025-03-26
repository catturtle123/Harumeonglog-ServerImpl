package com.example.harumeonglog.domain.post.domain;

import com.example.harumeonglog.domain.comment.domain.Comment;
import com.example.harumeonglog.domain.member.domain.Member;
import com.example.harumeonglog.domain.post.domain.enums.PostCategory;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class Post {

    private Long id;

    private Long postLikeNum;

    private String content;

    private Long postReportNum;

    private Long commentNum;

    private PostCategory category;

    private LocalDateTime deletedAt;

    private Member member;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<Comment> commentList;

    private List<PostImage> postImageList;
}
