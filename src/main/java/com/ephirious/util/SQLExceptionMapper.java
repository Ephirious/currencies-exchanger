package com.ephirious.util;

import com.ephirious.exception.apiexception.daoexception.DaoCheckException;
import com.ephirious.exception.apiexception.daoexception.DaoException;
import com.ephirious.exception.apiexception.daoexception.DaoUniqueException;
import com.ephirious.interfaces.ExceptionMapper;

import java.sql.SQLException;

public class SQLExceptionMapper implements ExceptionMapper<SQLException, DaoException> {
    private static final String UNIQUE_VIOLATION = "23505";
    private static final String FOREIGN_VIOLATION = "23503";
    private static final String NOT_NULL_VIOLATION = "23502";
    private static final String CHECK_VIOLATION = "23514";

    private static final String UNEXPECTED_EXCEPTION_MESSAGE = "Непредвиденная ошибка, связанная с БД";

    @Override
    public DaoException map(SQLException from) {
        String sqlState = from.getSQLState();
        return switch (sqlState) {
            case UNIQUE_VIOLATION, FOREIGN_VIOLATION -> new DaoUniqueException();
            case CHECK_VIOLATION, NOT_NULL_VIOLATION -> new DaoCheckException();
            default -> new DaoException(UNEXPECTED_EXCEPTION_MESSAGE);
        };
    }
}
