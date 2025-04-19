package com.example.harumeonglog.domain.post.controller;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.post.controller.enums.PostRequestCategory;
import com.example.harumeonglog.domain.post.controller.specification.PostControllerSpecification;
import com.example.harumeonglog.domain.post.entity.Post;
import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.domain.post.dto.request.PostRequest;
import com.example.harumeonglog.domain.post.dto.response.PostResponse;
import com.example.harumeonglog.domain.post.service.PostCommandService;
import com.example.harumeonglog.domain.post.service.PostQueryService;

import com.example.harumeonglog.global.security.annotation.AuthenticatedMember;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@Tag(name = "Post", description = "Post 관련 API")
public class PostController implements PostControllerSpecification {

    private final PostCommandService postCommandService;
    private final PostQueryService postQueryService;


    @GetMapping
    public CustomResponse<PostResponse.PostPreviewListResponse> getPosts(
            @RequestParam(name = "search") String search,
            @RequestParam(name = "postRequestCategory") PostRequestCategory postRequestCategory,
            @RequestParam(name = "cursor") Long cursor,
            @RequestParam(name = "size") Integer size
    ) {
        PostResponse.PostPreviewListResponse postListResponse = postQueryService.getPosts(cursor, size, search, postRequestCategory);
        return CustomResponse.ok(postListResponse);
    }

    @GetMapping("/{postId}")
    public CustomResponse<PostResponse.PostDetailResponse> getPost(
            @PathVariable Long postId, @AuthenticatedMember Member member
    ) {
        PostResponse.PostDetailResponse postDetailResponse = postQueryService.getPost(postId);
        return CustomResponse.ok(postDetailResponse);
    }


    @PostMapping
    public CustomResponse<Long> createPost(
            @RequestBody PostRequest.PostCreateRequest postCreateRequest
            ) {
        Post post = postCommandService.createPost(postCreateRequest);
        return CustomResponse.ok(post.getId());
    }

    @PatchMapping("/{postId}")
    public CustomResponse<Long> updatePost(
            @PathVariable Long postId,
            @RequestBody PostRequest.PostUpdateRequest postUpdateRequest,
            @AuthenticatedMember Member member
    ) {
        Post post = postCommandService.updatePost(postId, postUpdateRequest, member);
        return CustomResponse.ok(post.getId());
    }

    @DeleteMapping("/{postId}")
    public CustomResponse<Void> deletePost(
            @AuthenticatedMember Member member,
            @PathVariable Long postId
    ) {
        postCommandService.deletePost(postId, member);
        return CustomResponse.ok(null);
    }


    @PostMapping("/{postId}/likes")
    public CustomResponse<Void> likePost(
            @PathVariable Long postId,
            @AuthenticatedMember Member member
    ) {
        postCommandService.likePost(postId, member);
        return CustomResponse.ok(null);
    }

    @PostMapping("/{postId}/reports")
    public CustomResponse<Void> reportPost(
            @PathVariable Long postId,
            @AuthenticatedMember Member member
    ) {
        postCommandService.reportPost(postId, member);
        return CustomResponse.ok(null);
    }

    @GetMapping("/me")
    public CustomResponse<PostResponse.PostPreviewListResponse> getMyPost(
            @RequestParam(name = "cursor") Long cursor,
            @RequestParam(name = "size") Integer size,
            @AuthenticatedMember Member member
    ) {
        PostResponse.PostPreviewListResponse postPreviewListResponse = postQueryService.getMyPost(cursor, size, member);
        return CustomResponse.ok(postPreviewListResponse);
    }

    @GetMapping("/me/likes")
    public CustomResponse<PostResponse.PostPreviewListResponse> getMyLikePost(
            @RequestParam(name = "cursor") Long cursor,
            @RequestParam(name = "size") Integer size,
            @AuthenticatedMember Member member
    ) {
        PostResponse.PostPreviewListResponse postPreviewListResponse = postQueryService.getMyLikePost(cursor, size, member);
        return CustomResponse.ok(postPreviewListResponse);
    }

}
