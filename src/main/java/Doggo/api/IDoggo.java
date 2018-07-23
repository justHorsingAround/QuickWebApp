package Doggo.api;

import Doggo.service.Method;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IDoggo {
    String fetchUrl(HttpServletRequest req) throws IOException;
    Optional<String> fetchNumber(String url, String nonCapturingKeyword) throws IOException;
    Optional<Boolean> fetchBoolean(String url, String nonCapturingKeyword) throws IOException;
    Optional<String> fetchAPattern(String url, String regexPattern) throws IOException;
    Optional<Map<String, String>> loginStranger(String tableName, String loginName, String password) throws SQLException;
    void executeSelect(String sql) throws IOException;
    void execute(List<String> sqls, Method type) throws IOException;
    void execute(String sql, Method type) throws IOException;
    Connection getConnection();
    void send() throws IOException;
    <T> void send(T modifiedResult) throws IOException;
    List<Map<String, String>> getResult();
    void autoSend(boolean autoSend);
    void sendInternalServerError(String message) throws IOException;



}
