package com.ephirious.exception.apiexception.dao;

import com.ephirious.config.HttpStatusCode;

public class IncorrectFormatException extends DaoException {
    public IncorrectFormatException(String message) {
        super(HttpStatusCode.BAD_REQUEST.getStatusCode(), message);
    }
}
