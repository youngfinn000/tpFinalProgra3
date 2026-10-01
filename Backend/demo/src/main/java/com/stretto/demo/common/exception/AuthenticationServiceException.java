package com.stretto.demo.common.exception;

public class AuthenticationServiceException extends RuntimeException {
    public AuthenticationServiceException(String message) {
        super(message);
    }
}
