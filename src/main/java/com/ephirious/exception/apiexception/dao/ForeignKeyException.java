package com.ephirious.exception.apiexception.dao;

import com.ephirious.config.HttpStatusCode;

public class ForeignKeyException extends DaoException {
    public ForeignKeyException(String message) {
        super(HttpStatusCode.BAD_REQUEST.getStatusCode(), message);
    }
}
