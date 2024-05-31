package vn.conyeu.commons.exception;

public class ClassException extends RuntimeException {
    final Object object;

    public ClassException(Object object, String message) {
       this(null, message, null);
    }

    public ClassException(Object object, String message, Throwable throwable) {
        super(message, throwable);
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

}