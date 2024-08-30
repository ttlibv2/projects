package vn.conyeu.google.query;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import vn.conyeu.google.drives.builder.QueryClass;

public abstract class BoolExpression extends DslExpression<Boolean> implements Predicate {
    private transient volatile BoolExpression not;
    private transient volatile BoolExpression eqTrue, eqFalse;

    public BoolExpression(Expression<Boolean> mixin) {
        super(mixin);
    }

    /**
     * Create a {@code this && right} expression
     *
     * <p>Returns an intersection of this and the given expression</p>
     * @param right right hand side of the union
     * @return {@code this &amp;&amp; right}
     */
    public BoolExpression and(Predicate right) {
        right = (Predicate) ExpressionUtils.extract(right);
        if (right != null)
            return new QueryClass.BoolOp(QueryClass.DriveOps.AND, mixin, right);
        else return this;
    }

    /**
     * Create a {@code this || right} expression
     *
     * <p>Returns a union of this and the given expression</p>
     * @param right right hand side of the union
     * @return this || right
     */
    public BoolExpression or(Predicate right) {
        right = (Predicate) ExpressionUtils.extract(right);
        if (right != null)
            return new QueryClass.BoolOp(QueryClass.DriveOps.OR, mixin, right);
        else return this;
    }

    /**
     * Create a {@code !this} expression
     *
     * <p>Returns a negation of this boolean expression</p>
     * @return !this
     */
    @Override
    public BoolExpression not() {
        if (not == null) {
            not = new QueryClass.BoolOp(QueryClass.DriveOps.NOT, this);
        }
        return not;
    }

    /**
     * Create a {@code this == true} expression
     * @return this == true
     */
    public BoolExpression isTrue() {
        if (eqTrue == null) {
            eqTrue = eq(true);
        }
        return eqTrue;
    }

    /**
     * Create a {@code this == false} expression
     * @return this == false
     */
    public BoolExpression isFalse() {
        if (eqFalse == null) {
            eqFalse = eq(false);
        }
        return eqFalse;
    }

    private BoolExpression eq(Boolean right) {
        Expression<Boolean> constant = Expressions.constant(right);
        return new QueryClass.BoolOp(QueryClass.DriveOps.EQUAL, mixin, constant);
    }

}