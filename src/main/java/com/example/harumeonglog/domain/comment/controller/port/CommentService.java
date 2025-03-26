package com.example.harumeonglog.domain.comment.controller.port;

import com.example.harumeonglog.domain.comment.controller.dto.request.CommentRequest;
import com.example.harumeonglog.domain.comment.domain.Comment;
import org.springframework.data.domain.Slice;

public interface CommentService {
    Slice<Comment> getComments(Long postId, Integer cursor, Integer size);

    void reportComment(Long commentId);

    void blockComment(Long commentId);

    Comment createComment(CommentRequest.CommentCreateRequest commentCreateRequest);

    void deleteComment(Long commentId);
}
