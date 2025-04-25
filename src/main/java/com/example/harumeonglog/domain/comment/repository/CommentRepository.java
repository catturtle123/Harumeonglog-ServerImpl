package com.example.harumeonglog.domain.comment.repository;

import com.example.harumeonglog.domain.comment.entity.Comment;
import com.example.harumeonglog.domain.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c " +
            "from Comment c join fetch c.member m " +
            "where c.post = :post and c.parent is null and c.id < :cursor order by c.id desc")
    Slice<Comment> findCommentSliceByPost(Post post, Long cursor, Pageable pageable);

}
