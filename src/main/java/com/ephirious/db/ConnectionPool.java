package com.ephirious.db;

import com.ephirious.exception.EnvironmentVariableNotFound;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.NonNull;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class ConnectionPool {
    private static final String DB_PROTOCOL_ENVIRONMENT_DESCRIPTOR = "DATABASE_PROTOCOL";
    private static final String DB_SERVER_ENVIRONMENT_DESCRIPTOR = "DATABASE_SERVER";
    private static final String DB_PORT_ENVIRONMENT_DESCRIPTOR = "DATABASE_PORT";
    private static final String DB_NAME_ENVIRONMENT_DESCRIPTOR = "DATABASE_NAME";
    private static final String DB_USER_ENVIRONMENT_DESCRIPTOR = "DATABASE_USER";
    private static final String DB_PASSWORD_ENVIRONMENT_DESCRIPTOR = "DATABASE_PASSWORD";
    private static final String DB_DRIVER_CLASS_ENVIRONMENT_DESCRIPTOR = "DATABASE_CLASS_DRIVER";
    private static final String DB_POOL_SIZE_ENVIRONMENT_DESCRIPTOR = "DATABASE_POOL_SIZE";

    private final HikariDataSource pool;

    public ConnectionPool() {
        HikariConfig config = createPoolConfig();
        pool = new HikariDataSource(config);
    }

    public void close() {
        if (pool != null && !pool.isClosed()) {
            pool.close();
        }
    }

    public DataSource getDataSource() {
        return pool;
    }

    private String constructURL(@NonNull String protocol, @NonNull String server, @NonNull String port, @NonNull String databaseName) {
        String format = "%s://%s:%s/%s";
        return format.formatted(protocol, server, port, databaseName);
    }

    private String getEnvironmentVariableOrThrow(@NonNull String variableName) {
        return Optional.ofNullable(System.getenv(variableName))
                .orElseThrow(() -> new EnvironmentVariableNotFound(variableName));
    }

    private HikariConfig createPoolConfig() {
        String protocol = getEnvironmentVariableOrThrow(DB_PROTOCOL_ENVIRONMENT_DESCRIPTOR);
        String server = getEnvironmentVariableOrThrow(DB_SERVER_ENVIRONMENT_DESCRIPTOR);
        String port = getEnvironmentVariableOrThrow(DB_PORT_ENVIRONMENT_DESCRIPTOR);
        String dbName = getEnvironmentVariableOrThrow(DB_NAME_ENVIRONMENT_DESCRIPTOR);
        String user = getEnvironmentVariableOrThrow(DB_USER_ENVIRONMENT_DESCRIPTOR);
        String password = getEnvironmentVariableOrThrow(DB_PASSWORD_ENVIRONMENT_DESCRIPTOR);
        String driverClass = getEnvironmentVariableOrThrow(DB_DRIVER_CLASS_ENVIRONMENT_DESCRIPTOR);
        int poolSize = Integer.parseInt(getEnvironmentVariableOrThrow(DB_POOL_SIZE_ENVIRONMENT_DESCRIPTOR));

        String url = constructURL(protocol, server, port, dbName);

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setMaximumPoolSize(poolSize);
        config.setDriverClassName(driverClass);

        return config;
    }
}
