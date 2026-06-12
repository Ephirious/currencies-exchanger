package com.ephirious.config;

import lombok.Getter;

@Getter
public enum HttpStatusCode {
    INTERNAL_SERVER_ERROR(500);

    private final int code;

    HttpStatusCode(int code) {
        this.code = code;
    }
}
