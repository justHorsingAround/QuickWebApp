package Doggo.service;

import Doggo.dao.IGetDao;

import java.sql.SQLException;
import java.util.List;

public class GetService<S extends IGetDao, E> {
    private S dao;

    public GetService(S dao) {
        this.dao = dao;
    }

    public List<E> getAllData(String sql) throws SQLException {
        return dao.fetchEverything(sql);
    }
}
