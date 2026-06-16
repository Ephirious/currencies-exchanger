package com.ephirious.exception.apiexception.dao.currency;

import com.ephirious.config.HttpStatusCode;
import com.ephirious.exception.apiexception.dao.DaoException;

public class CurrencyNullFieldException extends DaoException {
    public CurrencyNullFieldException(String message) {
        super(HttpStatusCode.BAD_REQUEST.getStatusCode(), message);
    }
}
