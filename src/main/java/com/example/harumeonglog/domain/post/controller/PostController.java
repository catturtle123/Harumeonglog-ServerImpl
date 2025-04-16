package com.example.harumeonglog.domain.post.controller;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.post.controller.enums.PostRequestCategory;
import com.example.harumeonglog.domain.post.entity.Post;
import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.domain.post.dto.request.PostRequest;
import com.example.harumeonglog.domain.post.dto.response.PostResponse;
import com.example.harumeonglog.domain.post.service.PostCommandService;
import com.example.harumeonglog.domain.post.service.PostQueryService;

import com.example.harumeonglog.global.security.annotation.AuthenticatedMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@Tag(name = "Post", description = "Post 관련 API")
public class PostController {

    private final PostCommandService postCommandService;
    private final PostQueryService postQueryService;

    @Operation(summary = "게시물 조회 API by 김준환", description = "게시물 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
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

    @Operation(summary = "게시물 상세 조회 API by 김준환",description = "게시물 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "POST404", description = "게시물을 찾지 못했습니다.")
    })
    @GetMapping("/{postId}")
    public CustomResponse<PostResponse.PostDetailResponse> getPost(
            @PathVariable Long postId, @AuthenticatedMember Member member
    ) {
        PostResponse.PostDetailResponse postDetailResponse = postQueryService.getPost(postId);
        return CustomResponse.ok(postDetailResponse);
    }

    @Operation(summary = "게시물 생성 API by 김준환",description = "게시물 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    @PostMapping
    public CustomResponse<Long> createPost(
            @RequestBody PostRequest.PostCreateRequest postCreateRequest
            ) {
        Post post = postCommandService.createPost(postCreateRequest);
        return CustomResponse.ok(post.getId());
    }

    @Operation(summary = "게시물 수정 API by 김준환",description = "게시물 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "POST404", description = "게시물을 찾지 못했습니다.")
    })
    @PatchMapping("/{postId}")
    public CustomResponse<Long> updatePost(
            @PathVariable Long postId,
            @RequestBody PostRequest.PostUpdateRequest postUpdateRequest
    ) {
        Post post = postCommandService.updatePost(postId, postUpdateRequest);
        return CustomResponse.ok(post.getId());
    }

    @Operation(summary = "게시물 삭제 API by 김준환",description = "게시물 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "POST404", description = "게시물을 찾지 못했습니다.")
    })
    @DeleteMapping("/{postId}")
    public CustomResponse<Void> deletePost(
            @PathVariable Long postId
    ) {
        postCommandService.deletePost(postId);
        return CustomResponse.ok(null);
    }

    @Operation(summary = "게시물 좋아요 API by 김준환",description = "게시물 좋아요 생성/삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "POST404", description = "게시물을 찾지 못했습니다.")
    })
    @PostMapping("/{postId}/likes")
    public CustomResponse<Void> likePost(
            @PathVariable Long postId,
            @AuthenticatedMember Member member
    ) {
        postCommandService.likePost(postId, member);
        return CustomResponse.ok(null);
    }

    @Operation(summary = "게시물 신고 API by 김준환",description = "게시물 신고 생성/삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "POST404", description = "게시물을 찾지 못했습니다.")
    })
    @PostMapping("/{postId}/reports")
    public CustomResponse<Void> reportPost(
            @PathVariable Long postId,
            @AuthenticatedMember Member member
    ) {
        postCommandService.reportPost(postId, member);
        return CustomResponse.ok(null);
    }

    @Operation(summary = "내가 쓴 게시물 조회 API by 김준환",description = "내가 쓴 게시물 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    @GetMapping("/me")
    public CustomResponse<PostResponse.PostPreviewListResponse> getMyPost(
            @RequestParam(name = "cursor") Long cursor,
            @RequestParam(name = "size") Integer size,
            @AuthenticatedMember Member member
    ) {
        PostResponse.PostPreviewListResponse postPreviewListResponse = postQueryService.getMyPost(cursor, size, member);
        return CustomResponse.ok(postPreviewListResponse);
    }

    @Operation(summary = "내가 좋아요 누른 게시물 조회 API by 김준환",description = "내가 좋아요 누른 게시물 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
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
