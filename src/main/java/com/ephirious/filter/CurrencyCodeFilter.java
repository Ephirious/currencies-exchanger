package com.ephirious.filter;

import com.ephirious.exception.ApiException.CurrencyIncorrectCodeException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CurrencyCodeFilter extends HttpFilter {
    private static final String CODE_REGEX = "^/[A-Z]{3}";

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String path = request.getPathInfo();

        if (!path.matches(CODE_REGEX)) {
            throw new CurrencyIncorrectCodeException(path.replaceFirst("/", ""));
        }

        chain.doFilter(request, response);
    }
}
