package Doggo.dao.exceptions;

public class NoChangeHappenedException extends Exception {
    public NoChangeHappenedException() {
    }

    public NoChangeHappenedException(String message) {
        super(message);
    }
}
