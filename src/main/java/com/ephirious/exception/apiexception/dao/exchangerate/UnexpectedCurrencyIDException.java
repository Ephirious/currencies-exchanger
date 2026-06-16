package com.ephirious.exception.apiexception.dao.exchangerate;

import com.ephirious.config.HttpStatusCode;
import com.ephirious.exception.apiexception.dao.DaoException;

public class UnexpectedCurrencyIDException extends DaoException {
    public UnexpectedCurrencyIDException(String message) {
        super(HttpStatusCode.BAD_REQUEST.getStatusCode(), message);
    }
}
