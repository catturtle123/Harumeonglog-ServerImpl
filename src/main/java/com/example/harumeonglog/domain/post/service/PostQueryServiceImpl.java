package com.example.harumeonglog.domain.post.service;


import com.example.harumeonglog.domain.member.converter.MemberConverter;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.post.controller.enums.PostRequestCategory;
import com.example.harumeonglog.domain.post.converter.PostConverter;
import com.example.harumeonglog.domain.post.dto.response.PostResponse;
import com.example.harumeonglog.domain.post.entity.Post;
import com.example.harumeonglog.domain.post.entity.PostLike;
import com.example.harumeonglog.domain.post.entity.enums.PostCategory;
import com.example.harumeonglog.domain.post.repository.PostLikeRepository;
import com.example.harumeonglog.domain.post.repository.PostRepository;
import com.example.harumeonglog.global.error.code.PostErrorCode;
import com.example.harumeonglog.global.error.exception.PostException;
import com.example.harumeonglog.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostQueryServiceImpl implements PostQueryService {

    private final PostRepository postRepository;
    private final S3Util s3Util;
    private final PostLikeRepository postLikeRepository;

    @Override
    public PostResponse.PostPreviewListResponse getPosts(Long cursor, Integer size, String search, PostRequestCategory postRequestCategory, Member member) {
        cursor = normalizeCursor(cursor);
        search = normalizeSearch(search);

        Slice<Post> postSlice;
        if (postRequestCategory.isAll()) {
            postSlice = postRepository.findByContentLikeAndIdLessThanOrderByIdDesc(search, cursor, PageRequest.of(0, size));
            return buildPostPreviewListResponse(postSlice, member);
        }

        PostCategory postCategory = postRequestCategory.toPostCategory();
        postSlice = postRepository.findByPostCategoryAndContentLikeAndIdLessThanOrderByIdDesc(search, cursor, postCategory, PageRequest.of(0, size));
        return buildPostPreviewListResponse(postSlice, member);
    }

    @Override
    public PostResponse.PostDetailResponse getPost(Member owner,Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostException(PostErrorCode.NOT_FOUND));
        List<String> postImageList = extractImageKeyName(post);
        Boolean isLiked = postLikeRepository.existsByPostAndMember(post, owner);

        return PostConverter.toPostDetailResponse(post, MemberConverter.toMemberInfoResponse(post.getMember(), s3Util), postImageList, isLiked);
    }

    @Override
    public PostResponse.PostPreviewListResponse getMyPost(Long cursor, Integer size, Member member) {
        cursor = normalizeCursor(cursor);

        Slice<Post> postSlice = postRepository.findByMemberAndDeletedAtIsNullAndIdLessThanOrderByIdDesc(member, cursor, PageRequest.of(0, size));
        return buildPostPreviewListResponse(postSlice, member);
    }

    @Override
    public PostResponse.PostPreviewListResponse getMyLikePost(Long cursor, Integer size, Member member) {
        cursor = normalizeCursor(cursor);

        Slice<Post> postSlice = postRepository.findMyLikePosts(member, cursor, PageRequest.of(0, size));
        return buildPostPreviewListResponse(postSlice, member);
    }

    @Cacheable(cacheNames = "getHomePosts", cacheManager = "homePostsCacheManager", key = "'posts:categories'")
    @Override
    public PostResponse.HomePostListRequest getHomePosts() {
        List<Post> firstPostsByAllCategory = postRepository.findFirstPostsByAllCategory();

        return PostConverter.toHomePostListRequest(firstPostsByAllCategory);
    }

    private Long normalizeCursor(Long cursor) {
        return (cursor == 0) ? Long.MAX_VALUE : cursor;
    }

    private String normalizeSearch(String search) {
        return (search == null || search.isEmpty()) ? "" : search;
    }

    private PostResponse.PostPreviewListResponse buildPostPreviewListResponse(Slice<Post> postSlice, Member member) {
        Long nextCursor = null;
        List<Post> posts = postSlice.toList();

        if (postSlice.hasNext() && posts.size() > 0) {
            nextCursor = posts.get(posts.size() - 1).getId();
        }

        // ✅ 좋아요 여부를 한 번에 조회
        List<PostLike> postLikes = postLikeRepository.findByPostInAndMember(posts, member);
        Set<Long> likedPostIds = postLikes.stream()
                .map(postLike -> postLike.getPost().getId())
                .collect(Collectors.toSet());

        List<PostResponse.PostPreviewResponse> postPreviewResponses = posts.stream()
                .map(post -> {
                    String postImageKey = post.getPostImageList().isEmpty() ? null :
                            s3Util.getUrlFromKey(post.getPostImageList().get(0).getPostImageKeyName());

                    boolean isLiked = likedPostIds.contains(post.getId());

                    return PostConverter.toPostPreviewResponse(
                            post,
                            MemberConverter.toMemberInfoResponse(post.getMember(), s3Util),
                            postImageKey,
                            isLiked
                    );
                })
                .toList();

        return PostConverter.toPostPreviewListResponse(postPreviewResponses, nextCursor, postSlice.hasNext());
    }

    private List<String> extractImageKeyName(Post post) {
        return post.getPostImageList().stream()
                .map((p) -> s3Util.getUrlFromKey(p.getPostImageKeyName()))
                .toList();
    }
}
