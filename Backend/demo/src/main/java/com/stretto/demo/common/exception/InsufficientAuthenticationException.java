package com.stretto.demo.common.exception;

public class InsufficientAuthenticationException extends RuntimeException {
    public InsufficientAuthenticationException(String message) {
        super(message);
    }
}
