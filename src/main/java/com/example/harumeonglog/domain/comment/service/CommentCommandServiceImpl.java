package com.example.harumeonglog.domain.comment.service;

import com.example.harumeonglog.domain.comment.converter.CommentBlockConverter;
import com.example.harumeonglog.domain.comment.converter.CommentConverter;
import com.example.harumeonglog.domain.comment.converter.CommentLikeConverter;
import com.example.harumeonglog.domain.comment.converter.CommentReportConverter;
import com.example.harumeonglog.domain.comment.dto.request.CommentRequest;
import com.example.harumeonglog.domain.comment.dto.response.CommentResponse;
import com.example.harumeonglog.domain.comment.entity.Comment;
import com.example.harumeonglog.domain.comment.entity.CommentLike;
import com.example.harumeonglog.domain.comment.entity.CommentReport;
import com.example.harumeonglog.domain.comment.repository.CommentBlockRepository;
import com.example.harumeonglog.domain.comment.repository.CommentLikeRepository;
import com.example.harumeonglog.domain.comment.repository.CommentReportRepository;
import com.example.harumeonglog.domain.comment.repository.CommentRepository;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.post.entity.Post;
import com.example.harumeonglog.domain.post.repository.PostRepository;
import com.example.harumeonglog.global.error.code.CommentErrorCode;
import com.example.harumeonglog.global.error.code.PostErrorCode;
import com.example.harumeonglog.global.error.exception.CommentException;
import com.example.harumeonglog.global.error.exception.PostException;
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
    private final CommentLikeRepository commentLikeRepository;
    private final PostRepository postRepository;

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

        if (commentBlockRepository.findByMemberAndComment(member, comment).isPresent()) {
            throw new CommentException(CommentErrorCode.IS_ALREADY);
        }

        commentBlockRepository.save(CommentBlockConverter.toCommentBlock(comment, member));
    }

    @Override
    public CommentResponse.CommentCreateResponse createComment(CommentRequest.CommentCreateRequest commentCreateRequest, Long postId, Member member) {
        Post post = postRepository.findById(postId).orElseThrow(()-> new PostException(PostErrorCode.NOT_FOUND));

        Comment comment;
        if (commentCreateRequest.getParentId() == null) {
            comment = CommentConverter.toComment(commentCreateRequest, member);

            comment.addPost(post);

            commentRepository.save(comment);
        } else {
            Comment parent = commentRepository.findById(commentCreateRequest.getParentId()).orElseThrow(() -> new CommentException(CommentErrorCode.NOT_PARENT));

            comment = CommentConverter.toComment(commentCreateRequest, member);

            comment.addCommentComment(parent, post);

            commentRepository.save(comment);
        }

        return CommentConverter.toCommentCreateResponse(comment);
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
    public void likeComment(Long commentId, Member member) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentException(CommentErrorCode.NOT_FOUND));

        CommentLike commentLike = commentLikeRepository.findByCommentAndMember(comment, member);

        if (commentLike != null) {
            comment.fixLikeNum(-1L);
            commentLikeRepository.delete(commentLike);
        } else {
            comment.fixLikeNum(1L);
            commentLikeRepository.save(CommentLikeConverter.toCommentLike(comment, member));
        }

    }
}
