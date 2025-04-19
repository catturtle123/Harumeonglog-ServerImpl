package com.example.harumeonglog.domain.post.controller.specification;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.post.controller.enums.PostRequestCategory;
import com.example.harumeonglog.domain.post.dto.request.PostRequest;
import com.example.harumeonglog.domain.post.dto.response.PostResponse;
import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.global.security.annotation.AuthenticatedMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Post", description = "Post 관련 API")
public interface PostControllerSpecification {

    @Operation(summary = "게시물 조회 API by 김준환", description = "게시물 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    @GetMapping
    CustomResponse<PostResponse.PostPreviewListResponse> getPosts(
            @RequestParam(name = "search") String search,
            @RequestParam(name = "postRequestCategory") PostRequestCategory postRequestCategory,
            @RequestParam(name = "cursor") Long cursor,
            @RequestParam(name = "size") Integer size
    );

    @Operation(summary = "게시물 상세 조회 API by 김준환",description = "게시물 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "POST404", description = "게시물을 찾지 못했습니다.")
    })
    @GetMapping("/{postId}")
    CustomResponse<PostResponse.PostDetailResponse> getPost(
            @PathVariable Long postId,
            @AuthenticatedMember Member member
    );

    @Operation(summary = "게시물 생성 API by 김준환",description = "게시물 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    @PostMapping
    CustomResponse<Long> createPost(
            @RequestBody PostRequest.PostCreateRequest postCreateRequest
    );

    @Operation(summary = "게시물 수정 API by 김준환",description = "게시물 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "POST404", description = "게시물을 찾지 못했습니다."),
            @ApiResponse(responseCode = "POST403", description = "자신의 게시물이 아닙니다.")
    })
    @PatchMapping("/{postId}")
    CustomResponse<Long> updatePost(
            @PathVariable Long postId,
            @RequestBody PostRequest.PostUpdateRequest postUpdateRequest,
            @AuthenticatedMember Member member
    );

    @Operation(summary = "게시물 삭제 API by 김준환",description = "게시물 삭제 (soft delete)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "POST404", description = "게시물을 찾지 못했습니다.")
    })
    @DeleteMapping("/{postId}")
    CustomResponse<Void> deletePost(
            @AuthenticatedMember Member member,
            @PathVariable Long postId
    );

    @Operation(summary = "게시물 좋아요 API by 김준환",description = "게시물 좋아요 생성/삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "POST404", description = "게시물을 찾지 못했습니다.")
    })
    @PostMapping("/{postId}/likes")
    CustomResponse<Void> likePost(
            @PathVariable Long postId,
            @AuthenticatedMember Member member
    );

    @Operation(summary = "게시물 신고 API by 김준환",description = "게시물 신고 생성/삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "POST404", description = "게시물을 찾지 못했습니다.")
    })
    @PostMapping("/{postId}/reports")
    CustomResponse<Void> reportPost(
            @PathVariable Long postId,
            @AuthenticatedMember Member member
    );

    @Operation(summary = "내가 쓴 게시물 조회 API by 김준환",description = "내가 쓴 게시물 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    @GetMapping("/me")
    CustomResponse<PostResponse.PostPreviewListResponse> getMyPost(
            @RequestParam(name = "cursor") Long cursor,
            @RequestParam(name = "size") Integer size,
            @AuthenticatedMember Member member
    );

    @Operation(summary = "내가 좋아요 누른 게시물 조회 API by 김준환",description = "내가 좋아요 누른 게시물 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    @GetMapping("/me/likes")
    CustomResponse<PostResponse.PostPreviewListResponse> getMyLikePost(
            @RequestParam(name = "cursor") Long cursor,
            @RequestParam(name = "size") Integer size,
            @AuthenticatedMember Member member
    );
}
