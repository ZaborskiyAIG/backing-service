package com.backing.service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> globalException(RuntimeException exception) {
        log.error("Error call Rest request: ", exception);
        return ResponseEntity.internalServerError().body(exception.getMessage());
    }

    @ExceptionHandler(EntityNotExistException.class)
    public ResponseEntity<Object> entityNotExistException(EntityNotExistException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(BalanceException.class)
    public ResponseEntity<Object> balanceException(BalanceException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(PinCodeNotEqualsException.class)
    public ResponseEntity<Object> pinCodeNotEqualsException(PinCodeNotEqualsException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> onMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        StringBuilder stringBuilder = new StringBuilder();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            stringBuilder.append(fieldError.getField())
                    .append(": ")
                    .append(fieldError.getDefaultMessage())
                    .append("\n");
        }
        return ResponseEntity.badRequest().body(stringBuilder.toString());
    }
}