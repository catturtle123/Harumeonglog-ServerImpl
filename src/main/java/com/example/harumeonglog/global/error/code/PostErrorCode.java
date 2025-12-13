package com.example.harumeonglog.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PostErrorCode implements BaseErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "POST404", "게시물을 찾지 못했습니다."),
    NOT_OWN(HttpStatus.FORBIDDEN, "POST403", "자신의 게시물이 아닙니다."),
    OWN_POST_LIKE(HttpStatus.BAD_REQUEST, "POST400", "내 게시물은 좋아요를 누를 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
