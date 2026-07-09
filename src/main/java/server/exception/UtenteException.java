package main.java.server.exception;

/**
 *
 */
public class UtenteException extends RuntimeException {
    public UtenteException(String message) {
        super(message);
    }

    public UtenteException(String message, Throwable cause) {
        super(message, cause);
    }
}
