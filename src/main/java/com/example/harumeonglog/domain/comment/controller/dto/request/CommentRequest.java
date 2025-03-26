package com.example.harumeonglog.domain.comment.controller.dto.request;

import com.example.harumeonglog.domain.comment.controller.dto.response.CommentResponse;
import com.example.harumeonglog.domain.member.controller.dto.response.MemberResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class CommentRequest {

    @Getter
    @Builder
    public static class CommentCreateRequest {
        private String content;
    }
}
