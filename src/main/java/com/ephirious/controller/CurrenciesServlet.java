package com.ephirious.controller;

import com.ephirious.config.ServletsConfig;
import com.ephirious.container.ApplicationContainer;
import com.ephirious.dao.CurrencyDao;
import com.ephirious.dto.CurrencyDTO;
import com.ephirious.listener.ApplicationContext;
import com.ephirious.services.CurrencyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private CurrencyService currencyService;
    private ObjectMapper mapper;

    @Override
    public void init() throws ServletException {
        ApplicationContainer container = (ApplicationContainer) getServletContext()
                .getAttribute(ApplicationContext.APPLICATION_ATTRIBUTE);

        currencyService = new CurrencyService(container.get(CurrencyDao.class));
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
}