package com.ephirious.exception.apiexception.service.currency;

import com.ephirious.config.HttpStatusCode;
import com.ephirious.exception.apiexception.BaseApiException;

public class NotFoundException extends BaseApiException {
    public NotFoundException(String message) {
        super(HttpStatusCode.NOT_FOUND.getStatusCode(), message);
    }
}
