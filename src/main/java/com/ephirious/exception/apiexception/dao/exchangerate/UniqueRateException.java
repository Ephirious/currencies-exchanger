package com.ephirious.exception.apiexception.dao.exchangerate;

import com.ephirious.config.HttpStatusCode;
import com.ephirious.exception.apiexception.dao.DaoException;

public class UniqueRateException extends DaoException {
    public UniqueRateException(String message) {
        super(HttpStatusCode.CONFLICT.getStatusCode(), message);
    }
}
