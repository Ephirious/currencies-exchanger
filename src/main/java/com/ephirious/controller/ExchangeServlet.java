package com.ephirious.controller;

import com.ephirious.config.ServletsConfig;
import com.ephirious.container.ApplicationContainer;
import com.ephirious.exception.apiexception.servlet.InvalidParameterException;
import com.ephirious.listener.ApplicationContext;
import com.ephirious.services.ExchangeService;
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

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    private ObjectMapper mapper;
    private ExchangeService exchangeService;

    @Override
    public void init() throws ServletException {
        ApplicationContainer container = (ApplicationContainer) getServletContext()
                .getAttribute(ApplicationContext.APPLICATION_ATTRIBUTE);
        mapper = container.get(ObjectMapper.class);
        exchangeService = container.get(ExchangeService.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String codeFrom = ServletUtils.getParamOrThrow(request, "from");
        String codeTo = ServletUtils.getParamOrThrow(request, "to");
        String amount = ServletUtils.getParamOrThrow(request, "amount");

        CurrencyValidator.ensureCode(codeFrom);
        CurrencyValidator.ensureCode(codeTo);
        ensureAmount(amount);

        mapper.writeValue(response.getOutputStream(), exchangeService.exchange(codeFrom, codeTo, amount));
    }

    private void ensureAmount(String amount) {
        if (!ExchangeRateValidator.isAllDigits(amount)) {
            throw new InvalidParameterException("Количество переводимых средств должно содержать только цифры и точку в качестве разделителя");
        }
    }
}
