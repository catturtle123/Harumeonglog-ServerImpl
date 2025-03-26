package com.example.harumeonglog.domain.comment.controller.dto.response;

import com.example.harumeonglog.domain.comment.domain.Comment;
import com.example.harumeonglog.domain.member.controller.dto.response.MemberResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class CommentResponse {

    @Getter
    @Builder
    public static class CommentListResponse {
        private List<CommentPreviewResponse> items;
        private Boolean hasNext;
        private Long cursor;

        public static CommentListResponse from(Long cursor, Boolean hasNext, List<Comment> items) {
            return CommentListResponse.builder()
                    .items(items.stream().map(CommentPreviewResponse::from).toList())
                    .cursor(cursor)
                    .hasNext(hasNext)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class CommentPreviewResponse {
        private Long commentId;
        private String content;
        private MemberResponse.MemberInfoResponse memberInfoResponse;

        public static CommentPreviewResponse from(Comment comment) {
            return CommentPreviewResponse.builder()
                    .commentId(comment.getId())
                    .content(comment.getContent())
                    .memberInfoResponse(MemberResponse.MemberInfoResponse.from(comment.getMember()))
                    .build();
        }
    }
}
