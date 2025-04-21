package com.example.harumeonglog.domain.comment.service;

import com.example.harumeonglog.domain.comment.converter.CommentConverter;
import com.example.harumeonglog.domain.comment.dto.response.CommentResponse;
import com.example.harumeonglog.domain.comment.entity.Comment;
import com.example.harumeonglog.domain.comment.repository.CommentRepository;
import com.example.harumeonglog.domain.post.entity.Post;
import com.example.harumeonglog.domain.post.repository.PostRepository;
import com.example.harumeonglog.global.error.code.PostErrorCode;
import com.example.harumeonglog.global.error.exception.PostException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentQueryServiceImpl implements CommentQueryService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Override
    public CommentResponse.CommentPreviewListResponse getComments(Long postId, Long cursor, Integer size) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostException(PostErrorCode.NOT_FOUND));

        if (cursor == 0) {
            cursor = Long.MAX_VALUE;
        }

        Slice<Comment> commentSlice = commentRepository.findCommentSliceByPost(post, cursor, PageRequest.of(0, size));

        List<Comment> commentList = commentSlice.toList();

        Long nextCursor = null;
        if (!commentList.isEmpty() && commentSlice.hasNext()) {
            nextCursor = commentList.get(commentList.size() - 1).getId();
        }

        return CommentConverter.toCommentPreviewListResponse(commentList, commentSlice.hasNext(), nextCursor);
    }
}
