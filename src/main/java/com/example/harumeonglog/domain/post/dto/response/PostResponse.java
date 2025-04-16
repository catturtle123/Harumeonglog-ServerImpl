package com.example.harumeonglog.domain.post.dto.response;

import com.example.harumeonglog.domain.member.dto.response.MemberResponse.MemberInfoResponse;
import com.example.harumeonglog.domain.post.entity.Post;
import com.example.harumeonglog.domain.post.entity.enums.PostCategory;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class PostResponse {

    @Getter
    @Builder
    public static class PostListResponse {
        private Long cursor;
        private Boolean hasNext;
        private List<PostPreviewResponse> items;

        public static PostListResponse from(Long cursor, Boolean hasNext,List<Post> postResponseList) {
            return PostListResponse.builder()
                    .cursor(cursor)
                    .hasNext(hasNext)
                    .items(postResponseList.stream().map(PostPreviewResponse::from).toList()
                    ).build();
        }
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

        public static PostPreviewResponse from(Post post) {
            return PostPreviewResponse.builder()
                    .postId(post.getId())
                    .content(post.getContent())
                    .likeNum(post.getPostLikeNum())
                    .commentNum(post.getCommentNum())
                    .postCategory(post.getCategory())
                    .memberInfoResponse(MemberInfoResponse.from(post.getMember()))
                    .build();
        }
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
        private List<String> postImages;

    }

}
