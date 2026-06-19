package com.ephirious.util;

import com.ephirious.exception.apiexception.servlet.InvalidParameterException;

public class CurrencyValidator {
    private final static int VALID_CODE_LENGTH = 3;
    private final static int VALID_SIGN_LENGTH = 1;
    private final static int VALID_NAME_LENGTH = 24;

    public static void ensureCode(String code) {
        if (code == null || code.isBlank()) {
            throw new InvalidParameterException("Код не должен быть пустым полем");
        }
        if (!code.chars().allMatch(CurrencyValidator::isLatinUpper)) {
            throw new InvalidParameterException("Код %s должен состоять из заглавных английских букв".formatted(code));
        }
        if (code.length() != VALID_CODE_LENGTH) {
            throw new InvalidParameterException("Длина кода должна быть равна %d".formatted(VALID_CODE_LENGTH));
        }
    }

    public static void ensureSign(String sign) {
        if (sign == null || sign.isBlank()) {
            throw new InvalidParameterException("Знак не должен быть пустым полем");
        }
        if (sign.length() > VALID_SIGN_LENGTH) {
            throw new InvalidParameterException("Длина знака валюты '%s' должна не более %d символов".formatted(sign, VALID_SIGN_LENGTH));
        }
    }

    public static void ensureCurrencyName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidParameterException("Название валюты не должно быть пустым полем");
        }
        if (name.length() > VALID_NAME_LENGTH) {
            throw new InvalidParameterException("Длина названия валюты '%s' должна не более %d символов".formatted(name, VALID_NAME_LENGTH));
        }
    }

    private static boolean isLatinUpper(int letter) {
        return letter >= 'A' && letter <= 'Z';
    }
}
