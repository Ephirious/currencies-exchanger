package com.ephirious.util;

import com.ephirious.exception.ApiException.CurrencyIncorrectCodeException;

public class CurrencyValidator {
    private final static int VALID_CODE_LENGTH = 3;

    public static boolean isValidCode(String code) {
        if (code == null || code.length() != VALID_CODE_LENGTH) {
            return false;
        }
        return code.chars()
                .allMatch(CurrencyValidator::isLatinUpper);
    }

    public static void ensureCode(String code) {
        if (!CurrencyValidator.isValidCode(code)) {
            throw new CurrencyIncorrectCodeException(code);
        }
    }

    private static boolean isLatinUpper(int letter) {
        return letter >= 'A' && letter <= 'Z';
    }
}
