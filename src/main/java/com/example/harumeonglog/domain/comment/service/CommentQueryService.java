package com.example.harumeonglog.domain.comment.service;

import com.example.harumeonglog.domain.comment.dto.response.CommentResponse;
import com.example.harumeonglog.domain.member.entity.Member;

public interface CommentQueryService {
    CommentResponse.CommentPreviewListResponse getComments(Long postId, Long cursor, Integer size, Member member);

    CommentResponse.CommentMyPreviewListResponse getMyComments(Member member, Long cursor, Integer size);
}
