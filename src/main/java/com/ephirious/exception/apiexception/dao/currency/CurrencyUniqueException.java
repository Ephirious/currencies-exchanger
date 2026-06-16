package com.ephirious.exception.apiexception.dao.currency;

import com.ephirious.config.HttpStatusCode;
import com.ephirious.exception.apiexception.dao.DaoException;

public class CurrencyUniqueException extends DaoException {
    public CurrencyUniqueException(String message) {
        super(HttpStatusCode.CONFLICT.getStatusCode(), message);
    }
}
