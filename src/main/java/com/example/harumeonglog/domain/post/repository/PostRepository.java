package com.example.harumeonglog.domain.post.repository;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.post.entity.Post;
import com.example.harumeonglog.domain.post.entity.enums.PostCategory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p " +
            "from Post p join fetch p.member m " +
            "left join PostReport pr on pr.post = p and pr.member.id = :memberId " +
            "left join MemberBlock mb on mb.reporter.id = :memberId and mb.reported.id = m.id " +
            "where p.deletedAt is null " +
            "and pr.id is null " +
            "and mb.id is null " +
            "and p.content like %:content% " +
            "and p.id < :cursor " +
            "order by p.id desc")
    Slice<Post> findByContentLikeAndIdLessThanOrderByIdDesc(String content, Long memberId, Long cursor, Pageable pageable);

    @Query("select p " +
            "from Post p join fetch p.member m " +
            "left join PostReport pr on pr.post = p and pr.member.id = :memberId " +
            "left join MemberBlock mb on mb.reporter.id = :memberId and mb.reported.id = m.id " +
            "where p.category = :postCategory " +
            "and p.deletedAt is null " +
            "and pr.id is null " +
            "and mb.id is null " +
            "and p.content like %:content% " +
            "and p.id < :cursor " +
            "order by p.id desc")
    Slice<Post> findByPostCategoryAndContentLikeAndIdLessThanOrderByIdDesc(String content, Long memberId, Long cursor, PostCategory postCategory, PageRequest of);

    @Query("select p " +
            "from Post p join fetch p.member m " +
            "where p.member = :member and p.deletedAt is null and p.id < :cursor order by p.id desc")
    Slice<Post> findByMemberAndDeletedAtIsNullAndIdLessThanOrderByIdDesc(Member member, Long cursor, Pageable pageable);

    @Query("select p " +
            "from Post p " +
            "join PostLike pl on pl.post = p " +
            "join fetch p.member m " +
            "left join PostReport pr on pr.post = p and pr.member.id = :memberId " +
            "left join MemberBlock mb on mb.reporter.id = :memberId and mb.reported.id = m.id " +
            "where pl.member = :member " +
            "and p.deletedAt is null " +
            "and pr.id is null " +
            "and mb.id is null " +
            "and p.id < :cursor " +
            "order by p.id desc")
    Slice<Post> findMyLikePosts(Member member, Long memberId, Long cursor, Pageable pageable);

    @Query(value = """
        UPDATE Post p SET p.postLikeNum = p.postLikeNum + 1 WHERE p = :post
    """)
    @Modifying
    void updatePostLikeNumByPost(Post post);

    @Query(value = """
        UPDATE Post p SET p.postLikeNum = p.postLikeNum - 1 WHERE p = :post
    """)
    @Modifying
    void updatePostUnLikeNumByPost(Post post);


    @Query(value = """
        UPDATE Post p SET p.postReportNum = p.postReportNum + 1 WHERE p = :post
    """)
    @Modifying
    void updatePostReportNumByPost(Post post);

    @Query(value = """
        UPDATE Post p SET p.postReportNum = p.postReportNum - 1 WHERE p = :post
    """)
    @Modifying
    void updatePostUnReportNumByPost(Post post);

    @Query("select p " +
            "from Post p join fetch p.member " +
            "where p.deletedAt is null " +
            "and cast(p.createdAt as date) = :targetDate " +
            "order by p.postLikeNum desc, p.id desc")
    List<Post> findTop5PostsByDateAndLikes(@Param("targetDate") LocalDate targetDate, Pageable pageable);
}
