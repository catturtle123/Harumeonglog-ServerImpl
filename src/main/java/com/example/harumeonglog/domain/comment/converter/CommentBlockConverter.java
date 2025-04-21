package com.example.harumeonglog.domain.comment.converter;

import com.example.harumeonglog.domain.comment.entity.Comment;
import com.example.harumeonglog.domain.comment.entity.CommentBlock;
import com.example.harumeonglog.domain.member.entity.Member;

public class CommentBlockConverter {

    public static CommentBlock toCommentBlock(Comment comment, Member member) {
        return CommentBlock.builder()
                .member(member)
                .comment(comment)
                .build();
    }

}
