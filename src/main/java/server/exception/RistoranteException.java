package main.java.server.exception;

public class RistoranteException extends RuntimeException {

    public RistoranteException(String message) {
        super(message);
    }

    public RistoranteException(String message, Throwable cause) {
        super(message, cause);
    }
}
