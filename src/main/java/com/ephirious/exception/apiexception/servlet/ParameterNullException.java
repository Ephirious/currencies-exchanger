package com.ephirious.exception.apiexception.servlet;

import com.ephirious.config.HttpStatusCode;
import com.ephirious.exception.apiexception.BaseApiException;

public class ParameterNullException extends BaseApiException {
    public ParameterNullException(String message) {
        super(HttpStatusCode.BAD_REQUEST.getStatusCode(), message);
    }
}
