package com.codecool.web.servlets;

import Doggo.api.Doggo;
import com.codecool.web.services.UserService;;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@WebServlet("/login")
public class LoginServlet extends AbstractServlet{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(Doggo doggo = new Doggo(req, resp, "northwind")){
            String shipperId = req.getParameter("shipper");
            String supplierId = req.getParameter("supplier");
            UserService userService = new UserService(doggo);

            if (!shipperId.equals("")) {
                Map<String, String> shipper = userService.loginUser(shipperId, "Shippers", "shipper_id");
                if(shipper != null) {
                    req.getSession().setAttribute("user", shipper);
                    doggo.send(shipper);
                }
                else {
                    doggo.sendTextMessage(resp, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorised user");
                }
            }
            else {
                Map<String, String> supplier = userService.loginUser(supplierId, "Suppliers", "supplier_id");
                if(supplier != null) {
                    req.getSession().setAttribute("user", supplier);
                    doggo.send(supplier);
                }
                else {
                    doggo.sendTextMessage(resp, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorised user");
                }
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
