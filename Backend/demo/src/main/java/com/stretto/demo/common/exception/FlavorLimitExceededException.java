package com.stretto.demo.common.exception;

public class FlavorLimitExceededException extends RuntimeException {
    public FlavorLimitExceededException(String message) {
        super(message);
    }
}
