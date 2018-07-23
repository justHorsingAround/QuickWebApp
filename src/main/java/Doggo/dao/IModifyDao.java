package Doggo.dao;

import Doggo.dao.exceptions.NoChangeHappenedException;
import Doggo.dao.exceptions.PartialChangeHappenedInDatabase;

import java.sql.SQLException;
import java.util.List;

public interface IModifyDao {
    void mixed(List<String> sqls) throws SQLException, NoChangeHappenedException, PartialChangeHappenedInDatabase;

}
