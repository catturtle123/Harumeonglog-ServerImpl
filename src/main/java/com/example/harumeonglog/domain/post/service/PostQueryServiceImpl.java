package com.example.harumeonglog.domain.post.service;

import com.example.harumeonglog.domain.post.controller.port.PostQueryService;
import com.example.harumeonglog.domain.post.domain.Post;
import lombok.Builder;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@Builder
public class PostQueryServiceImpl implements PostQueryService {


    @Override
    public Slice<Post> getPosts(Long cursor, Integer size) {
        return null;
    }

    @Override
    public Post getPost() {
        return null;
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
