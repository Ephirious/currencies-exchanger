package com.ephirious.exception.apiexception.service.exchangerate;

import com.ephirious.config.HttpStatusCode;
import com.ephirious.exception.apiexception.BaseApiException;

public class NotExistRateException extends BaseApiException {
    public NotExistRateException(String message) {
        super(HttpStatusCode.NOT_FOUND.getStatusCode(), message);
    }
}
