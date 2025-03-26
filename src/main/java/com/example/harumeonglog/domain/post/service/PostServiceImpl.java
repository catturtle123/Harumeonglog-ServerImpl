package com.example.harumeonglog.domain.post.service;

import com.example.harumeonglog.domain.post.controller.dto.request.PostRequest;
import com.example.harumeonglog.domain.post.controller.port.PostService;
import com.example.harumeonglog.domain.post.domain.Post;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {


    @Override
    public Slice<Post> getPosts(Long cursor, Integer size) {
        return null;
    }

    @Override
    public Post getPost() {
        return null;
    }

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

    @Override
    public Slice<Post> getMyPost(Long cursor, Integer size) {
        return null;
    }

    @Override
    public Slice<Post> getMyLikePost(Long cursor, Integer size) {
        return null;
    }
}
