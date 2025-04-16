package com.example.harumeonglog.domain.comment.service;

import com.example.harumeonglog.domain.comment.dto.request.CommentRequest;
import com.example.harumeonglog.domain.comment.entity.Comment;
import lombok.Builder;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@Builder
public class CommentServiceImpl implements CommentService {
    @Override
    public Slice<Comment> getComments(Long postId, Integer cursor, Integer size) {
        return null;
    }

    @Override
    public void reportComment(Long commentId) {

    }

    @Override
    public void blockComment(Long commentId) {

    }

    @Override
    public Comment createComment(CommentRequest.CommentCreateRequest commentCreateRequest) {
        return null;
    }

    @Override
    public void deleteComment(Long commentId) {

    }

    @Override
    public void likeComment(Long commentId) {

    }
}
