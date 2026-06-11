package com.ephirious.controller;

import com.ephirious.container.ApplicationContainer;
import com.ephirious.dao.CurrencyDao;
import com.ephirious.dto.CurrencyDTO;
import com.ephirious.listener.ApplicationContext;
import com.ephirious.services.CurrencyService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private CurrencyService currencyService;

    @Override
    public void init() throws ServletException {
        ApplicationContainer container = (ApplicationContainer) getServletContext()
                .getAttribute(ApplicationContext.APPLICATION_ATTRIBUTE);
        currencyService = new CurrencyService(container.get(CurrencyDao.class));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String CONTENT_TYPE = "text/plain; charset=UTF-8";
        response.setContentType(CONTENT_TYPE);
        PrintWriter responseWriter = response.getWriter();
        for (CurrencyDTO currency : currencyService.getCurrencies()) {
            responseWriter.printf("<p>%s %s %s %s</p>",
                    currency.getId(),
                    currency.getCode(),
                    currency.getFullname(),
                    currency.getCode()
            );
        }
    }
}
