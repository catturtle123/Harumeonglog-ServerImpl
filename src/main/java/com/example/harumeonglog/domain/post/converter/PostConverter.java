package com.example.harumeonglog.domain.post.converter;

import com.example.harumeonglog.domain.member.converter.MemberConverter;
import com.example.harumeonglog.domain.member.dto.response.MemberResponse;
import com.example.harumeonglog.domain.post.dto.response.PostResponse;
import com.example.harumeonglog.domain.post.entity.Post;

import java.util.List;

public class PostConverter {

    public static PostResponse.PostDetailResponse toPostDetailResponse(Post post, MemberResponse.MemberInfoResponse memberInfoResponse, List<String> imageList) {
        return PostResponse.PostDetailResponse.builder()
                .postId(post.getId())
                .postCategory(post.getCategory())
                .memberInfoResponse(memberInfoResponse)
                .likeNum(post.getPostLikeNum())
                .commentNum(post.getCommentNum())
                .postImageList(imageList)
                .build();
    }

    public static PostResponse.PostPreviewResponse toPostPreviewResponse(Post post, MemberResponse.MemberInfoResponse memberInfoResponse, String imageList) {
        return PostResponse.PostPreviewResponse.builder()
                .postId(post.getId())
                .postCategory(post.getCategory())
                .content(post.getContent())
                .likeNum(post.getPostLikeNum())
                .commentNum(post.getCommentNum())
                .memberInfoResponse(memberInfoResponse)
                .build();
    }

    public static PostResponse.PostPreviewListResponse toPostPreviewListResponse(List<PostResponse.PostPreviewResponse> postList, Long cursor, Boolean hasNext) {
        return PostResponse.PostPreviewListResponse.builder()
                .items(postList)
                .hasNext(hasNext)
                .cursor(cursor)
                .build();
    }
}
