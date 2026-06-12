package com.ephirious.filter;

import com.ephirious.config.HttpStatusCode;
import com.ephirious.config.ServletsConfig;
import com.ephirious.container.ApplicationContainer;
import com.ephirious.listener.ApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@WebFilter("/*")
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
        } catch (RuntimeException exception) {
            response.setStatus(HttpStatusCode.INTERNAL_SERVER_ERROR.getCode());
            String exceptionMessage = exception.getMessage() != null ? exception.getMessage() : "Server Internal Server";
            try (ServletOutputStream outputStream = response.getOutputStream()) {
                mapper.writeValue(outputStream, Map.of("Error", exceptionMessage));
            }
        }
    }
}
