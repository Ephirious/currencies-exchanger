package com.ephirious.exception.apiexception;

public abstract class BaseApiException extends RuntimeException {
    private final int statusCode;

    public BaseApiException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getCode() {
        return statusCode;
    }
}
