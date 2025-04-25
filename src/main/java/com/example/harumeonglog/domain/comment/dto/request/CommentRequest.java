package com.example.harumeonglog.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

public class CommentRequest {

    @Getter
    @Builder
    public static class CommentCreateRequest {
        @Schema(maxLength = 255)
        private String content;
        private Long parentId;
    }
}
