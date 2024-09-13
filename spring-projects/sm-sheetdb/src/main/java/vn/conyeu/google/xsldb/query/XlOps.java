package vn.conyeu.google.xsldb.query;

import com.querydsl.core.types.Operator;

public enum XlOps implements Operator {

    // general
    EQUAL(Boolean.class),
    NOT_EQUAL(Boolean.class),
    IS_NULL(Boolean.class),
    IS_NOT_NULL(Boolean.class),
    ALIAS(String.class),

    // Comparable
    BETWEEN(Boolean.class),
    GREATER_OR_EQ(Boolean.class),
    GREATER_THAN(Boolean.class),
    LESS_OR_EQ(Boolean.class),
    LESS_THAN(Boolean.class),

    // Boolean
    AND(Boolean.class),
    NOT(Boolean.class),
    OR(Boolean.class),

    // Number
    ADD(Number.class),
    DIV(Number.class),
    MULT(Number.class),
    SUB(Number.class),
    MOD(Boolean.class),

    // String
    LOWER(String.class),
    UPPER(String.class),
    MATCHES(Boolean.class),
    MATCHES_IC(Boolean.class),

    LIKE(Boolean.class),
    LIKE_IC(Boolean.class),
    LIKE_ESCAPE(Boolean.class),
    LIKE_ESCAPE_IC(Boolean.class),

    ENDS_WITH(Boolean.class),
    ENDS_WITH_IC(Boolean.class),

    STARTS_WITH(Boolean.class),
    STARTS_WITH_IC(Boolean.class),

    CONTAINS(Boolean.class),
    CONTAINS_IC(Boolean.class),

    //LABEL
    LABEL(String.class),

    //FORMAT
    FORMAT_STRING(String.class),
    FORMAT_BOOLEAN(String.class),


    ;
    final Class type;

    XlOps(Class type) {
        this.type = type;
    }

    /**
     * Returns the type
     */
    @Override
    public Class getType() {
        return type;
    }

    public enum AggOps implements Operator {
        MAX_AGG(Comparable.class),
        MIN_AGG(Comparable.class),
        AVG_AGG(Number.class),
        SUM_AGG(Number.class),
        COUNT_AGG(Number.class);

        final Class type;

        AggOps(Class type) {
            this.type = type;
        }

        /**
         * Returns the type
         */
        @Override
        public Class getType() {
            return type;
        }
    }

    public enum DateTimeOps implements Operator {
        YEAR(Integer.class),
        MONTH(Integer.class),
        DAY_OF_MONTH(Integer.class),
        HOUR(Integer.class),
        MINUTE(Integer.class),
        SECOND(Integer.class),
        MILLISECOND(Integer.class),
        QUARTER(Integer.class),
        DAY_OF_WEEK(Integer.class),
        NOW(Comparable.class),
        DIFF_DATE(Comparable.class),
        TO_DATE(Comparable.class),


        ;
        final Class type;
        DateTimeOps(Class type) {
            this.type = type;
        }

        /**
         * Returns the type
         */
        @Override
        public Class getType() {
            return type;
        }
    }
}