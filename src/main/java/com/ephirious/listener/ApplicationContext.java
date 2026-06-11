package com.ephirious.listener;

import com.ephirious.container.ApplicationContainer;
import com.ephirious.dao.CurrencyDao;
import com.ephirious.db.ConnectionPool;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ApplicationContext implements ServletContextListener {
    public static final String APPLICATION_ATTRIBUTE = "applicationContext";

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ApplicationContainer container = new ApplicationContainer();

        ConnectionPool pool = new ConnectionPool();
        CurrencyDao currencyDao = new CurrencyDao(pool.getDataSource());

        container.put(ConnectionPool.class, pool);
        container.put(CurrencyDao.class, currencyDao);

        event.getServletContext().setAttribute(APPLICATION_ATTRIBUTE, container);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        ApplicationContainer container = (ApplicationContainer) event.getServletContext().getAttribute(APPLICATION_ATTRIBUTE);
        ConnectionPool pool = container.get(ConnectionPool.class);
        pool.close();
    }
}
