package com.example.harumeonglog.domain.post.dto.response;

import com.example.harumeonglog.domain.member.dto.response.MemberResponse.MemberInfoResponse;
import com.example.harumeonglog.domain.post.entity.Post;
import com.example.harumeonglog.domain.post.entity.enums.PostCategory;
import lombok.*;

import java.util.List;

public class PostResponse {

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class PostPreviewListResponse {
        private Long cursor;
        private Boolean hasNext;
        private List<PostPreviewResponse> items;

    }

    @Getter
    @Builder
    public static class PostPreviewResponse {
        private Long postId;
        private String content;
        private Long likeNum;
        private Long commentNum;
        private PostCategory postCategory;
        private MemberInfoResponse memberInfoResponse;
    }

    @Getter
    @Builder
    public static class PostDetailResponse {
        private Long postId;
        private String content;
        private Long likeNum;
        private Long commentNum;
        private PostCategory postCategory;
        private MemberInfoResponse memberInfoResponse;
        private List<String> postImageList;
    }

}
