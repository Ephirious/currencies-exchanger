package com.ephirious.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface SQLRowMapper<T> {
    T map(ResultSet value) throws SQLException;
}
