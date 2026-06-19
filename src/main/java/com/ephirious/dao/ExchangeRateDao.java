package com.ephirious.dao;

import com.ephirious.entities.Currency;
import com.ephirious.entities.ExchangeRate;
import com.ephirious.exception.apiexception.dao.DaoException;
import com.ephirious.interfaces.ExceptionMapper;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDao extends BaseDAO {
    private static final String BASE_SQL = """
            SELECT
                e.id AS exID,
                e.rate,
                c1.id AS base_id,
                c1.code AS base_code,
                c1.fullname AS base_name,
                c1.sign AS base_sign,
                c2.id AS target_id,
                c2.code AS target_code,
                c2.fullname AS target_name,
                c2.sign AS target_sign
            FROM %s e
            INNER JOIN currencies c1 ON c1.id = e.base_currency_id
            INNER JOIN currencies c2 ON c2.id = e.target_currency_id
            """;

    private static final String FIND_ALL = BASE_SQL.formatted("exchange_rates") + ";";

    private static final String FIND_BY_CODE_ID = BASE_SQL.formatted("exchange_rates") + """
            WHERE c1.code = ? AND c2.code = ?;
            """;


    private static final String INSERT_RATE = """
            WITH from_insert AS  (
                INSERT INTO exchange_rates(base_currency_id, target_currency_id, rate)
                SELECT
                    COALESCE((SELECT id FROM currencies WHERE code = ?), -1) AS base_currency_id,
                    COALESCE((SELECT id FROM currencies WHERE code = ?), -2) AS target_currency_id,
                    ? AS rate
                RETURNING *
            )
            """ + BASE_SQL.formatted("from_insert") + ";";

    private static final String UPDATE_RATE = """
            WITH from_update AS (
                UPDATE exchange_rates e
                SET rate = ?
                WHERE
                    (SELECT id FROM currencies WHERE code = ?) = e.base_currency_id
                    AND
                    (SELECT id FROM currencies WHERE code = ?) = e.target_currency_id
                RETURNING *
            )
            """ + BASE_SQL.formatted("from_update") + ";";

    public ExchangeRateDao(DataSource source, ExceptionMapper<SQLException, DaoException> mapper) {
        super(source, mapper);
    }

    public List<ExchangeRate> findAll() {
        return queryList(
                FIND_ALL,
                this::mapExchangeRate
        );
    }

    public Optional<ExchangeRate> findByCodeID(String baseCode, String targetCode) {
        return queryOptional(
                FIND_BY_CODE_ID,
                (statement) -> {
                    statement.setString(1, baseCode);
                    statement.setString(2, targetCode);
                },
                this::mapExchangeRate
        );
    }

    public Optional<ExchangeRate> insert(String baseId, String targetId, BigDecimal rate) {
        return queryOptional(
                INSERT_RATE,
                (statement) -> {
                    statement.setString(1, baseId);
                    statement.setString(2, targetId);
                    statement.setBigDecimal(3, rate);
                },
                this::mapExchangeRate
        );
    }

    public Optional<ExchangeRate> update(String baseId, String targetId, BigDecimal newRate) {
        return queryOptional(
                UPDATE_RATE,
                (statement) -> {
                    statement.setBigDecimal(1, newRate);
                    statement.setString(2, baseId);
                    statement.setString(3, targetId);
                },
                this::mapExchangeRate
        );
    }


    private ExchangeRate mapExchangeRate(ResultSet result) throws SQLException {
        Currency base = new Currency(
                result.getLong("base_id"),
                result.getString("base_code"),
                result.getString("base_name"),
                result.getString("base_sign")
        );

        Currency target = new Currency(
                result.getLong("target_id"),
                result.getString("target_code"),
                result.getString("target_name"),
                result.getString("target_sign")
        );

        return new ExchangeRate(
                result.getLong("exID"),
                base,
                target,
                result.getBigDecimal("rate")
        );
    }
}
