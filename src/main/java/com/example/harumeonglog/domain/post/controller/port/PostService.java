package com.example.harumeonglog.domain.post.controller.port;

import com.example.harumeonglog.domain.post.controller.dto.request.PostRequest;
import com.example.harumeonglog.domain.post.domain.Post;
import org.springframework.data.domain.Slice;

public interface PostService {
    Slice<Post> getPosts(Long cursor, Integer size);

    Post getPost();

    Post createPost(PostRequest.PostCreateRequest postCreateRequest);

    Post updatePost(Long postId, PostRequest.PostUpdateRequest postUpdateRequest);

    void deletePost(Long postId);

    void likePost(Long postId);

    void reportPost(Long postId);

    Slice<Post> getMyPost(Long cursor, Integer size);

    Slice<Post> getMyLikePost(Long cursor, Integer size);
}
