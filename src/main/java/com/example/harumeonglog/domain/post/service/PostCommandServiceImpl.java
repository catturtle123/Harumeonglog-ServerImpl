package com.example.harumeonglog.domain.post.service;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.post.converter.PostConverter;
import com.example.harumeonglog.domain.post.converter.PostLikeConverter;
import com.example.harumeonglog.domain.post.converter.PostReportConverter;
import com.example.harumeonglog.domain.post.dto.request.PostRequest;
import com.example.harumeonglog.domain.post.dto.response.PostResponse;
import com.example.harumeonglog.domain.post.entity.Post;
import com.example.harumeonglog.domain.post.entity.PostImage;
import com.example.harumeonglog.domain.post.entity.PostLike;
import com.example.harumeonglog.domain.post.entity.PostReport;
import com.example.harumeonglog.domain.post.repository.PostImageRepository;
import com.example.harumeonglog.domain.post.repository.PostLikeRepository;
import com.example.harumeonglog.domain.post.repository.PostReportRepository;
import com.example.harumeonglog.domain.post.repository.PostRepository;
import com.example.harumeonglog.global.error.code.PostErrorCode;
import com.example.harumeonglog.global.error.exception.PostException;
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
    private final PostLikeRepository postLikeRepository;
    private final PostReportRepository postReportRepository;

    @Override
    public PostResponse.PostCreateResponse createPost(PostRequest.PostCreateRequest postCreateRequest, Member member) {
        Post post = PostConverter.toPost(postCreateRequest, member);

        postCreateRequest.getPostImageList().forEach((s)-> {
            PostImage postImage = PostImage.builder().post(post).postImageKeyName(s).build();
            postImage.associateWith(post);
        });
        return PostConverter.toPostCreateResponse(postRepository.save(post));
    }

    @Override
    public PostResponse.PostUpdateResponse updatePost(Long postId, PostRequest.PostUpdateRequest postUpdateRequest, Member member) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostException(PostErrorCode.NOT_FOUND));

        isOwnPost(member, post);

        List<PostImage> postImageList = postUpdateRequest.getPostImageList().stream().map((s)-> PostImage.builder().post(post).postImageKeyName(s).build()).toList();
        post.update(postUpdateRequest.getContent(), postUpdateRequest.getPostCategory(), postImageList);

        return PostConverter.toPostUpdateResponse(post);
    }

    @Override
    public void deletePost(Long postId, Member member) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostException(PostErrorCode.NOT_FOUND));

        isOwnPost(member, post);

        post.softDelete();
    }

    @Override
    public void likePost(Long postId, Member member) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostException(PostErrorCode.NOT_FOUND));

        PostLike postLike = postLikeRepository.findByPostAndMember(post, member);
        if (postLike != null) {
            post.fixLikeNum(-1L);
            postLikeRepository.delete(postLike);
        } else {
            post.fixLikeNum(1L);
            postLikeRepository.save(PostLikeConverter.toPostLike(post, member));
        }
    }

    @Override
    public void reportPost(Long postId, Member member) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostException(PostErrorCode.NOT_FOUND));

        PostReport postReport = postReportRepository.findByPostAndMember(post, member);
        if (postReport != null) {
            post.fixReportNum(-1L);
            postReportRepository.delete(postReport);
        } else {
            post.fixReportNum(1L);
            postReportRepository.save(PostReportConverter.toPostReport(post, member));
        }
    }

    private void isOwnPost(Member member, Post post) {
        if (!post.getMember().getId().equals(member.getId())) {
            throw new PostException(PostErrorCode.NOT_OWN);
        }
    }
}
