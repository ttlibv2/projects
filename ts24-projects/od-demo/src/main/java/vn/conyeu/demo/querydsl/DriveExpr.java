package vn.conyeu.demo.querydsl;

import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.util.MathUtils;

public abstract class DriveExpr<T> extends SimpleExpr<T> {

    public DriveExpr(Expression<T> mixin) {
        super(mixin);
    }

    @Override
    public BooleanExpression eq(T right) {
        return super.eq(right);
    }

    /**
     * Create a {@code this != right} expression
     * @param right rhs of the comparison
     * @return this != right
     */
    protected BooleanExpression neq(T right) {
        return new BooleanOperation(GOps.NE, mixin, constant(right));
    }

    /**
     * Create a {@code this > right} expression
     *
     * @param <A>
     * @param right rhs of the comparison
     * @return {@code this > right}
     * @see java.lang.Comparable#compareTo(Object)
     */
    protected <A extends Number & Comparable> BooleanExpression gt(A right) {
        return gt(constant(cast(right)));
    }

    private T cast(Number number) {
        return MathUtils.cast(number, getType());
    }
}