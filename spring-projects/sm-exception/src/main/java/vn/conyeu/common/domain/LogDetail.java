package vn.conyeu.common.domain;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Getter;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import vn.conyeu.common.exception.BaseException;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;

import java.io.Serializable;
import java.util.Map;

@Getter
public class LogDetail implements Serializable {

    public static LogDetail e500() {
        return eStatus(500);
    }

    public static LogDetail e401() {
        return eStatus(401);
    }

    public static LogDetail e401(String code) {
        return eStatus(401).logCode(code);
    }


    public static LogDetail e403() {
        return eStatus(403);
    }

    public static LogDetail e400() {
        return eStatus(400);
    }

    public static LogDetail e404() {
        return eStatus(404);
    }

    public static LogDetail eStatus(int status) {
        return new LogDetail().status(status);
    }

    public static LogDetail eStatus(HttpStatus status) {
        return new LogDetail().status(status);
    }

    //---------------------------------------

    private Integer status;
    private String message;
    private String code;
    private String path;
    private String exception;
    private Throwable throwable;
    private String codeI18N;

    private ObjectMap custom;


    /**
     * Set the httpStatus
     *
     * @param httpStatus the value
     */
    public LogDetail status(HttpStatus httpStatus) {
        return status(httpStatus.value());
    }

    /**
     * Set the httpStatus
     *
     * @param status the value
     */
    public LogDetail status(Integer status) {
        this.status = status;
        if(code == null) code = "e_"+status;
        return this;
    }

    /**
     * Set the message
     * @param message the value
     */
    public LogDetail message(String message) {
        this.message = message;
        return this;
    }
    /**
     * Set the message
     * @param message the value
     */
    public LogDetail message(String message, Object...arguments) {
        this.message = Objects.formatString(message, arguments);
        return this;
    }

    /**
     * Set the logCode
     * @param logCode the value
     */
    public LogDetail logCode(String logCode) {
        this.code = logCode;
        if(codeI18N == null) codeI18N = code;
        return this;
    }

    /**
     * Set the codeI18N
     *
     * @param codeI18N the value
     */
    public LogDetail codeI18N(String codeI18N) {
        this.codeI18N = codeI18N;
        return this;
    }

    /**
     * Set the logPath
     * @param logPath the value
     */
    public LogDetail logPath(String logPath) {
        this.path = logPath;
        return this;
    }

    /**
     * Set the throwable
     * @param throwable the value
     */
    public LogDetail throwable(Throwable throwable) {
        this.throwable = throwable;

        if(throwable != null && exception == null) {
            this.exception = throwable.getClass().getName();
        }

        return this;
    }

    /**
     * Set the exception
     *
     * @param exception the value
     */
    public LogDetail exception(String exception) {
        this.exception = exception;
        return this;
    }

    /**
     * Set the arguments
     */
    public LogDetail arguments(String field, Object data) {
        getArguments().set(field, data);
        return this;
    }

    public ObjectMap getArguments() {
        return getCustom().computeIfAbsent("arguments", k -> ObjectMap.create());
    }

    /**
     * Set the other
     * @param other the value
     */
    public LogDetail other(Map<String, ?> other) {
        this.custom = ObjectMap.fromMap(other);
        return this;
    }

    public LogDetail detail(String field, Object data) {
       getCustom().computeIfAbsent("details", k -> ObjectMap.create()).set(field, data);
        return this;
    }

    public LogDetail detail(Map<String, Object> map) {
        for(Map.Entry<String, Object> entry:map.entrySet()) {
            detail(entry.getKey(), entry.getValue());
        }
        return this;
    }

    /**
     * Returns the codeI18N
     */
    public String getCodeI18N() {
        return Objects.firstNotBlank(codeI18N, code);
    }

    /**
     * Returns the other
     */
    @JsonAnyGetter
    public ObjectMap getCustom() {
        custom = ObjectMap.ifNull(custom);
        return custom;
    }

    public LogDetail custom(String field, Object data) {
        getCustom().set(field, data);
        return this;
    }

    public LogDetail copy() {
        return eStatus(status)
                .message(message)
                .codeI18N(codeI18N).logCode(code)
                .exception(exception)
                .logPath(path).throwable(throwable)
                .other(ObjectMap.clone(custom));
    }

    public LogDetail clone() {
        return copy();
    }


    public static LogDetail fromRequest(Map<String, Object> map) {
        ObjectMap objectMap = ObjectMap.fromMap(map);
        Object throwable = objectMap.get("throwable");
        if(throwable instanceof BaseException exp) {
            return exp.getObject();
        }

        return new LogDetail()
                .status(objectMap.getInteger("status", 500))
                .message("Xảy ra lỗi từ máy chủ")
                .custom("msg_detail", objectMap.getString("message"))
                .custom("origin", map);
    }

    public ObjectMap createMapResponse(ErrorAttributeOptions options) {

        ObjectMap map = ObjectMap
                .setNew("code", getCode())
                .set("summary", I18N.get(getCodeI18N(), message))
                .set(getCustom().delete("trace"));

        if(options.isIncluded(Include.STACK_TRACE)) {
            Object trace = getCustom().getString("trace", null);
            if(trace == null && throwable != null) trace = throwable;
            if(trace != null) map.set("trace", trace);
        }

        if(options.isIncluded(Include.EXCEPTION)) {
            String exp = exception != null ? exception : throwable != null ? throwable.getClass().getName() : null;
            if(exp != null) map.set("exp", exp);
        }

        return map;
    }

    public ObjectMap createMapResponse() {
        ErrorAttributeOptions options = ErrorAttributeOptions.of();
        return createMapResponse(options);
    }

    public ResponseEntity<Object> createResponse() {
        ErrorAttributeOptions options = ErrorAttributeOptions.of();
        return createResponse(options);
    }

    public ResponseEntity<Object> createResponse(ErrorAttributeOptions options) {
        return ResponseEntity.status(status).body(createMapResponse(options));
    }

}