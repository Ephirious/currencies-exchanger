package com.ephirious.exception;

public class CurrencyNotFoundException extends RuntimeException {
    private static final String message = "Валюта по коду %s не была найдена";

    public CurrencyNotFoundException(String code) {
        super(message.formatted(code));
    }
}
