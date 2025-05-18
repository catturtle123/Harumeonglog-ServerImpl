package com.example.harumeonglog.domain.post.converter;

import com.example.harumeonglog.domain.member.dto.response.MemberResponse;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.post.dto.request.PostRequest;
import com.example.harumeonglog.domain.post.dto.response.PostResponse;
import com.example.harumeonglog.domain.post.entity.Post;
import com.example.harumeonglog.domain.post.entity.PostImage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class PostConverter {

    public static PostResponse.PostDetailResponse toPostDetailResponse(Post post, MemberResponse.MemberInfoResponse memberInfoResponse, List<String> imageList) {

        // 현재 시각
        LocalDateTime now = LocalDateTime.now();

        // 생성 시각
        LocalDateTime createdAt = post.getCreatedAt();

        // 시간 차이 계산 (ChronoUnit.HOURS 사용)
        long elapsedTime = ChronoUnit.HOURS.between(createdAt, now);

        return PostResponse.PostDetailResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .postCategory(post.getCategory())
                .memberInfoResponse(memberInfoResponse)
                .elapsedTime(elapsedTime)
                .likeNum(post.getPostLikeNum())
                .commentNum(post.getCommentNum())
                .postImageList(imageList)
                .build();
    }

    public static PostResponse.PostPreviewResponse toPostPreviewResponse(Post post, MemberResponse.MemberInfoResponse memberInfoResponse, String image) {

        // 현재 시각
        LocalDateTime now = LocalDateTime.now();

        // 생성 시각
        LocalDateTime createdAt = post.getCreatedAt();

        // 시간 차이 계산 (ChronoUnit.HOURS 사용)
        long elapsedTime = ChronoUnit.HOURS.between(createdAt, now);

        return PostResponse.PostPreviewResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .postCategory(post.getCategory())
                .elapsedTime(elapsedTime)
                .content(post.getContent())
                .likeNum(post.getPostLikeNum())
                .commentNum(post.getCommentNum())
                .memberInfoResponse(memberInfoResponse)
                .imageKeyName(image)
                .build();
    }

    public static PostResponse.PostPreviewListResponse toPostPreviewListResponse(List<PostResponse.PostPreviewResponse> postList, Long cursor, Boolean hasNext) {
        return PostResponse.PostPreviewListResponse.builder()
                .items(postList)
                .hasNext(hasNext)
                .cursor(cursor)
                .build();
    }

    public static Post toPost(PostRequest.PostCreateRequest postCreateRequest, Member member) {
        return Post.builder()
                .title(postCreateRequest.getTitle())
                .content(postCreateRequest.getContent())
                .category(postCreateRequest.getPostCategory())
                .member(member)
                .build();
    }

    public static PostResponse.PostUpdateResponse toPostUpdateResponse(Post post) {

        List<String> postImageList = post.getPostImageList().stream().map(PostImage::getPostImageKeyName).toList();

        return PostResponse.PostUpdateResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .postCategory(post.getCategory())
                .postImageList(postImageList)
                .updateAt(post.getUpdatedAt())
                .content(post.getContent())
                .build();
    }

    public static PostResponse.PostCreateResponse toPostCreateResponse(Post post) {

        return PostResponse.PostCreateResponse.builder()
                .postId(post.getId())
                .updateAt(post.getUpdatedAt())
                .createAt(post.getCreatedAt())
                .build();
    }
}
