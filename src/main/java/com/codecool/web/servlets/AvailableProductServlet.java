package com.codecool.web.servlets;

import Doggo.api.Doggo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/protected/asap")
public class AvailableProductServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(Doggo doggo = new Doggo(req, resp, "northwind")){
            String sql = "SELECT * FROM products;";
            doggo.executeSelect(sql);


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
