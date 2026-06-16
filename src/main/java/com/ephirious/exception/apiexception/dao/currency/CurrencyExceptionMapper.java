package com.ephirious.exception.apiexception.dao.currency;

import com.ephirious.exception.apiexception.dao.DaoException;
import com.ephirious.exception.apiexception.dao.IncorrectFormatException;
import com.ephirious.exception.apiexception.dao.NullFieldException;
import com.ephirious.exception.apiexception.dao.UniqueException;
import com.ephirious.exception.apiexception.dao.exchangerate.*;
import com.ephirious.exception.environment.DatabaseMismatchException;
import com.ephirious.interfaces.ExceptionMapper;
import org.postgresql.util.PSQLException;
import org.postgresql.util.ServerErrorMessage;

import java.sql.SQLException;

public class CurrencyExceptionMapper implements ExceptionMapper<SQLException, DaoException> {
    private static final String UNIQUE_VIOLATION = "23505";
    private static final String NOT_NULL_VIOLATION = "23502";
    private static final String CHECK_VIOLATION = "23514";
    private static final String STRING_VIOLATION = "22001";

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
                    case NOT_NULL_VIOLATION -> mapExceptionByNullColumn(error.getColumn());
                    case STRING_VIOLATION -> new IncorrectFormatException("Введённые строки превышают ограничение по длине");
                    default -> new DaoException(UNEXPECTED_EXCEPTION_MESSAGE);
                };
            }

        }
        throw new DatabaseMismatchException("Не совпадают версии БД");
    }

    private DaoException mapExceptionByConstraint(String constraint) {
        if (constraint == null) {
            return new DaoException("Непредвиденное исключение при обработке ошибки CONSTRAINT таблицы валют");
        }
        return switch (constraint) {
            case "pk_currencies" -> new UniqueException("Валюта с таким ID уже существует");
            case "unique_code_check" -> new UniqueException("Валюта с таким кодом уже существует");
            case "upper_code_check" -> new IncorrectFormatException("Код валюты должен быть записан заглавными буквами");
            default -> new DaoException(UNEXPECTED_EXCEPTION_MESSAGE);
        };
    }

    private DaoException mapExceptionByNullColumn(String column) {
        if (column == null) {
            return new DaoException("Непредвиденное исключение при обработке ошибки NOT NULL таблицы валют");
        }
        return switch (column) {
            case "id" -> new NullFieldException("Валюта должна иметь идентификатор");
            case "code" -> new NullFieldException("Валюта должна иметь код");
            case "fullname" -> new NullFieldException("Валюта должна иметь название");
            case "sign" -> new NullFieldException("Валюта должна иметь знак");
            default -> new DaoException(UNEXPECTED_EXCEPTION_MESSAGE);
        };
    }
}
