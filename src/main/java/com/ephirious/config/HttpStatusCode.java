package com.ephirious.config;

import lombok.Getter;

@Getter
public enum HttpStatusCode {
    OK(200),
    CREATED(201),
    BAD_REQUEST(400),
    NOT_FOUND(404),
    CONFLICT(409),
    UNSUPPORTED_MEDIA_TYPE(415),
    INTERNAL_SERVET_ERROR(500);

    private final int statusCode;

    HttpStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
