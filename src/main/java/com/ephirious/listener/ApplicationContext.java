package com.ephirious.listener;

import com.ephirious.container.ApplicationContainer;
import com.ephirious.dao.CurrencyDao;
import com.ephirious.dao.ExchangeRateDao;
import com.ephirious.db.ConnectionPool;
import com.ephirious.exception.apiexception.daoexception.DaoException;
import com.ephirious.interfaces.ExceptionMapper;
import com.ephirious.services.CurrencyService;
import com.ephirious.services.ExchangeRateService;
import com.ephirious.util.SQLExceptionMapper;
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
        ExceptionMapper<SQLException, DaoException> exceptionMapper = new SQLExceptionMapper();

        CurrencyDao currencyDao = new CurrencyDao(pool.getDataSource(), exceptionMapper);
        ExchangeRateDao exchangeRateDao = new ExchangeRateDao(pool.getDataSource(), exceptionMapper);

        ObjectMapper mapper = new ObjectMapper();
        CurrencyService currencyService = new CurrencyService(currencyDao);
        ExchangeRateService exchangeRateService = new ExchangeRateService(currencyService, exchangeRateDao);

        container.put(ConnectionPool.class, pool);
        container.put(CurrencyDao.class, currencyDao);
        container.put(ObjectMapper.class, mapper);
        container.put(CurrencyService.class, currencyService);
        container.put(ExchangeRateService.class, exchangeRateService);

        event.getServletContext().setAttribute(APPLICATION_ATTRIBUTE, container);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        ApplicationContainer container = (ApplicationContainer) event.getServletContext().getAttribute(APPLICATION_ATTRIBUTE);
        container.get(ConnectionPool.class).close();
    }
}
