package com.example.harumeonglog.domain.post.entity;

import com.example.harumeonglog.domain.comment.entity.Comment;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.global.common.entity.BaseEntity;
import com.example.harumeonglog.domain.post.entity.enums.PostCategory;
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
@Table(name = "post")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(name = "post_like_num", nullable = false)
    @Builder.Default
    private Long postLikeNum = 0L;

    @Column(name = "content")
    private String content;

    @Column(name = "post_report_num", nullable = false)
    @Builder.Default
    private Long postReportNum = 0L;

    @Column(name = "comment_num", nullable = false)
    @Builder.Default
    private Long commentNum = 0L;

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private PostCategory category;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PostImage> postImageList = new ArrayList<>();

    // 비즈니스 함수
    public void update(String content, PostCategory category, List<PostImage> postImageList) {
        this.content = content;
        this.category = category;

        this.postImageList.clear();
        for (PostImage postImage : postImageList) {
            this.addPostImage(postImage);
        }
    }

    public void fixLikeNum(Long number) {
        this.postLikeNum += number;
    }

    public void fixReportNum(Long number) {
        this.postReportNum += number;
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    // 연관 관계 편의 메서드
    public void addPostImage(PostImage postImage) {
        postImage.addPost(this);
        this.postImageList.add(postImage);
    }

    // 연관관계 편의 메서드
    public void addComment(Comment comment) {
        comment.addPost(this);
        commentList.add(comment);
    }
}
