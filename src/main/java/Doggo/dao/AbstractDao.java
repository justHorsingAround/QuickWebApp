package Doggo.dao;

import javax.servlet.http.HttpServlet;
import java.sql.*;
import java.util.List;

public abstract class AbstractDao extends HttpServlet {

    final Connection connection;

    public AbstractDao(Connection connection) {
        this.connection = connection;
    }


    void modify(PreparedStatement preparedStatement) throws SQLException {
        int insertCount = preparedStatement.executeUpdate();
        if (insertCount != 1) {
            connection.rollback();
            throw new SQLException("Failed to insert 1 row");
        }
    }
}
