package main.java.server.exception;

public class PreferitiException extends RuntimeException {

    public PreferitiException(String message) {
        super(message);
    }

    public PreferitiException(String message, Throwable cause) {
        super(message, cause);
    }
}
