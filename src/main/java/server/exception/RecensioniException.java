package main.java.server.exception;

public class RecensioniException extends RuntimeException {
    public RecensioniException(String message) {
        super(message);
    }
    public RecensioniException(String message, Throwable cause) {
        super(message, cause);
    }
}
