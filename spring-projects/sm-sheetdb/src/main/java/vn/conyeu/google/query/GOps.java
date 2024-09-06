package vn.conyeu.google.query;

import com.querydsl.core.types.Operator;

public enum GOps implements Operator {
    LIKE(Boolean.class),
    EQUAL(Boolean.class),
    NOT_EQUAL(Boolean.class),
    LESS_THAN(Boolean.class),
    LESS_OR_EQUAL(Boolean.class),
    GREATER_THAN(Boolean.class),
    GREATER_OR_EQUAL(Boolean.class),
    AND(Boolean.class),
    OR(Boolean.class),
    NOT(Boolean.class),
    AND_ALL(Boolean.class),
    OR_ALL(Boolean.class);

    final Class type;

    GOps(Class type) {
        this.type = type;
    }

    @Override
    public Class getType() {
        return type;
    }
}