package Doggo.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IGetDao<T> {
    List<Map<String, String>> fetchEverything(String sql) throws SQLException;
}
