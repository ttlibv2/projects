package vn.conyeu.google.query;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;

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
        right = Utils.extractPredicate(right);
        if (right != null) return new BoolOp(GOps.AND, mixin, right);
        else return this;
    }

    /**
     * Create a {@code this && any(predicates)} expression
     *
     * <p>Returns an intersection of this and the union of the given predicates</p>
     *
     * @param predicates union of predicates
     * @return this &amp;&amp; any(predicates)
     */
    public BoolExpression andAnyOf(Predicate... predicates) {
        Expression expr = Utils.anyOf(predicates);
        return new BoolOp(GOps.AND_ALL, mixin, expr);
    }

    /**
     * Create a {@code this && any(predicates)} expression
     *
     * <p>Returns an intersection of this and the union of the given predicates</p>
     *
     * @param predicates union of predicates
     * @return this &amp;&amp; all(predicates)
     */
    public BoolExpression and(Predicate... predicates) {
        Expression expr = Utils.allOf(predicates);
        return new BoolOp(GOps.AND_ALL, mixin, expr);
    }

    /**
     * Create a {@code this or all(predicates)} expression
     * <p>Return a union of this and the intersection of the given predicates</p>
     *
     * @param predicates intersection of predicates
     * @return this or all(predicates)
     */
    public BoolExpression orAllOf(Predicate... predicates) {
        Expression expr = Utils.allOf(predicates);
        return new BoolOp(GOps.OR_ALL, mixin, expr);
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
            return new BoolOp(GOps.OR, mixin, right);
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
            not = new BoolOp(GOps.NOT, this);
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
        return new BoolOp(GOps.EQUAL, mixin, constant);
    }

}