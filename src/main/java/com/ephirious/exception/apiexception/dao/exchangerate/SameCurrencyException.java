package com.ephirious.exception.apiexception.dao.exchangerate;

import com.ephirious.config.HttpStatusCode;
import com.ephirious.exception.apiexception.dao.DaoException;

public class SameCurrencyException extends DaoException {
    public SameCurrencyException(String message) {
        super(HttpStatusCode.BAD_REQUEST.getStatusCode(), message);
    }
}
