package com.ephirious.exception.apiexception.dao.exchangerate;

import com.ephirious.exception.apiexception.dao.*;
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
    private static final String STRING_VIOLATION = "22001";


    private static final String UNEXPECTED_EXCEPTION_MESSAGE = "Непредвиденная ошибка, связанная с БД";

    @Override
    public DaoException map(SQLException from) {
        if (from instanceof PSQLException psqlException) {
            ServerErrorMessage error = psqlException.getServerErrorMessage();
            String code = from.getSQLState();

            if (STRING_VIOLATION.equals(code)) {
                return new IncorrectFormatException("Введённые строки превышают ограничение по длине");
            }


            if (error != null) {
                String constraintName = error.getConstraint();

                return switch (code) {
                    case CHECK_VIOLATION, UNIQUE_VIOLATION, FOREIGN_KEY_VIOLATION -> mapExceptionByConstraint(constraintName);
                    case NOT_NULL_VIOLATION -> mapExceptionByNullColumn(error.getColumn());
                    default -> new DaoException(UNEXPECTED_EXCEPTION_MESSAGE);
                };
            }

        }
        throw new DatabaseMismatchException("Не совпадают версии БД");
    }

    private DaoException mapExceptionByConstraint(String constraint) {
        if (constraint == null) {
            return new DaoException("Непредвиденное исключение при обработке ошибки CONSTRAINT таблицы обменных курсов");
        }
        return switch (constraint) {
            case "pk_exchange_rates" -> new UniqueException("Обменный курс с таким ID уже есть");
            case "check_rate_scale" -> new IncorrectFormatException("Нарушение количества знаков после запятой для курса обмена валюты");
            case "unique_currency_pair" -> new UniqueException("Обменный курс с данными валютами уже существует");
            case "check_different_currencies" -> new SameCurrencyException("Существование обменного курса, обменивающий валюту саму к себе недопустимо");
            case "fk_base_currency_id" -> new ForeignKeyException("Базовая валюты не существует");
            case "fk_target_currency_id" -> new ForeignKeyException("Целевая валюта не существует");
            default -> new DaoException(UNEXPECTED_EXCEPTION_MESSAGE);
        };
    }

    private DaoException mapExceptionByNullColumn(String column) {
        if (column == null) {
            return new DaoException("Непредвиденное исключение при обработке ошибки NOT NULL таблицы обменных курсов");
        }
        return switch (column) {
            case "id" -> new NullFieldException("Обменный курс должен иметь идентификатор");
            case "base_currency_id" -> new NullFieldException("Обменный курс должен ссылаться на базовую валюту");
            case "target_currency_id" -> new NullFieldException("Обменный курс должен ссылаться на целевую валюту");
            case "rate" -> new NullFieldException("Обменный курс должен иметь курс обмена");
            default -> new DaoException(UNEXPECTED_EXCEPTION_MESSAGE);
        };
    }
}
