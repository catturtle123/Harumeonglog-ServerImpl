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
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommentController implements CommentControllerSpecification {

    private final CommentCommandService commentCommandService;
    private final CommentQueryService commentQueryService;

    @GetMapping("/posts/{postId}/comments")
    public CustomResponse<CommentResponse.CommentListResponse> getComments(
            @PathVariable Long postId,
            @RequestParam(required = false) Integer cursor,
            @RequestParam(required = false) Integer size
    ) {
        Slice<Comment> commentSlice = commentQueryService.getComments(postId, cursor, size);
        Long nextCursor = commentSlice.toList().get(commentSlice.getSize() - 1).getId();
        return CustomResponse.ok(null);
    }

    @PostMapping("/comments/{commentId}/reports")
    public CustomResponse<Void> reportComment(@PathVariable Long commentId) {
        commentCommandService.reportComment(commentId);
        return CustomResponse.ok(null);
    }

    @PostMapping("/comments/{commentId}/blocks")
    public CustomResponse<Void> blockComment(@PathVariable Long commentId) {
        commentCommandService.blockComment(commentId);
        return CustomResponse.ok(null);
    }

    @PostMapping("/comments")
    public CustomResponse<Long> createComment(
        @RequestBody CommentRequest.CommentCreateRequest commentCreateRequest
    ) {
        Comment comment = commentCommandService.createComment(commentCreateRequest);
        return CustomResponse.ok(comment.getId());
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
