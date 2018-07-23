package Doggo.dao;

import Doggo.dao.exceptions.NoChangeHappenedException;
import Doggo.dao.exceptions.PartialChangeHappenedInDatabase;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ModifyDao extends AbstractDao implements IModifyDao {

    public ModifyDao(Connection connection) {
        super(connection);
    }

    @Override
    public void mixed(List<String> sqls) throws SQLException, NoChangeHappenedException, PartialChangeHappenedInDatabase {
        connection.setAutoCommit(false);
        try {
            Statement statement = connection.createStatement();
            for (String sql : sqls) {
                statement.addBatch(sql);
            }
            int[] status = statement.executeBatch();
            connection.commit();
            exmineResultStatus(status);
        } catch (SQLException s){
            connection.rollback();
            throw new SQLException(s.getMessage());
        }

    }

    private void exmineResultStatus(int[] status) throws NoChangeHappenedException, PartialChangeHappenedInDatabase {
        int statusCounter = 1;
        for(int i = 0; i < status.length; ++i){
            if(status[i] == 0){
                statusCounter++;
            }
        }
        if (statusCounter-1 == status.length){
            throw new NoChangeHappenedException();
        } else if (statusCounter > 1){
            throw new PartialChangeHappenedInDatabase();
        }
    }
}
