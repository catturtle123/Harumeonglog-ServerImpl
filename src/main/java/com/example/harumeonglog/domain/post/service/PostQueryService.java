package com.example.harumeonglog.domain.post.service;


import com.example.harumeonglog.domain.post.controller.enums.PostRequestCategory;
import com.example.harumeonglog.domain.post.dto.response.PostResponse;
import com.example.harumeonglog.domain.post.entity.Post;
import org.springframework.data.domain.Slice;

public interface PostQueryService {
    PostResponse.PostPreviewListResponse getPosts(Long cursor, Integer size, String search, PostRequestCategory postRequestCategory);

    PostResponse.PostDetailResponse getPost(Long postId);

    Slice<Post> getMyPost(Long cursor, Integer size);

    Slice<Post> getMyLikePost(Long cursor, Integer size);
}
