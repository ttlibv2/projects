package vn.conyeu.google.query;

import com.querydsl.core.types.*;

import java.util.Arrays;
import java.util.List;

public class BoolOp extends BoolExpression implements Operation<Boolean> {
    private final PredicateOperation opMixin;

    public BoolOp(PredicateOperation opMixin) {
        super(opMixin);
        this.opMixin = opMixin;
    }

    public BoolOp(Operator op, Expression<?>... args) {
        this(op, Arrays.asList(args));
    }

    public BoolOp(Operator op, List<Expression<?>> args) {
        super(ExpressionUtils.predicate(op, args));
        opMixin = (PredicateOperation) mixin;
    }

    @Override
    public final <R, C> R accept(Visitor<R, C> v, C context) {
        return v.visit(opMixin, context);
    }

    @Override
    public Expression<?> getArg(int index) {
        return opMixin.getArg(index);
    }

    @Override
    public List<Expression<?>> getArgs() {
        return opMixin.getArgs();
    }

    @Override
    public Operator getOperator() {
        return opMixin.getOperator();
    }

    @Override
    public BoolExpression not() {
        if (opMixin.getOperator() == GOps.NOT && opMixin.getArg(0) instanceof BoolExpression arg) {
            return arg;
        } else {
            return super.not();
        }
    }
}