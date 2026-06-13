package com.ephirious.exception.ApiException;

public class CurrencyNotFoundException extends BaseApiException {
    private static final String DEFAULT_MESSAGE = "Валюта по коду %s не была найдена";
    private static final int DEFAULT_STATUS_CODE = 400;

    public CurrencyNotFoundException(String code) {
        super(DEFAULT_STATUS_CODE, DEFAULT_MESSAGE.formatted(code));
    }

    public CurrencyNotFoundException(int statusCode, String message) {
        super(statusCode, message);
    }
}
