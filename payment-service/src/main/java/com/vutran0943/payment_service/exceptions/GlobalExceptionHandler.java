package com.vutran0943.payment_service.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<AppException> handleRuntimeException(RuntimeException e) {
        AppException appException = new AppException(ErrorCode.UNCATEGORIZED_ERROR);

        return ResponseEntity.status(appException.getStatus()).body(appException);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(500).body(e.getMessage());
    }
}
