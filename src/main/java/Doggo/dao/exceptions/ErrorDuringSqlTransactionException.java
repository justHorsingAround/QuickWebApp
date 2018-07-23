package Doggo.dao.exceptions;

public class ErrorDuringSqlTransactionException extends Exception {
    public ErrorDuringSqlTransactionException() {
    }

    public ErrorDuringSqlTransactionException(String message) {
        super(message);
    }
}
