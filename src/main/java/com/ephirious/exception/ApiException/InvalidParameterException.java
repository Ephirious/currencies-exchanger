package com.ephirious.exception.ApiException;

public class InvalidParameterException extends BaseApiException {
    private static final int DEFAULT_STATUS_CODE = 400;

    public InvalidParameterException(String message) {
        super(DEFAULT_STATUS_CODE, message);
    }
}
