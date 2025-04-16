package com.example.harumeonglog.domain.post.entity;

import com.example.harumeonglog.domain.comment.entity.Comment;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.global.common.entity.BaseEntity;
import com.example.harumeonglog.domain.post.entity.enums.PostCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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
    private Long postLikeNum;

    @Column(name = "content")
    private String content;

    @Column(name = "post_report_num", nullable = false)
    private Long postReportNum;

    @Column(name = "comment_num", nullable = false)
    private Long commentNum;

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private PostCategory category;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> postImageList;

    // 비즈니스 함수
    public void update(String content, PostCategory category, List<PostImage> postImageList) {
        this.content = content;
        this.category = category;
        this.postImageList = postImageList;
    }

    public void fixLikeNum(Long number) {
        this.postLikeNum += number;
    }

    public void fixReportNum(Long number) {
        this.postReportNum += number;
    }
}
