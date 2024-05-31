package vn.conyeu.ts.odcore.domain;

import lombok.EqualsAndHashCode;
import vn.conyeu.commons.utils.Objects;

@EqualsAndHashCode(callSuper = false)
public class ClsRequest extends ClsModel<ClsRequest> {
    private String jsonrpc = "2.0";
    private final Integer id = Objects.randomNumber(7);
    private final String method = "call";
    private Object params;

    public ClsRequest(Object params) {
        this.params = params;
    }

    public static ClsRequest fromObject(Object body) {
        if(body instanceof ClsRequest od) return od;
        else return new ClsRequest(body);
    }

    /**
     * Returns the jsonrpc
     */
    public String getJsonrpc() {
        return jsonrpc;
    }

    /**
     * Set the jsonrpc
     *
     * @param jsonrpc the value
     */
    public ClsRequest setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
        return this;
    }

    /**
     * Returns the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Returns the method
     */
    public String getMethod() {
        return method;
    }

    /**
     * Returns the params
     */
    public Object getParams() {
        return params;
    }

    /**
     * Set the params
     *
     * @param params the value
     */
    public ClsRequest setParams(Object params) {
        this.params = params;
        return this;
    }
}