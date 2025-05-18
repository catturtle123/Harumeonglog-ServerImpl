package com.example.harumeonglog.domain.post.dto.response;

import com.example.harumeonglog.domain.member.dto.response.MemberResponse.MemberInfoResponse;
import com.example.harumeonglog.domain.post.entity.enums.PostCategory;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class PostResponse {

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PostPreviewListResponse {
        private Long cursor;
        private Boolean hasNext;
        private List<PostPreviewResponse> items;

    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PostPreviewResponse {
        private Long postId;
        private String title;
        private String content;
        private Long likeNum;
        private Long commentNum;
        private Long elapsedTime;
        private PostCategory postCategory;
        private MemberInfoResponse memberInfoResponse;
        private String imageKeyName;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PostDetailResponse {
        private Long postId;
        private String content;
        private String title;
        private Long elapsedTime;
        private Long likeNum;
        private Long commentNum;
        private PostCategory postCategory;
        private MemberInfoResponse memberInfoResponse;
        private List<String> postImageList;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PostUpdateResponse {
        private Long postId;
        private String title;
        private String content;
        private PostCategory postCategory;
        private List<String> postImageList;
        private LocalDateTime updateAt;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PostCreateResponse {
        private Long postId;
        private LocalDateTime createAt;
        private LocalDateTime updateAt;
    }

}
