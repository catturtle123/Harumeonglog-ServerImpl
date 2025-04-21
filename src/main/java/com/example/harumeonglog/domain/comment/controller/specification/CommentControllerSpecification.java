package com.example.harumeonglog.domain.comment.controller.specification;

import com.example.harumeonglog.domain.comment.dto.response.CommentResponse;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.global.security.annotation.AuthenticatedMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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
            @RequestParam Integer size
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
}
