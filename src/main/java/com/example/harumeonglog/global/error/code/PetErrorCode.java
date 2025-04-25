package com.example.harumeonglog.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PetErrorCode implements BaseErrorCode{
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "PETIMAGE404", "반려견 이미지를 찾지 못했습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "PET404", "반려견을 찾지 못했습니다."),
    NOT_IN_GROUP(HttpStatus.BAD_REQUEST, "MEMBERPET400", "해당 반려견 그룹에 속하지 않았습니다."),
    NOT_ALLOWED_ROLE(HttpStatus.FORBIDDEN, "MEMBERPET403", "해당 기능을 수행할 권한이 없습니다."),
    ALREADY_INVITED(HttpStatus.BAD_REQUEST, "INVITE400", "해당 유저는 이미 초대된 상태입니다."),
    INVALID_ROLE(HttpStatus.BAD_REQUEST, "PETROLE400", "존재하지 않는 권한입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
