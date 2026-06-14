package com.stretto.demo.common.exception;

public class WholeSaleCustomerNotFoundException extends RuntimeException {
    public WholeSaleCustomerNotFoundException(String message) {
        super(message);
    }
}
