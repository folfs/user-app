package com.demo.user_app.error;

public class CustServiceException extends RuntimeException {
    public CustServiceException(String message) {
        super(message);
    }
}