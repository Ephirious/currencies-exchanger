package com.ephirious.exception.environment;

public class DatabaseMismatchException extends RuntimeException {
    public DatabaseMismatchException(String message) {
        super(message);
    }
}
