package com.ephirious.config;

public enum ServletsConfig {
    JSON_CONTENT_TYPE("application/json"),
    ENCODING("UTF-8");

    private final String setting;

    ServletsConfig(String setting) {
        this.setting = setting;
    }

    public String getSetting() {
        return setting;
    }
}
