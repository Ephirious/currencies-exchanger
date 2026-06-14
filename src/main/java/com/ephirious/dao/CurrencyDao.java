package com.ephirious.dao;

import com.ephirious.entities.Currency;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CurrencyDao extends BaseDAO {
    private static final int ID_INDEX = 1;
    private static final int CODE_INDEX = 2;
    private static final int FULLNAME_INDEX = 3;
    private static final int SIGN_INDEX = 4;

    private static final String FIND_ALL = """
            SELECT id, code, fullname, sign
            FROM currencies;
            """;

    private static final String FIND_BY_CODE = """
            SELECT id, code, fullname, sign
            FROM currencies
            WHERE code = ?;
            """;

    private static final String INSERT_CURRENCY = """
            INSERT INTO currencies (code, fullname, sign)
            VALUES (?, ?, ?)
            RETURNING *;
            """;

    public CurrencyDao(DataSource dataSource) {
        super(dataSource);
    }

    public List<Currency> findAll() {
        return queryList(
                FIND_ALL,
                this::mapCurrency,
                "Ошибка при получении списка валют"
        );
    }

    public Optional<Currency> findByCode(String code) {
        return queryOptional(
                FIND_BY_CODE,
                (statement) -> {
                    statement.setString(1, code);
                },
                this::mapCurrency,
                "Ошибка при получении валюты по коду"
        );
    }

    public Optional<Currency> insert(Currency entity) {
        return queryOptional(
                INSERT_CURRENCY,
                (statement) -> {
                    int indexSql = 1;
                    statement.setString(indexSql++, entity.getCode());
                    statement.setString(indexSql++, entity.getName());
                    statement.setString(indexSql++, entity.getSign());
                },
                this::mapCurrency,
                "Ошибка при добавлении валюты"
        );
    }

    private Currency mapCurrency(ResultSet result) throws SQLException {
        return new Currency(
                result.getLong(ID_INDEX),
                result.getString(CODE_INDEX),
                result.getString(FULLNAME_INDEX),
                result.getString(SIGN_INDEX)
        );
    }
}
