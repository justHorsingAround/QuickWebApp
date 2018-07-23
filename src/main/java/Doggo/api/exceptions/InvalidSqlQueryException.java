package Doggo.api.exceptions;

public class InvalidSqlQueryException extends Exception {
    public InvalidSqlQueryException(String message) {
        super(message);
    }
}
