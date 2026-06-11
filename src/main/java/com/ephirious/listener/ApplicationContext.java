package com.ephirious.listener;

import com.ephirious.container.ApplicationContainer;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ApplicationContext implements ServletContextListener {
    public static final String APPLICATION_ATTRIBUTE = "applicationContext";

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ApplicationContainer container = new ApplicationContainer();
        container.createConnectionPool();
        event.getServletContext().setAttribute(APPLICATION_ATTRIBUTE, container);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        ApplicationContainer container = (ApplicationContainer) event.getServletContext().getAttribute(APPLICATION_ATTRIBUTE);
        container.closeConnectionPool();
    }
}
