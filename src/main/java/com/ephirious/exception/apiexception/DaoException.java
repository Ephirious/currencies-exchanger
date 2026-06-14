package com.ephirious.exception.apiexception;

public class DaoException extends BaseApiException {
    private static final int DEFAULT_SERVER_EXCEPTION_STATUS_CODE = 500;

    public DaoException(String message) {
        super(DEFAULT_SERVER_EXCEPTION_STATUS_CODE, message);
    }

    public DaoException(int statusCode, String message) {
        super(statusCode, message);
    }
}
