package com.example.harumeonglog.domain.comment.controller;

import com.example.harumeonglog.domain.comment.dto.request.CommentRequest;
import com.example.harumeonglog.domain.comment.dto.response.CommentResponse;
import com.example.harumeonglog.domain.comment.entity.Comment;
import com.example.harumeonglog.domain.comment.service.CommentService;
import com.example.harumeonglog.global.common.response.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/posts/{postId}/comments")
    public CustomResponse<CommentResponse.CommentListResponse> getComments(
            @PathVariable Long postId,
            @RequestParam(required = false) Integer cursor,
            @RequestParam(required = false) Integer size
    ) {
        Slice<Comment> commentSlice = commentService.getComments(postId, cursor, size);
        Long nextCursor = commentSlice.toList().get(commentSlice.getSize() - 1).getId();
        return CustomResponse.ok(null);
    }

    @PostMapping("/comments/{commentId}/reports")
    public CustomResponse<Void> reportComment(@PathVariable Long commentId) {
        commentService.reportComment(commentId);
        return CustomResponse.ok(null);
    }

    @PostMapping("/comments/{commentId}/blocks")
    public CustomResponse<Void> blockComment(@PathVariable Long commentId) {
        commentService.blockComment(commentId);
        return CustomResponse.ok(null);
    }

    @PostMapping("/comments")
    public CustomResponse<Long> createComment(
        @RequestBody CommentRequest.CommentCreateRequest commentCreateRequest
    ) {
        Comment comment = commentService.createComment(commentCreateRequest);
        return CustomResponse.ok(comment.getId());
    }

    @DeleteMapping("/comments/{commentId}")
    public CustomResponse<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return CustomResponse.ok(null);
    }

}
