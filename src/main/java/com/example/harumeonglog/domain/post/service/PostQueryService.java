package com.example.harumeonglog.domain.post.service;


import com.example.harumeonglog.domain.post.entity.Post;
import org.springframework.data.domain.Slice;

public interface PostQueryService {
    Slice<Post> getPosts(Long cursor, Integer size);

    Post getPost();

    Slice<Post> getMyPost(Long cursor, Integer size);

    Slice<Post> getMyLikePost(Long cursor, Integer size);
}
