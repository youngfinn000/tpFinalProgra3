package com.stretto.demo.common.exception;

public class CredentialsExpiredException extends RuntimeException {
    public CredentialsExpiredException(String message) {
        super(message);
    }
}
