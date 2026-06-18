package com.ephirious.util;

import com.ephirious.exception.apiexception.servlet.InvalidParameterException;

public class CurrencyValidator {
    private final static int VALID_CODE_LENGTH = 3;
    private final static int VALID_SIGN_LENGTH = 1;
    private final static int VALID_NAME_LENGTH = 64;

    public static boolean isValidCode(String code) {
        if (code == null || code.length() != VALID_CODE_LENGTH) {
            return false;
        }
        return code.chars()
                .allMatch(CurrencyValidator::isLatinUpper);
    }

    public static void ensureCode(String code) {
        if (!CurrencyValidator.isValidCode(code) || code.isBlank()) {
            throw new InvalidParameterException("Неверный формат кода для следующей валюты: %s".formatted(code));
        }
    }

    public static void ensureSign(String sign) {
        if (sign == null || sign.length() > VALID_SIGN_LENGTH || sign.isBlank()) {
            throw new InvalidParameterException("Длина знака валюты '%s' должна не более 44 символов".formatted(sign));
        }
    }

    public static void ensureCurrencyName(String name) {
        if (name == null || name.length() > VALID_NAME_LENGTH || name.isBlank()) {
            throw new InvalidParameterException("Длина названия валюты '%s' должна не более 64 символов".formatted(name));
        }
    }

    private static boolean isLatinUpper(int letter) {
        return letter >= 'A' && letter <= 'Z';
    }
}
