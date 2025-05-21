package com.example.harumeonglog.domain.post.service;


import com.example.harumeonglog.domain.member.converter.MemberConverter;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.post.controller.enums.PostRequestCategory;
import com.example.harumeonglog.domain.post.converter.PostConverter;
import com.example.harumeonglog.domain.post.dto.response.PostResponse;
import com.example.harumeonglog.domain.post.entity.Post;
import com.example.harumeonglog.domain.post.entity.enums.PostCategory;
import com.example.harumeonglog.domain.post.repository.PostRepository;
import com.example.harumeonglog.global.error.code.PostErrorCode;
import com.example.harumeonglog.global.error.exception.PostException;
import com.example.harumeonglog.global.util.S3Util;
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
    private final S3Util s3Util;

    @Override
    public PostResponse.PostPreviewListResponse getPosts(Long cursor, Integer size, String search, PostRequestCategory postRequestCategory) {
        cursor = normalizeCursor(cursor);
        search = normalizeSearch(search);

        Slice<Post> postSlice;
        if (postRequestCategory.equals(PostRequestCategory.ALL)) {
            postSlice = postRepository.findByContentLikeAndIdLessThanOrderByIdDesc(search, cursor, PageRequest.of(0, size));
        } else {
            PostCategory postCategory = PostCategory.valueOf(postRequestCategory.name());
            postSlice = postRepository.findByPostCategoryAndContentLikeAndIdLessThanOrderByIdDesc(search, cursor, postCategory, PageRequest.of(0, size));
        }

        return buildPostPreviewListResponse(postSlice);
    }

    @Override
    public PostResponse.PostDetailResponse getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostException(PostErrorCode.NOT_FOUND));
        List<String> postImageList = post.getPostImageList().stream()
                .map((p)->s3Util.getUrlFromKey(p.getPostImageKeyName()))
                .toList();
        return PostConverter.toPostDetailResponse(post, MemberConverter.toMemberInfoResponse(post.getMember(), s3Util), postImageList);
    }

    @Override
    public PostResponse.PostPreviewListResponse getMyPost(Long cursor, Integer size, Member member) {
        cursor = normalizeCursor(cursor);

        Slice<Post> postSlice = postRepository.findByMemberAndDeletedAtIsNullAndIdLessThanOrderByIdDesc(member, cursor, PageRequest.of(0, size));
        return buildPostPreviewListResponse(postSlice);
    }

    @Override
    public PostResponse.PostPreviewListResponse getMyLikePost(Long cursor, Integer size, Member member) {
        cursor = normalizeCursor(cursor);

        Slice<Post> postSlice = postRepository.findMyLikePosts(member, cursor, PageRequest.of(0, size));
        return buildPostPreviewListResponse(postSlice);
    }

    private Long normalizeCursor(Long cursor) {
        return (cursor == 0) ? Long.MAX_VALUE : cursor;
    }

    private String normalizeSearch(String search) {
        return (search == null || search.isEmpty()) ? "" : search;
    }

    private PostResponse.PostPreviewListResponse buildPostPreviewListResponse(Slice<Post> postSlice) {
        Long nextCursor = null;
        List<Post> posts = postSlice.toList();

        if (!postSlice.isLast() && !posts.isEmpty()) {
            nextCursor = posts.get(posts.size() - 1).getId();
        }

        List<PostResponse.PostPreviewResponse> postPreviewResponses = posts.stream()
                .map(post -> {
                    String postImageKey = post.getPostImageList().isEmpty() ? null :
                            s3Util.getUrlFromKey(post.getPostImageList().get(0).getPostImageKeyName());
                    return PostConverter.toPostPreviewResponse(post, MemberConverter.toMemberInfoResponse(post.getMember(), s3Util), postImageKey);
                })
                .toList();

        return PostConverter.toPostPreviewListResponse(postPreviewResponses, nextCursor, postSlice.hasNext());
    }
}
