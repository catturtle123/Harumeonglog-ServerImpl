package com.example.harumeonglog.domain.comment.converter;

import com.example.harumeonglog.domain.comment.dto.request.CommentRequest;
import com.example.harumeonglog.domain.comment.dto.response.CommentResponse;
import com.example.harumeonglog.domain.comment.dto.response.CommentResponse.CommentCommentPreviewResponse;
import com.example.harumeonglog.domain.comment.dto.response.CommentResponse.CommentMyPreviewListResponse;
import com.example.harumeonglog.domain.comment.dto.response.CommentResponse.CommentMyPreviewResponse;
import com.example.harumeonglog.domain.comment.dto.response.CommentResponse.CommentPreviewResponse;
import com.example.harumeonglog.domain.comment.entity.Comment;
import com.example.harumeonglog.domain.member.converter.MemberConverter;
import com.example.harumeonglog.domain.member.entity.Member;

import java.util.List;
import java.util.Set;

public class CommentConverter {

    public static CommentResponse.CommentPreviewListResponse toCommentPreviewListResponse(List<CommentPreviewResponse> commentList, Boolean hasNext, Long cursor) {

        return CommentResponse.CommentPreviewListResponse.builder()
                .items(commentList)
                .cursor(cursor)
                .hasNext(hasNext)
                .build();
    }

    public static CommentPreviewResponse toCommentPreviewResponse(Comment comment, Set<Long> isBlocked) {

        List<CommentCommentPreviewResponse> commentcommentReponseList = comment.getCommentList().stream().map((c)->toCommentCommentPreviewResponse(c, isBlocked)).toList();

        return CommentPreviewResponse.builder()
                .commentId(comment.getId())
                .memberInfoResponse(MemberConverter.toMemberInfoResponse(comment.getMember()))
                .content(isBlockOrDeletedComment(comment, isBlocked))
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

    private static CommentCommentPreviewResponse toCommentCommentPreviewResponse(Comment comment, Set<Long> isBlocked) {

        return CommentCommentPreviewResponse.builder()
                .commentId(comment.getId())
                .memberInfoResponse(MemberConverter.toMemberInfoResponse(comment.getMember()))
                .content(isBlockOrDeletedComment(comment, isBlocked))
                .build();
    }

    public static CommentMyPreviewListResponse toCommentMyPreviewListResponse(List<CommentMyPreviewResponse> commentList, Boolean hasNext, Long cursor) {
        return CommentResponse.CommentMyPreviewListResponse.builder()
                .items(commentList)
                .cursor(cursor)
                .hasNext(hasNext)
                .build();
    }


    public static CommentMyPreviewResponse toCommentMyPreviewResponse(Comment comment) {
        return CommentMyPreviewResponse.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .createAt(comment.getCreatedAt())
                .build();
    }

    private static String isBlockOrDeletedComment(Comment comment, Set<Long> isBlocked) {
        return comment.getDeletedAt() != null || isBlocked.contains(comment.getId()) ? "차단 혹은 삭제된 댓글 입니다.": comment.getContent();
    }
}
