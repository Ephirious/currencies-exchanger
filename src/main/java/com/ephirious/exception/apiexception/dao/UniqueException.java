package com.ephirious.exception.apiexception.dao;

import com.ephirious.config.HttpStatusCode;

public class UniqueException extends DaoException {
    public UniqueException(String message) {
        super(HttpStatusCode.CONFLICT.getStatusCode(), message);
    }
}
