package com.example.harumeonglog.domain.comment.service;

import com.example.harumeonglog.domain.comment.dto.request.CommentRequest;
import com.example.harumeonglog.domain.comment.entity.Comment;
import com.example.harumeonglog.domain.member.entity.Member;
import org.springframework.data.domain.Slice;

public interface CommentCommandService {
    void reportComment(Long commentId);

    void blockComment(Long commentId);

    Comment createComment(CommentRequest.CommentCreateRequest commentCreateRequest);

    void deleteComment(Long commentId, Member member);

    void likeComment(Long commentId);
}
