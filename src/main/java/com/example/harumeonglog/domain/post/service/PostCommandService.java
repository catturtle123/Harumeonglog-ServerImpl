package com.example.harumeonglog.domain.post.service;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.post.dto.request.PostRequest;
import com.example.harumeonglog.domain.post.dto.response.PostResponse;
import com.example.harumeonglog.domain.post.entity.Post;


public interface PostCommandService {
    Post createPost(PostRequest.PostCreateRequest postCreateRequest);

    Post updatePost(Long postId, PostRequest.PostUpdateRequest postUpdateRequest);

    void deletePost(Long postId);

    void likePost(Long postId, Member member);

    void reportPost(Long postId, Member member);
}
