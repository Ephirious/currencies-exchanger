package com.ephirious.exception.apiexception.service.exchange;

import com.ephirious.config.HttpStatusCode;
import com.ephirious.exception.apiexception.BaseApiException;

public class ExchangeException extends BaseApiException {
    public ExchangeException(String message) {
        super(HttpStatusCode.NOT_FOUND.getStatusCode(), message);
    }
}
