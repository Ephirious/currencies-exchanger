package com.ephirious.exception.apiexception.servlet;

import com.ephirious.config.HttpStatusCode;
import com.ephirious.exception.apiexception.BaseApiException;

public class UnexpectedContentTypeException extends BaseApiException {
    public UnexpectedContentTypeException(String message) {
        super(HttpStatusCode.UNSUPPORTED_MEDIA_TYPE.getStatusCode(), message);
    }
}
