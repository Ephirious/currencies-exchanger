package com.ephirious.controller;

import com.ephirious.config.ServletsConfig;
import com.ephirious.container.ApplicationContainer;
import com.ephirious.dto.CurrencyDTO;
import com.ephirious.exception.apiexception.servlet.UnexpectedContentTypeException;
import com.ephirious.exception.apiexception.servlet.ParameterNullException;
import com.ephirious.listener.ApplicationContext;
import com.ephirious.services.CurrencyService;
import com.ephirious.util.CurrencyValidator;
import com.ephirious.util.ServletUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private static final String CODE_PARAM = "code";
    private static final String NAME_PARAM = "name";
    private static final String SIGN_PARAM = "sign";


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

        List<CurrencyDTO> currencies = currencyService.getCurrencies();

        try (ServletOutputStream outputStream = response.getOutputStream()) {
            mapper.writeValue(outputStream, currencies);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contentType = request.getContentType();
        ServletUtils.ensureContentType(contentType, ServletsConfig.X_WWW_FORM_URLENCODED_CONTENT_TYPE.getSetting());

        response.setContentType(ServletsConfig.JSON_CONTENT_TYPE.getSetting());
        response.setCharacterEncoding(ServletsConfig.ENCODING.getSetting());

        String code = ServletUtils.getParamOrThrow(request, CODE_PARAM).trim();
        String name = ServletUtils.getParamOrThrow(request, NAME_PARAM).trim();
        String sign = ServletUtils.getParamOrThrow(request, SIGN_PARAM).trim();

        CurrencyValidator.ensureCode(code);
        CurrencyValidator.ensureCurrencyName(name);
        CurrencyValidator.ensureSign(sign);

        CurrencyDTO added = currencyService.addCurrency(code, name, sign);
        int statusCodeAdded = 201;
        response.setStatus(statusCodeAdded);
        mapper.writeValue(response.getOutputStream(), added);
    }
}