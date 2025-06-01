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

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p " +
            "from Post p join fetch p.member m " +
            "where p.deletedAt is null and p.content like %:content% and p.id < :cursor order by p.id desc")
    Slice<Post> findByContentLikeAndIdLessThanOrderByIdDesc(String content, Long cursor, Pageable pageable);

    @Query("select p " +
            "from Post p join fetch p.member m " +
            "where p.category = :postCategory and p.deletedAt is null and p.content like %:content% and p.id < :cursor order by p.id desc")
    Slice<Post> findByPostCategoryAndContentLikeAndIdLessThanOrderByIdDesc(String content, Long cursor, PostCategory postCategory, PageRequest of);

    @Query("select p " +
            "from Post p join fetch p.member m " +
            "where p.member = :member and p.id < :cursor order by p.id desc")
    Slice<Post> findByMemberAndDeletedAtIsNullAndIdLessThanOrderByIdDesc(Member member, Long cursor, Pageable pageable);

    @Query("select p " +
            "from Post p join PostLike pl on pl.post = p join fetch p.member m " +
            "where pl.member = :member and p.deletedAt is null and p.id < :cursor order by p.id desc")
    Slice<Post> findMyLikePosts(Member member, Long cursor, Pageable pageable);

    @Query(value = """
        SELECT * FROM (
            SELECT *, ROW_NUMBER() OVER (PARTITION BY category ORDER BY created_at DESC) as rn
            FROM post
        ) AS ranked
        WHERE rn = 1
    """, nativeQuery = true)
    List<Post> findFirstPostsByAllCategory();
}
