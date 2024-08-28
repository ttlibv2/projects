package vn.conyeu.demo.querydsl;

import com.querydsl.core.types.Templates;

public abstract class ExpressionBase<T> implements Expression<T> {
    private final Class<? extends T> type;
    private transient volatile String toString;
    private transient volatile Integer hashCode;

    public ExpressionBase(Class<? extends T> type) {
        this.type = type;
    }

    public final Class<? extends T> getType() {
        return type;
    }

    public final int hashCode() {
        if (hashCode == null) {
            hashCode = accept(HashCodeVisitor.DEFAULT, null);
        }
        return hashCode;
    }

    public final String toString() {
        if (toString == null) {
            toString = accept(ToStringVisitor.DEFAULT, Templates.DEFAULT);
        }
        return toString;
    }
}