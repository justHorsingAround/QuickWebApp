package Doggo.api;

import Doggo.api.exceptions.InvalidSqlQueryException;
import Doggo.api.exceptions.ResultListIsNotEmptyException;
import Doggo.dao.ModifyDao;
import Doggo.dao.SelectDao;
import Doggo.dao.exceptions.NoChangeHappenedException;
import Doggo.dao.exceptions.PartialChangeHappenedInDatabase;
import Doggo.service.GetService;
import Doggo.service.Method;
import Doggo.service.ModifyService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Doggo extends AbstractDoggo implements IDoggo {
    private GetService getService;
    private ModifyService modifyService;
    private List<Map<String, String>> result;
    private boolean autoSend = true;

    public Doggo(HttpServletRequest req, HttpServletResponse resp, String databaseName) throws IOException, SQLException {
        super(req, resp, databaseName);
        modifyService = new ModifyService(new ModifyDao(connection));
        getService = new GetService(new SelectDao(connection));
    }

    @Override
    public void autoSend(boolean autoSend){
        this.autoSend = autoSend;
    }

    @Override
    public void sendInternalServerError(String message) throws IOException {
        sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);
    }

    @Override
    public void execute(String sql, Method type) throws IOException{
        List<String> sqls = new ArrayList<>();
        sqls.add(sql);
        execute(sqls, type);
    }

    @Override
    public void execute(List<String> sqls, Method type) throws IOException {
        try {
            pickMethod(type, sqls);
            if(autoSend) {
                sendTextMessage(resp, HttpServletResponse.SC_OK, "Transaction successful");
            }
        } catch (InvalidSqlQueryException i) {
            sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, i.getMessage());
            i.getMessage();
            i.printStackTrace();
        } catch (SQLException s) {
            sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, s.getMessage());
            s.getMessage();
            s.printStackTrace();
        } catch (NoChangeHappenedException e) {
            sendTextMessage(resp, HttpServletResponse.SC_OK, "No changes were made");
        } catch (PartialChangeHappenedInDatabase p) {
            sendTextMessage(resp, HttpServletResponse.SC_OK, "Transaction successful, but only partial changes were made");
        }
    }

    @Override
    public void executeSelect(String sql) throws IOException {
        try {
            checkSqlValidity(sql, "SELECT");
            processResult(sql);
            } catch (ResultListIsNotEmptyException e) {
                sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                e.getMessage();
                e.printStackTrace();
            } catch (InvalidSqlQueryException i) {
                sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, i.getMessage());
                i.getMessage();
                i.printStackTrace();
            } catch (SQLException s) {
                sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, s.getMessage());
                s.printStackTrace();

            }
    }

    @Override
    public void send() throws IOException {
        if(result.size() > 0){
            sendMessage(resp, HttpServletResponse.SC_OK, result);
        } else {
            sendTextMessage(resp, HttpServletResponse.SC_NO_CONTENT, "No content found for this request");
        }
    }

    @Override
    public <T> void send(T modifiedResult) throws IOException {
        if(modifiedResult != null){
            sendMessage(resp, HttpServletResponse.SC_OK, modifiedResult);
        } else {
            sendTextMessage(resp, HttpServletResponse.SC_NO_CONTENT, "No content found for this request");
        }
    }


    @Override
    public String fetchUrl(HttpServletRequest req) throws IOException {
        StringBuffer requestURL = req.getRequestURL();
        if (req.getQueryString() != null) {
            requestURL.append("?").append(req.getQueryString());
        }
        return requestURL.toString();
    }

    @Override
    public Optional<String> fetchNumber(String url, String nonCapturingKeyword) throws IOException {
        try {
            String pattern = "(?:" + nonCapturingKeyword + ")(\\d+)";
            Pattern regex = Pattern.compile(pattern);
            Matcher m = regex.matcher(url);
            if (m.find()) {
                return Optional.of(m.group(1));
            }
        } catch (Exception e){
            sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            e.printStackTrace();

        }
        return Optional.empty();
    }

    @Override
    public Optional<Boolean> fetchBoolean(String url, String nonCapturingKeyword) throws IOException {
        try {
            String pattern = "(?:" + nonCapturingKeyword + ")(true|false)";
            Pattern regex = Pattern.compile(pattern);
            Matcher m = regex.matcher(url);
            if (m.find()) {
                if (m.group(1).equals("true")) {
                    return Optional.of(true);
                } else {
                    return Optional.of(false);
                }
            }
        }
        catch (Exception e){
            sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> fetchAPattern(String url, String regexPattern) throws IOException {
        try {
            Pattern regex = Pattern.compile(regexPattern);
            Matcher m = regex.matcher(url);
            if (m.find()) {
                return Optional.of(m.group(1));
            }
        }
        catch (Exception e){
            sendTextMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
}

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public List<Map<String, String>> getResult() {
        return result;
    }

    @Override
    public Optional<Map<String, String>> loginStranger(String tableName, String loginName, String password) throws SQLException {
        String sql = "SELECT * FROM " + tableName + ";";
        List<Map<String, String>> users = getService.getAllData(sql);
        for (Map<String,String> user: users) {
            if(user.containsValue(loginName) && user.containsValue(password)){
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public boolean getAutoSendState(){
        return autoSend;
    }

    private void checkSqlValidity(String sql, String keyword) throws InvalidSqlQueryException, IOException {
        if(!(fetchAPattern(sql, "(?i)(" + keyword + ")").isPresent())){
            throw new InvalidSqlQueryException("Unmatching query and method selected, expected a " + keyword + " statement in: " + sql);
        }
    }

    private void pickMethod(Method type, List<String> sqls) throws InvalidSqlQueryException, SQLException, NoChangeHappenedException, PartialChangeHappenedInDatabase, IOException {
        switch (type) {
            case UPDATE:
                for (String sql: sqls) {
                    checkSqlValidity(sql, "UPDATE");
                }
                modifyService.updateData(sqls);
                break;
            case INSERT:
                for (String sql: sqls) {
                    checkSqlValidity(sql, "INSERT");
                }
                modifyService.insertNewData(sqls);
                break;
            case DELETE:
                for (String sql: sqls) {
                    checkSqlValidity(sql, "DELETE");
                }
                modifyService.deleteData(sqls);
                break;
            case MIXED:
                for (String sql: sqls) {
                    checkSqlValidity(sql, "DELETE|INSERT|UPDATE");
                }
                modifyService.mixedTransaction(sqls);
                break;
        }
    }

    private void processResult(String sql) throws SQLException, IOException, ResultListIsNotEmptyException {
        if (result == null) {
            result = getService.getAllData(sql);
            if(autoSend){
                send();
            }
        } else {
            throw new ResultListIsNotEmptyException("Result list is not null, you can only executeSelect a given type of query once");
        }
    }

    public void deleteResult(){
        result = null;
    }
}
