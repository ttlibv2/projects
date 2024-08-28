package vn.conyeu.google.drives;

import vn.conyeu.commons.utils.Objects;

public class DriveException extends RuntimeException {

    public DriveException(String message, Object...arguments) {
        super(Objects.formatString(message, arguments));
    }

    public DriveException(Throwable cause) {
        super(cause);
    }

    public DriveException(String message, Throwable cause) {
        super(message, cause);
    }
}