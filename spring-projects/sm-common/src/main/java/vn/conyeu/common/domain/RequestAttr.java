package vn.conyeu.common.domain;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Getter;
import vn.conyeu.commons.beans.ObjectMap;

import java.io.Serializable;

@Getter
public class RequestAttr implements Serializable {
    private Long timestamp;
    private String exception;
    private Throwable trace;
    private String message;
    private Object errors;
    private ObjectMap object;
    private Integer status;

    @JsonAnyGetter
    public ObjectMap getObject() {
        object = ObjectMap.ifNull(object);
        return object;
    }

    @JsonAnySetter
    public void set(String field, Object data) {
        getObject().set(field, data);
    }

    /**
     * Set the errors
     *
     * @param errors the value
     */
    public RequestAttr errors(Object errors) {
        this.errors = errors;
        return this;
    }

    /**
     * Set the exception
     *
     * @param exception the value
     */
    public RequestAttr exception(String exception) {
        this.exception = exception;
        return this;
    }

    /**
     * Set the message
     *
     * @param message the value
     */
    public RequestAttr message(String message) {
        this.message = message;
        return this;
    }

    /**
     * Set the timestamp
     *
     * @param timestamp the value
     */
    public RequestAttr timestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    /**
     * Set the trace
     *
     * @param trace the value
     */
    public RequestAttr trace(Throwable trace) {
        this.trace = trace;
        return this;
    }

    /**
     * Set the status
     *
     * @param status the value
     */
    public RequestAttr status(Integer status) {
        this.status = status;
        return this;
    }
}