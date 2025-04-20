package com.example.harumeonglog.domain.comment.converter;

import com.example.harumeonglog.domain.comment.dto.response.CommentResponse;
import com.example.harumeonglog.domain.comment.dto.response.CommentResponse.CommentPreviewResponse;
import com.example.harumeonglog.domain.comment.entity.Comment;
import com.example.harumeonglog.domain.member.converter.MemberConverter;

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
}
