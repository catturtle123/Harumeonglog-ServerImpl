package com.example.harumeonglog.domain.comment.dto.response;

import com.example.harumeonglog.domain.member.dto.response.MemberResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class CommentResponse {

    @Getter
    @Builder
    public static class CommentPreviewListResponse {
        private List<CommentPreviewResponse> items;
        private Boolean hasNext;
        private Long cursor;
    }

    @Getter
    @Builder
    public static class CommentPreviewResponse {
        private Long commentId;
        private String content;
        private MemberResponse.MemberInfoResponse memberInfoResponse;
        private List<CommentCommentPreviewResponse> commentcommentResponseList;
    }

    @Getter
    @Builder
    public static class CommentCreateResponse {
        private Long commentId;
        private LocalDateTime createAt;
        private LocalDateTime updateAt;
    }

    @Getter
    @Builder
    public static class CommentCommentPreviewResponse {
        private Long commentId;
        private String content;
        private MemberResponse.MemberInfoResponse memberInfoResponse;
    }

    @Getter
    @Builder
    public static class CommentMyPreviewListResponse {
        private List<CommentMyPreviewResponse> items;
        private Boolean hasNext;
        private Long cursor;
    }

    @Getter
    @Builder
    public static class CommentMyPreviewResponse {
        private Long commentId;
        private String content;
        private LocalDateTime createAt;
    }
}
