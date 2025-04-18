package com.example.harumeonglog.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommentErrorCode implements BaseErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT404", "댓글을 찾지 못했습니다."),
    NOT_OWN(HttpStatus.FORBIDDEN, "COMMENT403", "자신의 댓글이 아닙니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
