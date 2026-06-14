package com.ephirious.exception.apiexception.daoexception;

public class DaoUniqueException extends DaoException {
    private static final int DEFAULT_STATUS_CODE = 409;
    private static final String DEFAULT_MESSAGE = "Такая валюты уже существует";

    public DaoUniqueException() {
        super(DEFAULT_STATUS_CODE, DEFAULT_MESSAGE);
    }

    public DaoUniqueException(int statusCode, String message) {
        super(statusCode, message);
    }
}
