package vn.conyeu.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import vn.conyeu.common.domain.LogDetail;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.commons.utils.MapperHelper;

import java.util.Map;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class BaseException extends RuntimeException {

    public static BaseException e500(String code) {
        return eStatus(500, code);
    }

    public static BaseException e401(String code) {
        return eStatus(401, code);
    }

    public static BaseException e400(String code) {
        return eStatus(400, code);
    }

    public static BaseException e404(String code) {
        return eStatus(404, code);
    }

    public static BaseException eStatus(int status) {
        return eStatus(status, "e_"+status);
    }

    public static BaseException eStatus(int status, String code) {
        return eStatus(HttpStatus.valueOf(status), code);
    }

    public static BaseException eStatus(HttpStatus status, String code) {
        return new BaseException(status).code(code);
    }

    protected final LogDetail object;

    /**
     * Create exception with status 500
     */
    public BaseException() {
        this(LogDetail.e500());
    }

    public BaseException(LogDetail detail) {
        this.object = Asserts.notNull(detail);
    }

    protected BaseException(HttpStatus status) {
        this(LogDetail.eStatus(status));
    }

    /**
     * Set the message
     *
     * @param message the value
     */
    public BaseException message(String message) {
        object.message(message);
        return this;
    }

    /**
     * Set the message
     *
     * @param message   the value
     * @param arguments
     */
    public BaseException message(String message, Object... arguments) {
        object.message(message, arguments);
        return this;
    }

    /**
     * Set the logCode
     *
     * @param logCode the value
     */
    public BaseException code(String logCode) {
        object.logCode(logCode);
        return this;
    }

    /**
     * Set the logPath
     *
     * @param logPath the value
     */
    public BaseException logPath(String logPath) {
        object.logPath(logPath);
        return this;
    }

    /**
     * Set the throwable
     *
     * @param throwable the value
     */
    public BaseException throwable(Throwable throwable) {
        object.throwable(throwable);
        return this;
    }

    /**
     * Set the other
     *
     * @param other the value
     */
    public BaseException other(Map<String, ?> other) {
        object.other(other);
        return this;
    }

    /**
     * Set the arguments
     */
    public BaseException arguments(String field, Object data) {
        object.detail(field, data);
        return this;
    }

    public BaseException arguments(Map<String, Object> map) {
        object.detail(map);
        return this;
    }

    public BaseException details(Object data) {
        object.detail("details", data);
        return this;
    }

    public BaseException status(HttpStatus httpStatus) {
        object.status(httpStatus);
        return this;
    }

    public BaseException status(Integer httpStatus) {
        object.status(httpStatus);
        return this;
    }

    /**
     * Returns the object
     */
    public LogDetail getObject() {
        return object;
    }

    @Override
    public String getMessage() {
        return MapperHelper.serializeToString(object);
    }
}