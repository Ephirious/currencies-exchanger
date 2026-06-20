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
        if (rate.chars().filter(ch -> ch == '.').count() > 1 ||
            rate.charAt(0) == '.' || rate.charAt(rate.length() - 1) == '.') {
            throw new InvalidParameterException("Курс не должен содержать более одной точки. Точка не должна быть началом и концом");
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
