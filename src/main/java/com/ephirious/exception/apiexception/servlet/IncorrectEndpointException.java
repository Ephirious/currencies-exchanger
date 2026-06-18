package com.ephirious.exception.apiexception.servlet;

import com.ephirious.config.HttpStatusCode;
import com.ephirious.exception.apiexception.BaseApiException;

public class IncorrectEndpointException extends BaseApiException {
    public IncorrectEndpointException(String message) {
        super(HttpStatusCode.BAD_REQUEST.getStatusCode(), message);
    }
}
