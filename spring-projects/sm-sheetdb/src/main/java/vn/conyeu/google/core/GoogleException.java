package vn.conyeu.google.core;

public class GoogleException extends RuntimeException {

    public GoogleException(String message) {
        super(message);
    }

    public GoogleException(String message, Throwable cause) {
        super(message, cause);
    }

    public GoogleException(Throwable cause) {
        super(cause);
    }
}