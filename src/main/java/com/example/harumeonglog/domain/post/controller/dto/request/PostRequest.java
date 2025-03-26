package com.example.harumeonglog.domain.post.controller.dto.request;

import com.example.harumeonglog.domain.post.controller.dto.enums.PostCategory;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class PostRequest {

    @Getter
    @Builder
    public static class PostCreateRequest {
        private PostCategory postCategory;
        private String content;
        private List<String> postImageList;
    }

    @Getter
    @Builder
    public static class PostUpdateRequest {
        private PostCategory postCategory;
        private String content;
        private List<String> postImageList;
    }
}
