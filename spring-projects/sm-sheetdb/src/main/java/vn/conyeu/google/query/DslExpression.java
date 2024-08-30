package vn.conyeu.google.query;

import com.querydsl.core.types.Expression;

public abstract class DslExpression<T> implements Expression<T> {
    protected final Expression<T> mixin;
    protected final int hashCode;

    public DslExpression(Expression<T> mixin) {
        this.mixin = mixin;
        this.hashCode = mixin.hashCode();
    }

    public final Class<? extends T> getType() {
        return mixin.getType();
    }

    public boolean equals(Object o) { // can be overwritten
        return mixin.equals(o);
    }

    public final int hashCode() {
        return hashCode;
    }

    public final String toString() {
        return mixin.toString();
    }
}