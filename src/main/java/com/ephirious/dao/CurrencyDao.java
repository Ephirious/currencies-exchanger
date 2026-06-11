package com.ephirious.dao;

import com.ephirious.entities.Currency;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDao {
    private static final int ID_INDEX = 1;
    private static final int CODE_INDEX = 2;
    private static final int FULLNAME_INDEX = 3;
    private static final int SIGN_INDEX = 4;

    private static final String FIND_ALL = """
            SELECT *
            FROM currencies
            """;

    private final DataSource dataSource;

    public CurrencyDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Currency> findAll() throws SQLException {
        List<Currency> currencies = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                currencies.add(new Currency(
                        result.getLong(ID_INDEX),
                        result.getString(CODE_INDEX),
                        result.getString(FULLNAME_INDEX),
                        result.getString(SIGN_INDEX))
                );
            }
        }

        return currencies;
    }

}
