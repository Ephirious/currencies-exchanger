package com.ephirious.exception.apiexception.dao;

import com.ephirious.config.HttpStatusCode;

public class NullFieldException extends DaoException {
    public NullFieldException(String message) {
        super(HttpStatusCode.BAD_REQUEST.getStatusCode(), message);
    }
}
