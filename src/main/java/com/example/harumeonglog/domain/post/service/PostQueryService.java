package com.example.harumeonglog.domain.post.service;


import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.post.controller.enums.PostRequestCategory;
import com.example.harumeonglog.domain.post.dto.response.PostResponse;
import com.example.harumeonglog.domain.post.entity.Post;
import org.springframework.data.domain.Slice;

public interface PostQueryService {
    PostResponse.PostPreviewListResponse getPosts(Long cursor, Integer size, String search, PostRequestCategory postRequestCategory, Member member);

    PostResponse.PostDetailResponse getPost(Member member, Long postId);

    PostResponse.PostPreviewListResponse getMyPost(Long cursor, Integer size, Member member);

    PostResponse.PostPreviewListResponse getMyLikePost(Long cursor, Integer size, Member member);

    PostResponse.HomePostListRequest getHomePosts();
}
