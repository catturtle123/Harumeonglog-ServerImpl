package com.example.harumeonglog.domain.comment.converter;

import com.example.harumeonglog.domain.comment.dto.request.CommentRequest;
import com.example.harumeonglog.domain.comment.dto.response.CommentResponse;
import com.example.harumeonglog.domain.comment.dto.response.CommentResponse.CommentPreviewResponse;
import com.example.harumeonglog.domain.comment.entity.Comment;
import com.example.harumeonglog.domain.member.converter.MemberConverter;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.post.entity.Post;

import java.util.List;
import java.util.stream.Stream;

public class CommentConverter {

    public static CommentResponse.CommentPreviewListResponse toCommentPreviewListResponse(List<Comment> commentList, Boolean hasNext, Long cursor) {

        List<CommentPreviewResponse> commentPreviewResponseStream = commentList.stream().map(CommentConverter::toCommentPreviewResponse).toList();

        return CommentResponse.CommentPreviewListResponse.builder()
                .items(commentPreviewResponseStream)
                .cursor(cursor)
                .hasNext(hasNext)
                .build();
    }

    private static CommentPreviewResponse toCommentPreviewResponse(Comment comment) {
        return CommentPreviewResponse.builder()
                .commentId(comment.getId())
                .memberInfoResponse(MemberConverter.toMemberInfoResponse(comment.getMember()))
                .content(comment.getContent())
                .build();
    }

    public static Comment toComment(CommentRequest.CommentCreateRequest commentCreateRequest, Member member) {
        return Comment.builder()
                .content(commentCreateRequest.getContent())
                .member(member)
                .build();
    }

    public static CommentResponse.CommentCreateResponse toCommentCreateResponse(Comment comment) {
        return CommentResponse.CommentCreateResponse.builder()
                .commentId(comment.getId())
                .createAt(comment.getCreatedAt())
                .updateAt(comment.getUpdatedAt())
                .build();
    }
}
