package com.example.harumeonglog.domain.post.controller.dto.response;

import com.example.harumeonglog.domain.member.controller.dto.response.MemberResponse;
import com.example.harumeonglog.domain.member.controller.dto.response.MemberResponse.MemberInfoResponse;
import com.example.harumeonglog.domain.post.domain.Post;
import com.example.harumeonglog.domain.post.domain.PostImage;
import com.example.harumeonglog.domain.post.domain.enums.PostCategory;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;

public class PostResponse {

    @Getter
    @Builder
    public static class PostListResponse {
        private Long cursor;
        private Boolean hasNext;
        private List<PostPreviewResponse> postResponseList;

        public static PostListResponse from(Long cursor,Slice<Post> postResponseList) {
            return PostListResponse.builder()
                    .cursor(cursor)
                    .hasNext(postResponseList.hasNext())
                    .postResponseList(postResponseList.stream().map(PostPreviewResponse::from).toList()
                    ).build();
        }
    }

    @Getter
    @Builder
    public static class PostPreviewResponse {
        private String content;
        private Long likeNum;
        private Long commentNum;
        private PostCategory postCategory;
        private MemberInfoResponse memberInfoResponse;

        public static PostPreviewResponse from(Post post) {
            return PostPreviewResponse.builder()
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
        private String content;
        private Long likeNum;
        private Long commentNum;
        private PostCategory postCategory;
        private MemberInfoResponse memberInfoResponse;
        private List<String> postImages;

        public static PostDetailResponse from(Post post) {
            return PostDetailResponse.builder()
                    .content(post.getContent())
                    .commentNum(post.getCommentNum())
                    .likeNum(post.getPostLikeNum())
                    .postCategory(post.getCategory())
                    .memberInfoResponse(MemberInfoResponse.from(post.getMember()))
                    .postImages(post.getPostImageList().stream().map(PostImage::getPostImageKeyName).toList())
                    .build();
        }
    }

}
