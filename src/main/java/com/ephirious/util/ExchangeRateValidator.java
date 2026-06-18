package com.ephirious.util;

import com.ephirious.exception.apiexception.servlet.InvalidParameterException;

public final class ExchangeRateValidator {
    public final static int RATE_MAX_PRECISION = 6;

    private ExchangeRateValidator() {

    }

    public static void ensureRate(String rate) {
        if (rate.isBlank() || !isAllDigits(rate)) {
            throw new InvalidParameterException("Курс должен состоять из цифр и разделён точкой");
        }
        if (!isValidRatePrecision(rate)) {
            throw new InvalidParameterException("Курс должен содержать на более 6 цифр после запятой");
        }
    }

    public static boolean isValidRatePrecision(String rate) {
        String[] parts = rate.split("\\.");
        if (parts.length <= 1) {
            return true;
        }
        return parts[1].length() <= RATE_MAX_PRECISION;
    }

    public static boolean isAllDigits(String rate) {
        return rate.chars()
                .allMatch(letter -> Character.isDigit(letter) || letter == '.');
    }
}
