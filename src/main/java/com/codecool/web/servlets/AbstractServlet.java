package com.codecool.web.servlets;

import Doggo.dto.MessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.math.NumberUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;


public abstract class AbstractServlet extends HttpServlet {


    static boolean isBoolean(String s){
        if(s.equals("true") || s.equals("false")){
            return true;
        }
        return false;
    }


    static String constructInsert(StringBuilder columns, StringBuilder values, String tableName){
        String sql = "INSERT INTO " + tableName + "(" +
                columns.substring(0, columns.length() - 1) +
                ") VALUES (" +
                values.substring(0, values.length() - 1) +
                ");";

        return sql;
    }


    public String prepareSqlString(HttpServletRequest req, String tableName) {
        StringBuilder columnNames = new StringBuilder();
        StringBuilder inputValues = new StringBuilder();

        Map params = req.getParameterMap();
        Iterator i = params.keySet().iterator();
        while (i.hasNext()) {
            String key = (String) i.next();
            String value = ((String[]) params.get(key))[0];

            if (!value.equals("")) {
                columnNames.append(key + ",");
                if (NumberUtils.isNumber(value) || AbstractServlet.isBoolean(value)) {
                    inputValues.append(value);
                    inputValues.append(",");
                } else {
                    inputValues.append("'");
                    inputValues.append(value);
                    inputValues.append("'");
                    inputValues.append(",");
                }
            }
        }
        return AbstractServlet.constructInsert(columnNames, inputValues, tableName);
    }
}
