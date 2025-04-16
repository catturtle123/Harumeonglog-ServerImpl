package com.example.harumeonglog.domain.post.service;


import com.example.harumeonglog.domain.member.converter.MemberConverter;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.post.controller.enums.PostRequestCategory;
import com.example.harumeonglog.domain.post.converter.PostConverter;
import com.example.harumeonglog.domain.post.dto.response.PostResponse;
import com.example.harumeonglog.domain.post.entity.Post;
import com.example.harumeonglog.domain.post.entity.PostImage;
import com.example.harumeonglog.domain.post.entity.enums.PostCategory;
import com.example.harumeonglog.domain.post.repository.PostRepository;
import com.example.harumeonglog.global.error.code.PostErrorCode;
import com.example.harumeonglog.global.error.exception.PostException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostQueryServiceImpl implements PostQueryService {

    private final PostRepository postRepository;

    @Override
    public PostResponse.PostPreviewListResponse getPosts(Long cursor, Integer size, String search, PostRequestCategory postRequestCategory) {

        if (search == null || search.isEmpty()) {
            search = "";
        }

        if (cursor == 0) {
            cursor = Long.MAX_VALUE;
        }

        Slice<Post> postSlice;
        if (postRequestCategory.equals(PostRequestCategory.ALL)) {
            postSlice = postRepository.findByContentLikeAndIdLessThanOrderByIdDesc(search, cursor, PageRequest.of(0, size));
        } else {
            PostCategory postCategory = PostCategory.valueOf(postRequestCategory.name());
            postSlice = postRepository.findByPostCategoryAndContentLikeAndIdLessThanOrderByIdDesc(search, cursor, postCategory, PageRequest.of(0, size));
        }

        Long nextCursor = null;
        if (!postSlice.isLast()) {
            nextCursor = postSlice.toList().get(postSlice.toList().size() - 1).getId();
        }

        List<PostResponse.PostPreviewResponse> postPreviewResponses = postSlice.toList().stream().map((post) -> {
            String postImageKey = post.getPostImageList().get(0).getPostImageKeyName() != null ? post.getPostImageList().get(0).getPostImageKeyName() : null;
            return PostConverter.toPostPreviewResponse(post, MemberConverter.toMemberInfoResponse(post.getMember()), postImageKey);
        }).toList();

        return PostConverter.toPostPreviewListResponse(postPreviewResponses, nextCursor, postSlice.hasNext());
    }

    @Override
    public PostResponse.PostDetailResponse getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostException(PostErrorCode.NOT_FOUND));
        List<String> postImageList = post.getPostImageList().stream().map(PostImage::getPostImageKeyName).toList();
        return PostConverter.toPostDetailResponse(post, MemberConverter.toMemberInfoResponse(post.getMember()), postImageList);
    }

    @Override
    public PostResponse.PostPreviewListResponse getMyPost(Long cursor, Integer size, Member member) {
        if (cursor == 0) {
            cursor = Long.MAX_VALUE;
        }

        Slice<Post> postSlice = postRepository.findByMemberAndIdLessThanOrderByIdDesc(member, cursor, PageRequest.of(0, size));

        Long nextCursor = null;
        if (!postSlice.isLast()) {
            nextCursor = postSlice.toList().get(postSlice.toList().size() - 1).getId();
        }

        List<PostResponse.PostPreviewResponse> postPreviewResponses = postSlice.toList().stream().map((post) -> {
            String postImageKey = post.getPostImageList().get(0).getPostImageKeyName() != null ? post.getPostImageList().get(0).getPostImageKeyName() : null;
            return PostConverter.toPostPreviewResponse(post, MemberConverter.toMemberInfoResponse(post.getMember()), postImageKey);
        }).toList();

        return PostConverter.toPostPreviewListResponse(postPreviewResponses, nextCursor, postSlice.hasNext());
    }

    @Override
    public PostResponse.PostPreviewListResponse getMyLikePost(Long cursor, Integer size, Member member) {
        if (cursor == 0) {
            cursor = Long.MAX_VALUE;
        }

        Slice<Post> postSlice = postRepository.findMyLikePosts(member, cursor, PageRequest.of(0, size));

        Long nextCursor = null;
        if (!postSlice.isLast()) {
            nextCursor = postSlice.toList().get(postSlice.toList().size() - 1).getId();
        }

        List<PostResponse.PostPreviewResponse> postPreviewResponses = postSlice.toList().stream().map((post) -> {
            String postImageKey = post.getPostImageList().get(0).getPostImageKeyName() != null ? post.getPostImageList().get(0).getPostImageKeyName() : null;
            return PostConverter.toPostPreviewResponse(post, MemberConverter.toMemberInfoResponse(post.getMember()), postImageKey);
        }).toList();

        return PostConverter.toPostPreviewListResponse(postPreviewResponses, nextCursor, postSlice.hasNext());
    }
}
