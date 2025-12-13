package com.example.harumeonglog.domain.post.converter;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.post.entity.Post;
import com.example.harumeonglog.domain.post.entity.PostLike;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostLikeConverter {

    public static PostLike toPostLike(Post post, Member member) {
        return PostLike.builder()
                .member(member)
                .post(post)
                .build();
    }
}
