package com.ephirious.exception.apiexception;

public class UnexpectedContentTypeException extends BaseApiException {
    private static final int DEFAULT_STATUS_CODE = 415;
    private static final String DEFAULT_EXCEPTION_MESSAGE = "Неподдерживаемый тип контетна '%s'. Ожидаемые тип '%s'";

    public UnexpectedContentTypeException(String invalid, String expected) {
        super(DEFAULT_STATUS_CODE, DEFAULT_EXCEPTION_MESSAGE.formatted(invalid, expected));
    }

    public UnexpectedContentTypeException(int statusCode, String message) {
        super(statusCode, message);
    }


}
