package com.example.harumeonglog.domain.post.controller.dto.request;

import com.example.harumeonglog.domain.post.controller.dto.enums.PostCategory;
import lombok.Getter;

import java.util.List;

public class PostRequest {

    @Getter
    public static class PostCreateRequest {
        private PostCategory postCategory;
        private String content;
        private List<String> postImageList;
    }

    @Getter
    public static class PostUpdateRequest {
        private PostCategory postCategory;
        private String content;
        private List<String> postImageList;
    }
}
