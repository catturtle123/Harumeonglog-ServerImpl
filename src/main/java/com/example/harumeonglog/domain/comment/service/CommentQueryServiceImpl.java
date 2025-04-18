package com.example.harumeonglog.domain.comment.service;

import com.example.harumeonglog.domain.comment.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentQueryServiceImpl implements CommentQueryService {

    @Override
    public Slice<Comment> getComments(Long postId, Integer cursor, Integer size) {
        return null;
    }
}
