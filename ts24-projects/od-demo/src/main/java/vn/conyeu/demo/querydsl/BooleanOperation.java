package vn.conyeu.demo.querydsl;

import java.util.Arrays;
import java.util.List;

public class BooleanOperation extends BooleanExpression implements Operation<Boolean> {
    private final PredicateOperation opMixin;

    protected BooleanOperation(PredicateOperation mixin) {
        super(mixin);
        this.opMixin = mixin;
    }

    protected BooleanOperation(Operator op, Expression... args) {
        this(op, Arrays.asList(args));
    }

    protected BooleanOperation(Operator op, List<Expression> args) {
        super(new PredicateOperation(op, args));
        opMixin = (PredicateOperation) mixin;
    }

    @Override
    public final <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(opMixin, context);
    }

    @Override
    public Expression<?> getArg(int index) {
        return opMixin.getArg(index);
    }

    @Override
    public List<Expression> getArgs() {
        return opMixin.getArgs();
    }

    @Override
    public Operator getOperator() {
        return opMixin.getOperator();
    }

    @Override
    public BooleanExpression not() {
        if (opMixin.getOperator() == GOps.NOT && opMixin.getArg(0) instanceof BooleanExpression) {
            return (BooleanExpression) opMixin.getArg(0);
        } else {
            return super.not();
        }
    }
}