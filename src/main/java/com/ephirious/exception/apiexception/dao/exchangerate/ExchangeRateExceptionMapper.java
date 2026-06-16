package com.ephirious.exception.apiexception.dao.exchangerate;

import com.ephirious.exception.apiexception.dao.DaoException;
import com.ephirious.exception.environment.DatabaseMismatchException;
import com.ephirious.interfaces.ExceptionMapper;
import org.postgresql.util.PSQLException;
import org.postgresql.util.ServerErrorMessage;

import java.sql.SQLException;

public class ExchangeRateExceptionMapper implements ExceptionMapper<SQLException, DaoException> {
    private static final String UNIQUE_VIOLATION = "23505";
    private static final String NOT_NULL_VIOLATION = "23502";
    private static final String CHECK_VIOLATION = "23514";
    private static final String FOREIGN_KEY_VIOLATION = "23503";

    private static final String RATE_SCALE_CHECK_CONSTRAINT_NAME = "check_rate_scale";
    private static final String UNIQUE_RATE_PAIR_CONSTRAINT_NAME = "unique_currency_pair";
    private static final String NOT_SAME_CURRENCY_RATE_CONSTRAINT_NAME = "check_different_currencies";

    private static final String UNEXPECTED_EXCEPTION_MESSAGE = "Непредвиденная ошибка, связанная с БД";

    @Override
    public DaoException map(SQLException from) {
        if (from instanceof PSQLException psqlException) {
            ServerErrorMessage error = psqlException.getServerErrorMessage();

            if (error != null) {
                String code = psqlException.getSQLState();
                String constraintName = error.getConstraint();

                return switch (code) {
                    case CHECK_VIOLATION, UNIQUE_VIOLATION -> mapExceptionByConstraint(constraintName);
                    case NOT_NULL_VIOLATION -> new NullFieldExchangeRateException("Данный об обменном курсе должны быть заполнены полностью");
                    case FOREIGN_KEY_VIOLATION -> new UnexpectedCurrencyIDException("Данной валюты не существует в списке валют");
                    default -> new DaoException(UNEXPECTED_EXCEPTION_MESSAGE);
                };
            }

        }
        throw new DatabaseMismatchException("Не совпадают версии БД");
    }

    private DaoException mapExceptionByConstraint(String constraint) {
        return switch (constraint) {
            case RATE_SCALE_CHECK_CONSTRAINT_NAME -> new IncorrectRateScaleException("Нарушение количества знаков после запятой для курса обмена валюты");
            case UNIQUE_RATE_PAIR_CONSTRAINT_NAME -> new UniqueRateException("Обменный курс с данными валютами уже сущестует");
            case NOT_SAME_CURRENCY_RATE_CONSTRAINT_NAME -> new SameCurrencyException("Существование обменного курса, обменивающий валюту саму к себе недопустимо");
            default -> new DaoException(UNEXPECTED_EXCEPTION_MESSAGE);
        };
    }
}
