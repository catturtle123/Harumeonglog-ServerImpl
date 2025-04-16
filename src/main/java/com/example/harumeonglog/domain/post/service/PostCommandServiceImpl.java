package com.example.harumeonglog.domain.post.service;

import com.example.harumeonglog.domain.post.dto.request.PostRequest;
import com.example.harumeonglog.domain.post.entity.Post;
import lombok.Builder;
import org.springframework.stereotype.Service;

@Service
@Builder
public class PostCommandServiceImpl implements PostCommandService {

    @Override
    public Post createPost(PostRequest.PostCreateRequest postCreateRequest) {
        return null;
    }

    @Override
    public Post updatePost(Long postId, PostRequest.PostUpdateRequest postUpdateRequest) {
        return null;
    }

    @Override
    public void deletePost(Long postId) {

    }

    @Override
    public void likePost(Long postId) {

    }

    @Override
    public void reportPost(Long postId) {

    }
}
