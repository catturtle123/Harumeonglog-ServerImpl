package com.example.harumeonglog.domain.comment.service;

import com.example.harumeonglog.domain.comment.converter.CommentBlockConverter;
import com.example.harumeonglog.domain.comment.converter.CommentReportConverter;
import com.example.harumeonglog.domain.comment.dto.request.CommentRequest;
import com.example.harumeonglog.domain.comment.entity.Comment;
import com.example.harumeonglog.domain.comment.entity.CommentBlock;
import com.example.harumeonglog.domain.comment.entity.CommentReport;
import com.example.harumeonglog.domain.comment.repository.CommentBlockRepository;
import com.example.harumeonglog.domain.comment.repository.CommentReportRepository;
import com.example.harumeonglog.domain.comment.repository.CommentRepository;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.global.error.code.CommentErrorCode;
import com.example.harumeonglog.global.error.exception.CommentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentCommandServiceImpl implements CommentCommandService {

    private final CommentRepository commentRepository;
    private final CommentReportRepository commentReportRepository;
    private final CommentBlockRepository commentBlockRepository;

    @Override
    public void reportComment(Long commentId, Member member) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentException(CommentErrorCode.NOT_FOUND));

        CommentReport commentReport = commentReportRepository.findByCommentAndMember(comment, member);

        if (commentReport != null) {
            comment.fixReportNum(-1L);
            commentReportRepository.delete(commentReport);
        } else {
            comment.fixReportNum(1L);
            commentReportRepository.save(CommentReportConverter.toCommentReport(comment, member));
        }

    }

    @Override
    public void blockComment(Long commentId, Member member) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentException(CommentErrorCode.NOT_FOUND));

        commentBlockRepository.save(CommentBlockConverter.toCommentBlock(comment, member));
    }

    @Override
    public Comment createComment(CommentRequest.CommentCreateRequest commentCreateRequest) {
        return null;
    }

    @Override
    public void deleteComment(Long commentId, Member member) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentException(CommentErrorCode.NOT_FOUND));

        if (comment.getMember().equals(member)) {
            throw new CommentException(CommentErrorCode.NOT_OWN);
        }

        comment.delete();
    }

    @Override
    public void likeComment(Long commentId) {

    }
}
