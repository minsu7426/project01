package com.backend.project01.global.error.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    //common
    INTERVAL_SERVER_ERROR(500, "서버 오류입니다."),
    INVALID_INPUT_VALUE(400, "잘못된 입력입니다."),

    //member
    DUPLICATE_USERNAME(400, "중복된 이름입니다."),
    USER_NOT_FOUND(400, "존재하지 않는 회원입니다.")
    ;

    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
