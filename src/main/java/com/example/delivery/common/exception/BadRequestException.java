package com.example.delivery.common.exception;

import com.example.delivery.common.exception.enums.ErrorCode;
import lombok.Getter;

@Getter
public class BadRequestException extends CustomException {
    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BadRequestException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
