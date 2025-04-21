package com.example.harumeonglog.domain.comment.service;

import com.example.harumeonglog.domain.comment.dto.request.CommentRequest;
import com.example.harumeonglog.domain.comment.dto.response.CommentResponse;
import com.example.harumeonglog.domain.comment.entity.Comment;
import com.example.harumeonglog.domain.member.entity.Member;
import org.springframework.data.domain.Slice;

public interface CommentCommandService {
    void reportComment(Long commentId, Member member);

    void blockComment(Long commentId, Member member);

    CommentResponse.CommentCreateResponse createComment(CommentRequest.CommentCreateRequest commentCreateRequest, Long postId, Member member);

    void deleteComment(Long commentId, Member member);

    void likeComment(Long commentId, Member member);
}
