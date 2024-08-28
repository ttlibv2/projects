package vn.conyeu.demo.querydsl;

public abstract class BooleanExpression extends SimpleExpr<Boolean> implements Predicate {
    private transient volatile BooleanExpression eqTrue, eqFalse;
    private transient volatile BooleanExpression not;

    public BooleanExpression(Expression<Boolean> mixin) {
        super(mixin);
    }

    /**
     * Create a {@code this && right} expression
     *
     * <p>Returns an intersection of this and the given expression</p>
     *
     * @param right right hand side of the union
     * @return {@code this &amp;&amp; right}
     */
    public BooleanExpression and(Predicate right) {
        right = Utils.extract(right);
        return right == null ? this : new BooleanOperation(GOps.AND, mixin, right);
    }

    /**
     * Create a {@code !this} expression
     * <p>Returns a negation of this boolean expression</p>
     * @return !this
     */
    @Override
    public BooleanExpression not() {
        if (not == null) {
            not = new BooleanOperation(GOps.NOT, this);
        }
        return not;
    }

    /**
     * Create a {@code this || right} expression
     *
     * <p>Returns a union of this and the given expression</p>
     *
     * @param right right hand side of the union
     * @return this || right
     */
    public BooleanExpression or(Predicate right) {
        right = Utils.extract(right);
        return right == null ? this : new BooleanOperation(GOps.OR, mixin, right);
    }


    /**
     * Create a {@code this == true} expression
     * @return this == true
     */
    public BooleanExpression isTrue() {
        return eq(true);
    }

    /**
     * Create a {@code this == false} expression
     * @return this == false
     */
    public BooleanExpression isFalse() {
        return eq(false);
    }

    public BooleanExpression eq(Boolean right) {
        if (right) {
            if (eqTrue == null) eqTrue = super.eq(true);
            return eqTrue;
        } //
        else {
            if (eqFalse == null) eqFalse = super.eq(false);
            return eqFalse;
        }
    }
}