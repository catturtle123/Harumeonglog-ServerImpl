package com.example.harumeonglog.domain.comment.service;

import com.example.harumeonglog.domain.comment.dto.response.CommentResponse;

public interface CommentQueryService {
    CommentResponse.CommentPreviewListResponse getComments(Long postId, Long cursor, Integer size);
}
