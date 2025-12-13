package com.example.harumeonglog.domain.post.dto.request;

import com.example.harumeonglog.domain.post.entity.enums.PostCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class PostRequest {

    @Getter
    @Builder
    public static class PostCreateRequest {
        @NotNull
        private PostCategory postCategory;
        @Schema(maxLength = 100)
        @Size(max = 100)
        private String title;
        @Schema(maxLength = 2000)
        @Size(max = 2000)
        private String content;
        private List<String> postImageList;
    }

    @Getter
    @Builder
    public static class PostUpdateRequest {
        @NotNull
        private PostCategory postCategory;
        @Schema(maxLength = 100)
        @Size(max = 100)
        private String title;
        @Schema(maxLength = 2000)
        @Size(max = 2000)
        private String content;
        private List<String> postImageList;
    }
}
