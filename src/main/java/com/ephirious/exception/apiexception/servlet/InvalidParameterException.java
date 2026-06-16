package com.ephirious.exception.apiexception.servlet;

import com.ephirious.config.HttpStatusCode;
import com.ephirious.exception.apiexception.BaseApiException;

public class InvalidParameterException extends BaseApiException {
    public InvalidParameterException(String message) {
        super(HttpStatusCode.BAD_REQUEST.getStatusCode(), message);
    }
}
