package com.example.harumeonglog.domain.comment.controller;

import com.example.harumeonglog.domain.comment.controller.specification.CommentControllerSpecification;
import com.example.harumeonglog.domain.comment.dto.request.CommentRequest;
import com.example.harumeonglog.domain.comment.dto.response.CommentResponse;
import com.example.harumeonglog.domain.comment.entity.Comment;
import com.example.harumeonglog.domain.comment.service.CommentCommandService;
import com.example.harumeonglog.domain.comment.service.CommentQueryService;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.global.security.annotation.AuthenticatedMember;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommentController implements CommentControllerSpecification {

    private final CommentCommandService commentCommandService;
    private final CommentQueryService commentQueryService;

    @GetMapping("/posts/{postId}/comments")
    public CustomResponse<CommentResponse.CommentPreviewListResponse> getComments(
            @PathVariable Long postId,
            @RequestParam Long cursor,
            @RequestParam Integer size,
            @AuthenticatedMember Member member
    ) {
        CommentResponse.CommentPreviewListResponse commentPreviewListResponse = commentQueryService.getComments(postId, cursor, size, member);
        return CustomResponse.ok(commentPreviewListResponse);
    }

    @PostMapping("/comments/{commentId}/reports")
    public CustomResponse<Void> reportComment(
            @PathVariable Long commentId,
            @AuthenticatedMember Member member
    ) {
        commentCommandService.reportComment(commentId, member);
        return CustomResponse.ok(null);
    }

    @PostMapping("/comments/{commentId}/blocks")
    public CustomResponse<Void> blockComment(
            @PathVariable Long commentId,
            @AuthenticatedMember Member member
    ) {
        commentCommandService.blockComment(commentId, member);
        return CustomResponse.ok(null);
    }

    @PostMapping("/comments/{commentId}/likes")
    public CustomResponse<Void> likeComment(
            @PathVariable Long commentId,
            @AuthenticatedMember Member member
    ) {
        commentCommandService.likeComment(commentId, member);
        return CustomResponse.ok(null);
    }

    @PostMapping("/posts/{postId}/comments")
    public CustomResponse<CommentResponse.CommentCreateResponse> createComment(
        @RequestBody CommentRequest.CommentCreateRequest commentCreateRequest,
        @PathVariable Long postId,
        @AuthenticatedMember Member member
    ) {
        CommentResponse.CommentCreateResponse commentCreateResponse = commentCommandService.createComment(commentCreateRequest, postId, member);
        return CustomResponse.ok(commentCreateResponse);
    }

    @DeleteMapping("/comments/{commentId}")
    public CustomResponse<Void> deleteComment(
            @AuthenticatedMember Member member,
            @PathVariable Long commentId
    ) {
        commentCommandService.deleteComment(commentId, member);
        return CustomResponse.ok(null);
    }

}
