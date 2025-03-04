package com.vutran0943.payment_service.exceptions;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_ERROR(9999, 500, "Uncategorized Error"),
    INVALID_PAYMENT(1001, 400, "Invalid Payment");

    private int code;
    private int status;
    private String message;

    ErrorCode(int code, int status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }
}
