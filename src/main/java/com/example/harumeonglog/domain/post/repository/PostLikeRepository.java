package com.example.harumeonglog.domain.post.repository;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.post.entity.Post;
import com.example.harumeonglog.domain.post.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostAndMember(Post post, Member member);

    Boolean existsByPostAndMember(Post post, Member member);

    List<PostLike> findByPostInAndMember(List<Post> post, Member member);
}
