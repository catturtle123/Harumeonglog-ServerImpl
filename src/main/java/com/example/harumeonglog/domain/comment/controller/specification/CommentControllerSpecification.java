package com.example.harumeonglog.domain.comment.controller.specification;

import com.example.harumeonglog.domain.comment.dto.request.CommentRequest;
import com.example.harumeonglog.domain.comment.dto.response.CommentResponse;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.global.security.annotation.AuthenticatedMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Comment", description = "comment 관련 API")
public interface CommentControllerSpecification {

    @Operation(summary = "Comment 목록 조회 API by 김준환", description = "comment를 전체 조회합니다")
    @Parameter(name = "cursor", description = "0 이면 끝 값으로 올라감", example = "0")
    @Parameter(name = "size", description = "이만큼 가져옴", example = "10")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "POST404", description = "게시물을 찾지 못했습니다.")
    })
    @GetMapping("/posts/{postId}/comments")
    CustomResponse<CommentResponse.CommentPreviewListResponse> getComments(
            @PathVariable Long postId,
            @RequestParam Long cursor,
            @RequestParam Integer size,
            @AuthenticatedMember Member member
    );

    @Operation(summary = "Comment 삭제 API by 김준환", description = "comment 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "COMMENT404", description = "댓글을 찾지 못했습니다.")
    })
    @DeleteMapping("/comments/{commentId}")
    CustomResponse<Void> deleteComment(
            @AuthenticatedMember Member member,
            @PathVariable Long commentId
    );

    @Operation(summary = "Comment 신고 API by 김준환", description = "comment 신고합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "COMMENT404", description = "댓글을 찾지 못했습니다.")
    })
    @PostMapping("/comments/{commentId}/reports")
    CustomResponse<Void> reportComment(
            @PathVariable Long commentId,
            @AuthenticatedMember Member member
    );

    @Operation(summary = "Comment 차단 API by 김준환", description = "comment 차단합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "COMMENT400", description = "이미 차단된 댓글입니다."),
            @ApiResponse(responseCode = "COMMENT404", description = "댓글을 찾지 못했습니다.")
    })
    @PostMapping("/comments/{commentId}/blocks")
    CustomResponse<Void> blockComment(
            @PathVariable Long commentId,
            @AuthenticatedMember Member member
    );

    @Operation(summary = "Comment 좋아요 API by 김준환", description = "comment 좋아요 기능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "COMMENT404", description = "댓글을 찾지 못했습니다.")
    })
    @PostMapping("/comments/{commentId}/likes")
    CustomResponse<Void> likeComment(
            @PathVariable Long commentId,
            @AuthenticatedMember Member member
    );

    @Operation(summary = "Comment 생성 API by 김준환", description = "comment 생성합니다, parentId가 null이면 댓글 Long 값이 들어가면 대댓글(해당 parentId의 댓글에 종속되는)이 됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "POST404", description = "게시물을 찾지 못했습니다.")
    })
    @PostMapping("/posts/{postId}/comments")
    CustomResponse<CommentResponse.CommentCreateResponse> createComment(
            @RequestBody CommentRequest.CommentCreateRequest commentCreateRequest,
            @PathVariable Long postId,
            @AuthenticatedMember Member member
    );
}
