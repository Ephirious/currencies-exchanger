package com.ephirious.config;

import lombok.Getter;

@Getter
public enum ServletsConfig {
    JSON_CONTENT_TYPE("application/json"),
    ENCODING("UTF-8"),
    X_WWW_FORM_URLENCODED_CONTENT_TYPE("application/x-www-form-urlencoded");

    private final String setting;

    ServletsConfig(String setting) {
        this.setting = setting;
    }
}
