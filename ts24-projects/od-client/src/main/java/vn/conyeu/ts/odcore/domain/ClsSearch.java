package vn.conyeu.ts.odcore.domain;

import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;

import java.io.Serializable;

public class ClsSearch implements Serializable {
    private ObjectMap data;
    private ClsOperator operator = ClsOperator.like;
    private Integer limit = 20;
    private Integer offset = 0;

    public static ClsSearch forData(ObjectMap data) {
        return forData(data, ClsOperator.like);
    }

    public static ClsSearch forData(ObjectMap data, ClsOperator operator) {
        return new ClsSearch(data, operator);
    }

    public ClsSearch() {
    }

    public ClsSearch(ObjectMap data) {
        this(data, ClsOperator.like);
    }

    public ClsSearch(ObjectMap data, ClsOperator operator) {
        setData(data).setOperator(operator);
    }

    /**
     * Returns the operator
     */
    public ClsOperator getOperator() {
        return operator;
    }

    /**
     * Set the operator
     *
     * @param operator the value
     */
    public ClsSearch setOperator(ClsOperator operator) {
        this.operator = operator;
        return this;
    }

    /**
     * Returns the data
     */
    public ObjectMap getData() {
        return data;
    }

    /**
     * Set the data
     *
     * @param data the value
     */
    public ClsSearch setData(ObjectMap data) {
        this.data = data;
        return this;
    }

    /**
     * Returns the size
     */
    public Integer getLimit() {
        return Objects.firstNotNull(limit, 20);
    }

    /**
     * Set the size
     *
     * @param limit the value
     */
    public ClsSearch setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    /**
     * Returns the offset
     */
    public Integer getOffset() {
        return Objects.firstNotNull(offset, 0);
    }

    /**
     * Set the offset
     *
     * @param offset the value
     */
    public ClsSearch setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    public ClsPage newPage() {
        return new ClsPage().limit(getLimit()).offset(getOffset());
    }
}