package com.example.harumeonglog.domain.comment.service;

import com.example.harumeonglog.domain.comment.dto.request.CommentRequest;
import com.example.harumeonglog.domain.comment.entity.Comment;
import org.springframework.data.domain.Slice;

public interface CommentService {
    Slice<Comment> getComments(Long postId, Integer cursor, Integer size);

    void reportComment(Long commentId);

    void blockComment(Long commentId);

    Comment createComment(CommentRequest.CommentCreateRequest commentCreateRequest);

    void deleteComment(Long commentId);

    void likeComment(Long commentId);
}
