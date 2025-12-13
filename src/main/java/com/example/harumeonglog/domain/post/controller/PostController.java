package com.example.harumeonglog.domain.post.controller;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.post.controller.enums.PostRequestCategory;
import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.domain.post.dto.request.PostRequest;
import com.example.harumeonglog.domain.post.dto.response.PostResponse;
import com.example.harumeonglog.domain.post.service.PostCommandService;
import com.example.harumeonglog.domain.post.service.PostQueryService;

import com.example.harumeonglog.global.security.annotation.AuthenticatedMember;
import com.example.harumeonglog.global.validation.annotation.CheckCursorValidation;
import com.example.harumeonglog.global.validation.annotation.CheckSizeValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@Validated
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
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "postRequestCategory") PostRequestCategory postRequestCategory,
            @RequestParam(name = "cursor") @CheckCursorValidation Long cursor,
            @RequestParam(name = "size") @CheckSizeValidation Integer size,
            @AuthenticatedMember Member member
    ) {
        PostResponse.PostPreviewListResponse postListResponse = postQueryService.getPosts(cursor, size, search, postRequestCategory, member);
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
        PostResponse.PostDetailResponse postDetailResponse = postQueryService.getPost(member, postId);
        return CustomResponse.ok(postDetailResponse);
    }

    @Operation(summary = "게시물 생성 API by 김준환",description = "게시물 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    @PostMapping
    public CustomResponse<PostResponse.PostCreateResponse> createPost(
            @RequestBody @Valid PostRequest.PostCreateRequest postCreateRequest,
            @AuthenticatedMember Member member
            ) {
        PostResponse.PostCreateResponse postCreateResponse = postCommandService.createPost(postCreateRequest, member);
        return CustomResponse.ok(postCreateResponse);
    }

    @Operation(summary = "게시물 수정 API by 김준환",description = "게시물 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "POST404", description = "게시물을 찾지 못했습니다."),
            @ApiResponse(responseCode = "POST403", description = "자신의 게시물이 아닙니다.")
    })
    @PatchMapping("/{postId}")
    public CustomResponse<PostResponse.PostUpdateResponse> updatePost(
            @PathVariable Long postId,
            @RequestBody @Valid PostRequest.PostUpdateRequest postUpdateRequest,
            @AuthenticatedMember Member member
    ) {
        PostResponse.PostUpdateResponse postUpdateResponse = postCommandService.updatePost(postId, postUpdateRequest, member);
        return CustomResponse.ok(postUpdateResponse);
    }

    @Operation(summary = "게시물 삭제 API by 김준환",description = "게시물 삭제 (soft delete)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "POST404", description = "게시물을 찾지 못했습니다.")
    })
    @DeleteMapping("/{postId}")
    public CustomResponse<Void> deletePost(
            @AuthenticatedMember Member member,
            @PathVariable Long postId
    ) {
        postCommandService.deletePost(postId, member);
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
            @RequestParam(name = "cursor") @CheckCursorValidation Long cursor,
            @RequestParam(name = "size") @CheckSizeValidation Integer size,
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
            @RequestParam(name = "cursor") @CheckCursorValidation Long cursor,
            @RequestParam(name = "size") @CheckSizeValidation Integer size,
            @AuthenticatedMember Member member
    ) {
        PostResponse.PostPreviewListResponse postPreviewListResponse = postQueryService.getMyLikePost(cursor, size, member);
        return CustomResponse.ok(postPreviewListResponse);
    }

    @Operation(summary = "어제 인기 게시물 by 김준환", description = "어제 인기 게시물 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    @GetMapping("/yesterday")
    public CustomResponse<PostResponse.PostYesterdayResponseList> getYesterdayGoodPosts(
    ) {
        PostResponse.PostYesterdayResponseList postYesterdayResponseList = postQueryService.getYesterdayGoodPosts();
        return CustomResponse.ok(postYesterdayResponseList);
    }
}
