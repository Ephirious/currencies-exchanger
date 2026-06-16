package com.ephirious.exception.initialize;

public class EnvironmentVariableNotFound extends RuntimeException {
    private static final String MESSAGE = "Переменная окружения с именем %s не найдена";

    public EnvironmentVariableNotFound(String variableName) {
        super(MESSAGE.formatted(variableName));
    }
}
