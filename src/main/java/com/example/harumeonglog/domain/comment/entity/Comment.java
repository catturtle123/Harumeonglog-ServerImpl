package com.example.harumeonglog.domain.comment.entity;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.post.entity.Post;
import com.example.harumeonglog.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "comment")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "comment_like_num")
    @Builder.Default
    private Long commentLikeNum = 0L;

    @Column(name = "comment_report_num")
    @Builder.Default
    private Long commentReportNum = 0L;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> commentList = new ArrayList<>();

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void fixReportNum(Long number) {
        this.commentReportNum += number;
    }

    public void fixLikeNum(Long number) {
        this.commentLikeNum += number;
    }

    public void addPost(Post post) {
        this.post = post;
        post.getCommentList().add(this);
    }

    public void addCommentComment(Comment parent, Post post) {
        parent.commentList.add(this);
        this.parent = parent;
        post.getCommentList().add(this);
        this.post = post;
    }
}
