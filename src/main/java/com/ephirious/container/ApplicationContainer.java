package com.ephirious.container;

import com.ephirious.db.ConnectionPool;

import java.sql.DriverManager;

public class ApplicationContainer {
    private ConnectionPool pool;

    public void createConnectionPool() {
        pool = new ConnectionPool();
    }

    public ConnectionPool getPool() {
        String EXCEPTION_MESSAGE = "Невозможно вернуть пул соединений - он ещё не создан";
        if (pool == null) {
            throw new NullPointerException(EXCEPTION_MESSAGE);
        }
        return pool;
    }

    public void closeConnectionPool() {
        pool.close();
    }
}
