package com.example.harumeonglog.domain.post.dto.request;

import com.example.harumeonglog.domain.post.entity.enums.PostCategory;
import lombok.Builder;
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
