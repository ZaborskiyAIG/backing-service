package com.backing.service.exception;

public class PinCodeNotEqualsException extends RuntimeException {

    public PinCodeNotEqualsException(String message) {
        super(message);
    }
}
