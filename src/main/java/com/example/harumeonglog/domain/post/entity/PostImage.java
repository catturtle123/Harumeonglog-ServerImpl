package com.example.harumeonglog.domain.post.entity;

import com.example.harumeonglog.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "post_image")
public class PostImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_image_id")
    private Long id;

    @Column(name = "post_image_key_name", nullable = false)
    private String postImageKeyName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    // 연관관계 편의 메서드
    public void associateWith(Post post) {
        this.post = post;
        if (!post.getPostImageList().contains(this)) {
            post.getPostImageList().add(this);
        }
    }
}
