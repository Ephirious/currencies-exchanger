package com.ephirious.controller;

import com.ephirious.config.HttpStatusCode;
import com.ephirious.config.ServletsConfig;
import com.ephirious.container.ApplicationContainer;
import com.ephirious.exception.apiexception.servlet.IncorrectEndpointException;
import com.ephirious.listener.ApplicationContext;
import com.ephirious.services.ExchangeRateService;
import com.ephirious.util.CurrencyValidator;
import com.ephirious.util.ExchangeRateValidator;
import com.ephirious.util.ServletUtils;
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

        CurrencyValidator.ensureCode(base);
        CurrencyValidator.ensureCode(target);

        ensureCurrencyCodes(base, target);

        mapper.writeValue(response.getOutputStream(), exchangeRateService.getExchangeRate(base, target));
    }

    @Override
    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtils.ensureContentType(request.getContentType(), ServletsConfig.X_WWW_FORM_URLENCODED_CONTENT_TYPE.getSetting());

        response.setContentType(ServletsConfig.JSON_CONTENT_TYPE.getSetting());
        response.setCharacterEncoding(ServletsConfig.ENCODING.getSetting());

        String endpoint = request.getPathInfo().replace("/", "");
        ensureEndPoint(endpoint);

        String base = endpoint.substring(START_INDEX_FIRST_CODE, END_INDEX_FIRST_CODE);
        String target = endpoint.substring(START_INDEX_SECOND_CODE, END_INDEX_SECOND_CODE);

        CurrencyValidator.ensureCode(base);
        CurrencyValidator.ensureCode(target);

        String rate = ServletUtils.getParamOrThrow(request, ExchangeRatesServlet.RATE_PARAM).trim();

        ExchangeRateValidator.ensureRate(rate);

        mapper.writeValue(response.getOutputStream(), exchangeRateService.updateExchangeRate(base, target, rate));
    }

    private void ensureEndPoint(String endpoint) {
        if (endpoint.length() != ENDPOINT_LENGTH) {
            throw new IncorrectEndpointException("Некорректный формат ключей валют");
        }
    }

    private void ensureCurrencyCodes(String base, String target) {
        CurrencyValidator.ensureCode(base);
        CurrencyValidator.ensureCode(target);
    }
}
