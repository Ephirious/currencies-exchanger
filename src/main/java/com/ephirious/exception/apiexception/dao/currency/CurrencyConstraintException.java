package com.ephirious.exception.apiexception.dao.currency;

import com.ephirious.config.HttpStatusCode;
import com.ephirious.exception.apiexception.dao.DaoException;

public class CurrencyConstraintException extends DaoException {
    public CurrencyConstraintException(String message) {
        super(HttpStatusCode.BAD_REQUEST.getStatusCode(), message);
    }
}
