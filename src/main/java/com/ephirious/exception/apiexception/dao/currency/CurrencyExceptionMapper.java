package com.ephirious.exception.apiexception.dao.currency;

import com.ephirious.exception.apiexception.dao.DaoException;
import com.ephirious.interfaces.ExceptionMapper;

import java.sql.SQLException;

public class CurrencyExceptionMapper implements ExceptionMapper<SQLException, DaoException> {
    private static final String UNIQUE_VIOLATION = "23505";
    private static final String NOT_NULL_VIOLATION = "23502";
    private static final String CHECK_VIOLATION = "23514";

    private static final String UNEXPECTED_EXCEPTION_MESSAGE = "Непредвиденная ошибка, связанная с БД";

    @Override
    public DaoException map(SQLException from) {
        String sqlState = from.getSQLState();
        return switch (sqlState) {
            case UNIQUE_VIOLATION -> new CurrencyUniqueException("Такая валюта уже существует");
            case CHECK_VIOLATION -> new CurrencyConstraintException("Неккоректные данные для валюты. Нарушены ограничения для полей");
            case NOT_NULL_VIOLATION -> new CurrencyNullFieldException("Некорректная валюты. Остались незаполненные поля");
            default -> new DaoException(UNEXPECTED_EXCEPTION_MESSAGE);
        };
    }
}
