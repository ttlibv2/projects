package vn.conyeu.google.core;

import vn.conyeu.commons.utils.Objects;

public class GoogleException extends RuntimeException {

    public GoogleException(String message, Object...arguments) {
        super(Objects.formatString(message, arguments));
    }

    public GoogleException(String message, Throwable cause) {
        super(message, cause);
    }

    public GoogleException(Throwable cause) {
        super(cause);
    }
}