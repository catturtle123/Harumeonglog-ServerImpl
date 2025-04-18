package com.example.harumeonglog.domain.comment.service;

import com.example.harumeonglog.domain.comment.entity.Comment;
import org.springframework.data.domain.Slice;

public interface CommentQueryService {
    Slice<Comment> getComments(Long postId, Integer cursor, Integer size);
}
