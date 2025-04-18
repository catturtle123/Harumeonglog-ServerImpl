package com.example.harumeonglog.domain.comment.controller.specification;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.global.security.annotation.AuthenticatedMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Comment", description = "comment 관련 API")
public interface CommentControllerSpecification {

    @Operation(summary = "Comment 삭제 API", description = "comment 삭제합니다.")
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
