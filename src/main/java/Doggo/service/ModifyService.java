package Doggo.service;

import Doggo.dao.IModifyDao;
import Doggo.dao.exceptions.NoChangeHappenedException;
import Doggo.dao.exceptions.PartialChangeHappenedInDatabase;

import java.sql.SQLException;
import java.util.List;

public class ModifyService<T extends IModifyDao> {
    private T dao;

    public ModifyService(T dao) {
        this.dao = dao;
    }

    public void deleteData(List<String> sqls) throws SQLException, NoChangeHappenedException, PartialChangeHappenedInDatabase {
        dao.mixed(sqls);
    }

    public void insertNewData(List<String> sqls) throws SQLException, NoChangeHappenedException, PartialChangeHappenedInDatabase {
        dao.mixed(sqls);
    }

    public void updateData(List<String> sqls) throws SQLException, NoChangeHappenedException, PartialChangeHappenedInDatabase {
        dao.mixed(sqls);
    }

    public void mixedTransaction(List<String> sqls) throws SQLException, NoChangeHappenedException, PartialChangeHappenedInDatabase {
        dao.mixed(sqls);
    }
}
