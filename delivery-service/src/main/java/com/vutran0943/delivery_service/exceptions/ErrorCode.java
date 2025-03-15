package com.vutran0943.delivery_service.exceptions;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_ERROR(9999, 500, "Uncategorized Error"),
    SHIPMENT_NOTFOUND_ERROR(1001, 404, "Shipment does not exist"),
    SHIPMENT_STATUS_INVALID(1002, 400, "Shipment status invalid");

    private String message;
    private int code;
    private int status;

    ErrorCode(int code, int status, String message) {
        this.message = message;
        this.code = code;
        this.status = status;
    }
}
