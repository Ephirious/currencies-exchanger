package com.ephirious.dao;

import com.ephirious.exception.apiexception.DaoException;
import com.ephirious.interfaces.SQLConsumer;
import com.ephirious.interfaces.SQLRowMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class BaseDAO {
    private static final SQLConsumer<PreparedStatement> EMPTY_STATEMENT_SETTER = statement -> {};

    protected final DataSource source;

    public BaseDAO(DataSource dataSource) {
        source = dataSource;
    }

    protected <T> List<T> queryList(String sql, SQLConsumer<PreparedStatement> statementSetter, SQLRowMapper<T> rowMapper, String exceptionMessage) {
        List<T> objects = new ArrayList<>();

        try (Connection connection = source.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statementSetter.accept(statement);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    objects.add(rowMapper.map(result));
                }
            }

        } catch (SQLException exception) {
            throw new DaoException(exceptionMessage);
        }

        return objects;
    }

    protected <T> List<T> queryList(String sql, SQLRowMapper<T> rowMapper, String exceptionMessage) {
        return queryList(sql, EMPTY_STATEMENT_SETTER, rowMapper, exceptionMessage);
    }

    protected <T> Optional<T> queryOptional(String sql, SQLConsumer<PreparedStatement> statementSetter, SQLRowMapper<T> rowMapper, String exceptionMessage) {
        try (Connection connection = source.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statementSetter.accept(statement);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return Optional.of(rowMapper.map(result));
                }
            }

        } catch (SQLException exception) {
            throw new DaoException(exceptionMessage);
        }

        return Optional.empty();
    }

    protected <T> Optional<T> queryOptional(String sql, SQLRowMapper<T> rowMapper, String exceptionMessage) {
        return queryOptional(sql, EMPTY_STATEMENT_SETTER, rowMapper, exceptionMessage);
    }

    protected int queryUpdate(String sql, SQLConsumer<PreparedStatement> statementSetter, String exceptionMessage) {
        try (Connection connection = source.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statementSetter.accept(statement);
            return statement.executeUpdate();
        } catch (SQLException exception) {
            throw new DaoException(exceptionMessage);
        }
    }


}