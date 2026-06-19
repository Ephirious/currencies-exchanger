package com.ephirious.listener;

import com.ephirious.container.ApplicationContainer;
import com.ephirious.dao.CurrencyDao;
import com.ephirious.dao.ExchangeRateDao;
import com.ephirious.db.ConnectionPool;
import com.ephirious.exception.apiexception.dao.DaoException;
import com.ephirious.exception.apiexception.dao.exchangerate.ExchangeRateExceptionMapper;
import com.ephirious.interfaces.ExceptionMapper;
import com.ephirious.services.CurrencyService;
import com.ephirious.services.ExchangeRateService;
import com.ephirious.exception.apiexception.dao.currency.CurrencyExceptionMapper;
import com.ephirious.services.ExchangeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.SQLException;

@WebListener
public class ApplicationContext implements ServletContextListener {
    public static final String APPLICATION_ATTRIBUTE = "applicationContext";

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ApplicationContainer container = new ApplicationContainer();

        ConnectionPool pool = new ConnectionPool();
        ExceptionMapper<SQLException, DaoException> exCurrencyMapper = new CurrencyExceptionMapper();
        ExceptionMapper<SQLException, DaoException> exRateMapper = new ExchangeRateExceptionMapper();

        CurrencyDao currencyDao = new CurrencyDao(pool.getDataSource(), exCurrencyMapper);
        ExchangeRateDao exchangeRateDao = new ExchangeRateDao(pool.getDataSource(), exRateMapper);

        ObjectMapper mapper = new ObjectMapper();

        CurrencyService currencyService = new CurrencyService(currencyDao);
        ExchangeRateService exchangeRateService = new ExchangeRateService(exchangeRateDao);
        ExchangeService exchangeService = new ExchangeService(exchangeRateService);

        container.put(ConnectionPool.class, pool);
        container.put(CurrencyDao.class, currencyDao);
        container.put(ObjectMapper.class, mapper);
        container.put(CurrencyService.class, currencyService);
        container.put(ExchangeRateService.class, exchangeRateService);
        container.put(ExchangeService.class, exchangeService);

        event.getServletContext().setAttribute(APPLICATION_ATTRIBUTE, container);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        ApplicationContainer container = (ApplicationContainer) event.getServletContext().getAttribute(APPLICATION_ATTRIBUTE);
        container.get(ConnectionPool.class).close();
    }
}
