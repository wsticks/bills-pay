package com.interswitch.user_management.exception;

public class CustomException extends RuntimeException {
    public CustomException(String msg, Throwable t) {
        super(msg, t);
    }

    public CustomException(String msg) {
        super(msg);
    }
}
