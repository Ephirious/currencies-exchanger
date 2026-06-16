package com.ephirious.controller;

import com.ephirious.config.ServletsConfig;
import com.ephirious.container.ApplicationContainer;
import com.ephirious.listener.ApplicationContext;
import com.ephirious.services.ExchangeRateService;
import com.ephirious.util.CurrencyValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private static final int ENDPOINT_LENGTH = 6;

    private static final int START_INDEX_FIRST_CODE = 0;
    private static final int END_INDEX_FIRST_CODE = 3;
    private static final int START_INDEX_SECOND_CODE = 3;
    private static final int END_INDEX_SECOND_CODE = 6;

    private ExchangeRateService exchangeRateService;
    private ObjectMapper mapper;

    @Override
    public void init() throws ServletException {
        ApplicationContainer container = (ApplicationContainer) getServletContext()
                .getAttribute(ApplicationContext.APPLICATION_ATTRIBUTE);

        exchangeRateService = container.get(ExchangeRateService.class);
        mapper = container.get(ObjectMapper.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(ServletsConfig.JSON_CONTENT_TYPE.getSetting());
        response.setCharacterEncoding(ServletsConfig.ENCODING.getSetting());

        String endpoint = request.getPathInfo().replace("/", "");
        ensureEndPoint(endpoint);

        String base = endpoint.substring(START_INDEX_FIRST_CODE, END_INDEX_FIRST_CODE);
        String target = endpoint.substring(START_INDEX_SECOND_CODE, END_INDEX_SECOND_CODE);

        ensureCurrencyCodes(base, target);

        mapper.writeValue(response.getOutputStream(), exchangeRateService.getExchangeRate(base, target));
    }

    private void ensureEndPoint(String endpoint) {
        if (endpoint.length() != ENDPOINT_LENGTH) {
            throw new RuntimeException("Некорректный формат ключей валют"); // Исправить исключения на кастомные
        }
    }

    private void ensureCurrencyCodes(String base, String target) {
        CurrencyValidator.ensureCode(base);
        CurrencyValidator.ensureCode(target);
    }
}
