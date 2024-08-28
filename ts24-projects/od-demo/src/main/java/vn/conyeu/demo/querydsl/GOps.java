package vn.conyeu.demo.querydsl;

public enum GOps implements Operator {

    // general
    EQ(Boolean.class),
    NE(Boolean.class),

    // collection
    IN(Boolean.class), // cmp. contains
    NOT_IN(Boolean.class),

    // map
    HAS(Boolean.class),

    // Boolean
    AND(Boolean.class),
    NOT(Boolean.class),
    OR(Boolean.class),

    // Comparable
    BETWEEN(Boolean.class),
    GOE(Boolean.class),
    GT(Boolean.class),
    LOE(Boolean.class),
    LT(Boolean.class),

    // String
    LIKE(Boolean.class);

    private final Class<?> type;

    GOps(Class<?> type) {
        this.type = type;
    }

    @Override
    public Class<?> getType() {
        return type;
    }
}