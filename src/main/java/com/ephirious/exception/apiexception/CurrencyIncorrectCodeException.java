package com.ephirious.exception.apiexception;

public class CurrencyIncorrectCodeException extends BaseApiException {
    private static final int DEFAULT_STATUS_CODE = 400;
    private static final String DEFAULT_MESSAGE = "Неверный формат кода для следующей валюты: %s";

    public CurrencyIncorrectCodeException(String code) {
        super(DEFAULT_STATUS_CODE, DEFAULT_MESSAGE.formatted(code));
    }

    public CurrencyIncorrectCodeException(int statusCode, String message) {
        super(statusCode, message);
    }
}
