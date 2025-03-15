package com.vutran0943.delivery_service.exceptions;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    private int code;
    private int status;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.status = errorCode.getStatus();
    }
}
