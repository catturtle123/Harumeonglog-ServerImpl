package com.example.harumeonglog.domain.post.repository;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.post.controller.enums.PostRequestCategory;
import com.example.harumeonglog.domain.post.entity.Post;
import com.example.harumeonglog.domain.post.entity.enums.PostCategory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {

    Slice<Post> findByContentLikeAndIdLessThanOrderByIdDesc(String content, Long id, Pageable pageable);

    @Query("select p from Post p where p.category = :postcategory and p.content like :search and p.id < :cursor order by p.id desc")
    Slice<Post> findByPostCategoryAndContentLikeAndIdLessThanOrderByIdDesc(String search, Long cursor, PostCategory postCategory, PageRequest of);

    Slice<Post> findByMemberAndIdLessThanOrderByIdDesc(Member member, Long id, Pageable pageable);

    @Query("select p from Post p join PostLike pl on pl.post = p where pl.member = :member and p.id < :cursor order by p.id desc")
    Slice<Post> findMyLikePosts(Member member, Long cursor, Pageable pageable);
}
