package com.example.harumeonglog.domain.comment.repository;

import com.example.harumeonglog.domain.comment.entity.Comment;
import com.example.harumeonglog.domain.comment.entity.CommentBlock;
import com.example.harumeonglog.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentBlockRepository extends JpaRepository<CommentBlock, Long> {
    CommentBlock findByCommentAndMember(Comment comment, Member member);
}
