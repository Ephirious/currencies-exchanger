package com.ephirious.dao;

import com.ephirious.entities.Currency;
import com.ephirious.entities.ExchangeRate;
import com.ephirious.exception.apiexception.daoexception.DaoException;
import com.ephirious.interfaces.ExceptionMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDao extends BaseDAO {
    private static final int ID_INDEX = 1;
    private static final int BASE_CURRENCY_INDEX = 2;
    private static final int TARGET_CURRENCY_INDEX = 3;
    private static final int RATE_INDEX = 4;

    private static final String FIND_ALL = """
            SELECT id, base_currency_id, target_currency_id, rate
            FROM exchange_rates;
            """;

    private static final String FIND_BY_CODE_IDS = """
            SELECT id, base_currency_id, target_currency_id, rate
            FROM exchange_rates
            WHERE base_currency_id = ? AND target_currency_id = ?;
            """;

    public ExchangeRateDao(DataSource source, ExceptionMapper<SQLException, DaoException> mapper) {
        super(source, mapper);
    }

    public List<ExchangeRate> findAll() {
        return queryList(
                FIND_ALL,
                this::mapExchangeRate
        );
    }

    public Optional<ExchangeRate> findByCodeIDs(Long base, Long target) {
        return queryOptional(
                FIND_BY_CODE_IDS,
                (statement) -> {
                    int index = 1;
                    statement.setLong(index++, base);
                    statement.setLong(index++, target);
                },
                this::mapExchangeRate
        );
    }

    private ExchangeRate mapExchangeRate(ResultSet result) throws SQLException {
        Currency base = new Currency();
        Currency target = new Currency();
        base.setId(result.getLong(BASE_CURRENCY_INDEX));
        target.setId(result.getLong(TARGET_CURRENCY_INDEX));
        return new ExchangeRate(
                result.getLong(ID_INDEX),
                base,
                target,
                result.getBigDecimal(RATE_INDEX)
        );
    }
}
