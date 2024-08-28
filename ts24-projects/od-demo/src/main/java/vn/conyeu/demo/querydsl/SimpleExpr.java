package vn.conyeu.demo.querydsl;

import vn.conyeu.commons.utils.Asserts;

public abstract class SimpleExpr<T> implements Expression<T> {
    protected final Expression<T> mixin;
    protected final int hashCode;

    public SimpleExpr(Expression<T> mixin) {
        this.mixin = mixin;
        this.hashCode = mixin.hashCode();
    }

    public final Class<? extends T> getType() {
        return mixin.getType();
    }

    public final int hashCode() {
        return hashCode;
    }

    public final String toString() {
        return mixin.toString();
    }

    public boolean equals(Object o) { // can be overwritten
        return mixin.equals(o);
    }


    /**
     * Create a {@code this == right} expression
     *
     * <p>Use expr.isNull() instead of expr.eq(null)</p>
     *
     * @param right rhs of the comparison
     * @return this == right
     */
    public BooleanExpression eq(T right) {
        Asserts.notNull(right, "eq(null) is not allowed. Use isNull() instead");
        return new BooleanOperation(GOps.EQ, mixin, constant(right));
    }

    protected Constant<T> constant(T value) {
        return ConstantImpl.create(value);
    }
}