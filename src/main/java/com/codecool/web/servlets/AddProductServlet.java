package com.codecool.web.servlets;

import Doggo.api.Doggo;
import Doggo.service.Method;
import com.codecool.web.dao.MetaDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/protected/addproduct")
public class AddProductServlet extends AbstractServlet {
    private final String tableName = "Products";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(Doggo doggo = new Doggo(req, resp, "northwind")){
            String sql = "SELECT * FROM " + tableName;

            doggo.autoSend(false);

            MetaDao metaDao = new MetaDao(doggo.getConnection());
            try {
                Map<String, Integer> tableSample = metaDao.getMetaData(sql);

                doggo.send(tableSample);
            }
            catch (SQLException a){
                a.printStackTrace();
                doggo.sendInternalServerError(a.getMessage());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Doggo doggo = new Doggo(req, resp, "northwind")) {
          
            String s = prepareSqlString(req, tableName);
            List<String> sql = new ArrayList<>();
            sql.add(s);
            doggo.execute(sql, Method.INSERT);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
