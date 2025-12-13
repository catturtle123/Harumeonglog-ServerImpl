package com.example.harumeonglog.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommentErrorCode implements BaseErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT404", "댓글을 찾지 못했습니다."),
    NOT_OWN(HttpStatus.FORBIDDEN, "COMMENT403", "자신의 댓글이 아닙니다."),
    IS_ALREADY(HttpStatus.BAD_REQUEST, "COMMENT400", "이미 차단된 댓글입니다."),
    NOT_PARENT(HttpStatus.NOT_FOUND, "COMMENT404", "부모의 댓글이 존재하지 않습니다."),
    OWN_COMMENT_LIKE(HttpStatus.BAD_REQUEST, "COMMENT400", "내 댓글은 좋아요를 누를 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
