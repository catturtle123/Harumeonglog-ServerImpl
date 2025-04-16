package com.example.harumeonglog.domain.comment.dto.request;

import lombok.Builder;
import lombok.Getter;

public class CommentRequest {

    @Getter
    @Builder
    public static class CommentCreateRequest {
        private String content;
    }
}
