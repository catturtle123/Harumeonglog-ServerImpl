package com.example.harumeonglog.domain.post.controller.port;

import com.example.harumeonglog.domain.post.controller.dto.request.PostRequest;
import com.example.harumeonglog.domain.post.domain.Post;

public interface PostCommandService {
    Post createPost(PostRequest.PostCreateRequest postCreateRequest);

    Post updatePost(Long postId, PostRequest.PostUpdateRequest postUpdateRequest);

    void deletePost(Long postId);

    void likePost(Long postId);

    void reportPost(Long postId);
}
