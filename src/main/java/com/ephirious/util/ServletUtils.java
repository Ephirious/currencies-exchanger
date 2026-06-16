package com.ephirious.util;

import com.ephirious.exception.apiexception.servlet.ParameterNullException;
import com.ephirious.exception.apiexception.servlet.UnexpectedContentTypeException;
import jakarta.servlet.http.HttpServletRequest;

public final class ServletUtils {
    private ServletUtils() {

    }

    public static boolean isContentType(String contentType, String expected) {
        if (expected == null) {
            return false;
        }

        return contentType
                .toLowerCase()
                .startsWith(expected);
    }

    public static void ensureContentType(String contentType, String expected) {
        if (contentType == null) {
            throw new UnexpectedContentTypeException("Ожидается следующий тип контента: %s".formatted(expected));
        }
        if (!isContentType(contentType, expected)){
            String message = "Непподерживаемый формат запроса. Ожидается %s, получен %s)"
                    .formatted(expected, contentType);
            throw new UnexpectedContentTypeException(message);
        }
    }

    public static String getParamOrThrow(HttpServletRequest request, String paramName) {
        String param = request.getParameter(paramName);
        if (param == null) {
            throw new ParameterNullException("Параметр с именем %s не обнаружен".formatted(paramName));
        }
        return param;
    }
}
