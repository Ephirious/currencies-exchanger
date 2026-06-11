package com.ephirious.container;

import java.util.HashMap;
import java.util.Map;

public class ApplicationContainer {
    private static final String NULL_CLASS_EXCEPTION_MESSAGE = "Был передан некорректный класс или null";

    private final Map<Class<?>, Object> values;

    public ApplicationContainer() {
        values = new HashMap<>();
    }

    public <T> void put(Class<T> type, T object) {
        if (type == null) {
            throw new NullPointerException(NULL_CLASS_EXCEPTION_MESSAGE);
        }
        values.put(type, type.cast(object));
    }

    public <T> T get(Class<T> type) {
        if (type == null) {
            throw new NullPointerException(NULL_CLASS_EXCEPTION_MESSAGE);
        }
        return type.cast(values.get(type));
    }
}
