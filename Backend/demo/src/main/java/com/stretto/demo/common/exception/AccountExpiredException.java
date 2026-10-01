package com.stretto.demo.common.exception;

public class AccountExpiredException extends RuntimeException {
    public AccountExpiredException(String message) {
        super(message);
    }
}
