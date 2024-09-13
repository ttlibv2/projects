//package vn.conyeu.google.xsldb.query;
//
//import com.querydsl.core.types.*;
//import com.querydsl.core.types.dsl.BooleanExpression;
//import com.querydsl.core.types.dsl.Expressions;
//import com.querydsl.jpa.JPQLTemplates;
//
//public abstract class XlBoolean extends XlExpression<Boolean> implements Predicate {
//    private transient volatile XlBoolean eqTrue, eqFalse;
//    private transient volatile XlBoolean not;
//
//    public XlBoolean(Expression<Boolean> mixin) {
//        super(mixin);
//    }
//
//    @Override
//    public XlBoolean as(String alias) {
//        return super.as(alias);
//    }
//
//
//    /**
//     * Create a {@code this && right} expression
//     *
//     * <p>Returns an intersection of this and the given expression</p>
//     *
//     * @param right right hand side of the union
//     * @return {@code this &amp;&amp; right}
//     */
//    public BooleanExpression and(@Nullable Predicate right) {
//        right = (Predicate) ExpressionUtils.extract(right);
//        if (right != null) {
//            return Expressions.booleanOperation(Ops.AND, mixin, right);
//        } else {
//            return this;
//        }
//    }
//
//    /**
//     * Create a {@code this || right} expression
//     *
//     * <p>Returns a union of this and the given expression</p>
//     *
//     * @param right right hand side of the union
//     * @return this || right
//     */
//    public BooleanExpression or(@Nullable Predicate right) {
//        right = (Predicate) ExpressionUtils.extract(right);
//        if (right != null) {
//            return Expressions.booleanOperation(Ops.OR, mixin, right);
//        } else {
//            return this;
//        }
//    }
//
//
//    /**
//     * Create a {@code !this} expression
//     *
//     * <p>Returns a negation of this boolean expression</p>
//     *
//     * @return !this
//     */
//    @Override
//    public BooleanExpression not() {
//        if (not == null) {
//            // uses this, because it makes unwrapping easier
//            not = Expressions.booleanOperation(Ops.NOT, this);
//        }
//        return not;
//    }
//
//    /**
//     * Create a {@code this || right} expression
//     *
//     * <p>Returns a union of this and the given expression</p>
//     *
//     * @param right right hand side of the union
//     * @return this || right
//     */
//    public BooleanExpression or(@Nullable Predicate right) {
//        right = (Predicate) ExpressionUtils.extract(right);
//        if (right != null) {
//            return Expressions.booleanOperation(Ops.OR, mixin, right);
//        } else {
//            return this;
//        }
//    }
//
//
//    /**
//     * Create a {@code this or all(predicates)} expression
//     *
//     * <p>Return a union of this and the intersection of the given predicates</p>
//     *
//     * @param predicates intersection of predicates
//     * @return this or all(predicates)
//     */
//    public BooleanExpression orAllOf(Predicate... predicates) {
//        return or(ExpressionUtils.allOf(predicates));
//    }
//
//
//    /**
//     * Create a {@code this == true} expression
//     *
//     * @return this == true
//     */
//    public BooleanExpression isTrue() {
//        return eq(true);
//    }
//
//    /**
//     * Create a {@code this == false} expression
//     *
//     * @return this == false
//     */
//    public BooleanExpression isFalse() {
//        return eq(false);
//    }
//
//    @Override
//    public BooleanExpression eq(Boolean right) {
//        if (right) {
//            if (eqTrue == null) {
//                eqTrue = super.eq(true);
//            }
//            return eqTrue;
//        } else {
//            if (eqFalse == null) {
//                eqFalse = super.eq(false);
//            }
//            return eqFalse;
//        }
//    }
//
//    JPQLTemplates
//
//
//
//
//
//
//    public static class XlBooleanOp extends XlBoolean {
//
//    }
//
//
//}