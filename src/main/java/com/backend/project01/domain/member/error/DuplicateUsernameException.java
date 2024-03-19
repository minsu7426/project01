package com.backend.project01.domain.member.error;

import com.backend.project01.global.error.exception.BusinessException;
import com.backend.project01.global.error.exception.ErrorCode;

public class DuplicateUsernameException extends BusinessException {

    public DuplicateUsernameException(ErrorCode errorCode) {
        super(errorCode);
    }
}
