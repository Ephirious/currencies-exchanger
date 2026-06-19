package com.ephirious.controller;

import com.ephirious.config.HttpStatusCode;
import com.ephirious.config.ServletsConfig;
import com.ephirious.container.ApplicationContainer;
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

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    public static final String BASE_CODE_PARAM = "baseCurrencyCode";
    public static final String TARGET_CODE_PARAM = "targetCurrencyCode";
    public static final String RATE_PARAM = "rate";

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
        mapper.writeValue(response.getOutputStream(), exchangeRateService.getExchangeRates());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtils.ensureContentType(request.getContentType(), ServletsConfig.X_WWW_FORM_URLENCODED_CONTENT_TYPE.getSetting());

        String base = ServletUtils.getParamOrThrow(request, BASE_CODE_PARAM).trim();
        String target = ServletUtils.getParamOrThrow(request, TARGET_CODE_PARAM).trim();
        String rate = ServletUtils.getParamOrThrow(request, RATE_PARAM).trim();

        CurrencyValidator.ensureCode(base);
        CurrencyValidator.ensureCode(target);
        ExchangeRateValidator.ensureRate(rate);

        response.setStatus(HttpStatusCode.CREATED.getStatusCode());
        mapper.writeValue(response.getOutputStream(), exchangeRateService.addExchangeRate(base, target, rate));
    }
}
