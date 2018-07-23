package Doggo.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectDao extends AbstractDao implements IGetDao {
    public SelectDao(Connection connection) {
        super(connection);
    }


    public List<Map<String, String>> fetchEverything(String sql) throws SQLException {
        List<Map<String, String>> result = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(make(resultSet));
                }
            }
            return result;
        }
    }

    private Map<String, String> make(ResultSet resultSet) throws SQLException {
        Map<String, String> result = new HashMap<>();
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnNumber = rsmd.getColumnCount();
        for (int i = 1; i <= columnNumber; ++i) {
            String key = rsmd.getColumnName(i);
            result.put(key, resultSet.getString(key));
        }
        return result;
    }

}
