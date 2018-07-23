package Doggo.api;

import Doggo.api.exceptions.FailToCloseConnectionException;
import Doggo.dto.MessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.security.sasl.SaslException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractDoggo extends HttpServlet implements AutoCloseable{
    private final ObjectMapper objectMapper = new ObjectMapper();
    final String databaseName;
    final Connection connection;
    final HttpServletRequest req;
    final HttpServletResponse resp;

    public AbstractDoggo(HttpServletRequest req, HttpServletResponse resp, String databaseName) throws SQLException, IOException {
            this.databaseName = databaseName;
            this.resp = resp;
            this.req = req;
            try {
                this.connection = setUpConnection(req.getServletContext(), databaseName);
            } catch (SQLException s){
                sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, s.getMessage());
                s.printStackTrace();
                throw new SQLException(s.getMessage());
            }

    }


    Connection setUpConnection(ServletContext servletContext, String databaseName) throws SQLException {
        DataSource dataSource = (DataSource) servletContext.getAttribute(databaseName);
        return dataSource.getConnection();
    }

    @Override
    public void close() throws IOException {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to close connection");
            }
        }
    }

    void sendMessage(HttpServletResponse resp, int status, Object object) throws IOException {
        resp.setStatus(status);
        try {
            objectMapper.writeValue(resp.getOutputStream(), object);

        } catch(IOException e) {
            e.printStackTrace();
            sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            throw new IOException(e);
        }
    }

    public void sendTextMessage(HttpServletResponse resp, int status, String message) throws IOException {
        sendMessage(resp, status, new MessageDto(message));
    }
}
