package com.ephirious.controller;

import com.ephirious.config.ServletsConfig;
import com.ephirious.container.ApplicationContainer;
import com.ephirious.exception.ApiException.CurrencyIncorrectCodeException;
import com.ephirious.listener.ApplicationContext;
import com.ephirious.services.CurrencyService;
import com.ephirious.util.CurrencyValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private CurrencyService currencyService;
    private ObjectMapper mapper;

    @Override
    public void init() throws ServletException {
        ApplicationContainer container = (ApplicationContainer) getServletContext()
                .getAttribute(ApplicationContext.APPLICATION_ATTRIBUTE);

        currencyService = container.get(CurrencyService.class);
        mapper = container.get(ObjectMapper.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(ServletsConfig.JSON_CONTENT_TYPE.getSetting());
        response.setCharacterEncoding(ServletsConfig.ENCODING.getSetting());

        String code = request.getPathInfo().replace("/", "");
        CurrencyValidator.ensureCode(code);

        mapper.writeValue(response.getOutputStream(), currencyService.getCurrency(code));
    }
}
