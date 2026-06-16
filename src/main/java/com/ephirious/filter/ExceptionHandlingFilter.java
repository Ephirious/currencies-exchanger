package com.ephirious.filter;

import com.ephirious.config.HttpStatusCode;
import com.ephirious.config.ServletsConfig;
import com.ephirious.container.ApplicationContainer;
import com.ephirious.exception.apiexception.BaseApiException;
import com.ephirious.listener.ApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

public class ExceptionHandlingFilter extends HttpFilter {
    private ObjectMapper mapper;

    @Override
    public void init() throws ServletException {
        ApplicationContainer container = (ApplicationContainer) getServletContext().getAttribute(ApplicationContext.APPLICATION_ATTRIBUTE);
        mapper = container.get(ObjectMapper.class);
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.setCharacterEncoding(ServletsConfig.ENCODING.getSetting());
        response.setContentType(ServletsConfig.JSON_CONTENT_TYPE.getSetting());

        try {
            chain.doFilter(request, response);
        } catch (BaseApiException apiException) {
            writeExceptionResponse(response, apiException, apiException.getCode());
        }
        catch (RuntimeException exception) {
            writeExceptionResponse(response, exception, HttpStatusCode.INTERNAL_SERVET_ERROR.getStatusCode());
        }
    }

    private void writeExceptionResponse(HttpServletResponse response, Exception exception, int statusCode) throws IOException {
        response.setStatus(statusCode);
        String exceptionMessage = exception.getMessage() != null ? exception.getMessage() : "Server Internal Server";
        mapper.writeValue(response.getOutputStream(), Map.of("Error", exceptionMessage));
    }
}
