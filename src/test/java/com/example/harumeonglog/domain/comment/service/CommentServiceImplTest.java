package com.example.harumeonglog.domain.comment.service;

import com.example.harumeonglog.domain.comment.controller.dto.request.CommentRequest;
import com.example.harumeonglog.domain.comment.controller.port.CommentService;
import com.example.harumeonglog.domain.comment.domain.Comment;
import com.example.harumeonglog.domain.comment.domain.CommentBlock;
import com.example.harumeonglog.domain.comment.domain.CommentLike;
import com.example.harumeonglog.domain.comment.domain.CommentReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Slice;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CommentServiceImplTest {

    private CommentService commentService;

    @BeforeEach
    void init() {
        this.commentService = new CommentServiceImpl();
    }

    @Test
    @DisplayName("댓글 목록을 불러올 수 있다.")
    void canGetComments() {
        // given

        //when
        Slice<Comment> comments = commentService.getComments(1L, 0, 10);

        //then
        assertThat(comments).isNull();
    }

    @Test
    @DisplayName("댓글 신고를 할 수 있다")
    void canReportComment() {
        //given
        CommentReport commentReport = new CommentReport();

        //when
        commentService.reportComment(1L);

        //then
    }

    @Test
    @DisplayName("댓글 삭제 할 수 있다")
    void canDeleteComment() {
        //given

        //when
        commentService.deleteComment(1L);

        //then
    }

    @Test
    @DisplayName("댓글 생성을 할 수 있다.")
    void canCreateComment() {
        //given
        CommentRequest.CommentCreateRequest commentCreateRequest = CommentRequest.CommentCreateRequest.builder()
                .content("comment content")
                .build();

        //when
        Comment comment = commentService.createComment(commentCreateRequest);

        //then
        assertThat(comment).isNull();
    }

    @Test
    @DisplayName("댓글 차단을 할 수 있다")
    void canBlockComment() {
        //given
        CommentBlock commentBlock = new CommentBlock();

        //when
        commentService.blockComment(1L);

        //then
    }

    @Test
    @DisplayName("댓글 좋아요를 할 수 있다")
    void canLikeComment() {
        //given
        CommentLike commentLike = new CommentLike();

        //when
        commentService.likeComment(1L);

        //then
    }

}