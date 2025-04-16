package com.example.harumeonglog.domain.post.service;

import com.example.harumeonglog.domain.post.converter.PostConverter;
import com.example.harumeonglog.domain.post.dto.request.PostRequest;
import com.example.harumeonglog.domain.post.entity.Post;
import com.example.harumeonglog.domain.post.entity.PostImage;
import com.example.harumeonglog.domain.post.repository.PostImageRepository;
import com.example.harumeonglog.domain.post.repository.PostRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostCommandServiceImpl implements PostCommandService {
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;

    @Override
    public Post createPost(PostRequest.PostCreateRequest postCreateRequest) {
        Post post = postRepository.save(PostConverter.toPost(postCreateRequest));
        List<PostImage> postImageList = postCreateRequest.getPostImageList().stream().map((s)-> PostImage.builder().post(post).postImageKeyName(s).build()).toList();
        postImageRepository.saveAll(postImageList);
        return post;
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
