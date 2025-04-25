package com.example.harumeonglog.domain.comment.converter;

import com.example.harumeonglog.domain.comment.dto.request.CommentRequest;
import com.example.harumeonglog.domain.comment.dto.response.CommentResponse;
import com.example.harumeonglog.domain.comment.dto.response.CommentResponse.CommentCommentPreviewResponse;
import com.example.harumeonglog.domain.comment.dto.response.CommentResponse.CommentPreviewResponse;
import com.example.harumeonglog.domain.comment.entity.Comment;
import com.example.harumeonglog.domain.member.converter.MemberConverter;
import com.example.harumeonglog.domain.member.entity.Member;

import java.util.List;

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

        List<CommentCommentPreviewResponse> commentcommentReponseList = comment.getCommentList().stream().map(CommentConverter::toCommentCommentPreviewResponse).toList();

        return CommentPreviewResponse.builder()
                .commentId(comment.getId())
                .memberInfoResponse(MemberConverter.toMemberInfoResponse(comment.getMember()))
                .content(comment.getContent())
                .commentcommentResponseList(commentcommentReponseList)
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

    private static CommentCommentPreviewResponse toCommentCommentPreviewResponse(Comment comment) {

        return CommentCommentPreviewResponse.builder()
                .commentId(comment.getId())
                .memberInfoResponse(MemberConverter.toMemberInfoResponse(comment.getMember()))
                .content(comment.getContent())
                .build();
    }
}
