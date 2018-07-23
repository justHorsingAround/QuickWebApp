package com.codecool.web.dao;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class MetaDao {
    Connection connection;

    public MetaDao(Connection conenction) {
        this.connection = conenction;
    }

    public Map<String, Integer> getMetaData(String sql) throws SQLException {
        Map<String, Integer> columns = new HashMap<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                ResultSetMetaData rsmd = resultSet.getMetaData();
                int columnNumber = rsmd.getColumnCount();
                for (int i = 1; i <= columnNumber; ++i) {
                    String key = rsmd.getColumnName(i);
                    columns.put(key, rsmd.isNullable(i));
                }
            }
        }
        return columns;
    }
}
