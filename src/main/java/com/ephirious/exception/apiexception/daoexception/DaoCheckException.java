package com.ephirious.exception.apiexception.daoexception;

public class DaoCheckException extends DaoException {
    private static final int DEFAULT_STATUS_CODE = 400;
    private static final String DEFAULT_MESSAGE = "Недопустимые данные при вставке\\обновлении БД";

    public DaoCheckException() {
        super(DEFAULT_STATUS_CODE, DEFAULT_MESSAGE);
    }

    public DaoCheckException(int statusCode, String message) {
        super(statusCode, message);
    }
}
