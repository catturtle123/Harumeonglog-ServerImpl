package com.example.harumeonglog.domain.comment.service;

import com.example.harumeonglog.domain.comment.dto.request.CommentRequest;
import com.example.harumeonglog.domain.comment.entity.Comment;
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

    @Override
    public void reportComment(Long commentId) {

    }

    @Override
    public void blockComment(Long commentId) {

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
