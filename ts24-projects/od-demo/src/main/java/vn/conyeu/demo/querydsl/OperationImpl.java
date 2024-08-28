package vn.conyeu.demo.querydsl;

import java.util.Arrays;
import java.util.List;

public class OperationImpl<T> extends ExpressionBase<T> implements Operation<T>{
    private final List<Expression> args;
    private final Operator operator;

    public OperationImpl(Class<? extends T> type, Operator operator, Expression<?>... args) {
        this(type, operator, Arrays.asList(args));
    }

    public OperationImpl(Class<? extends T> type, Operator operator, List<Expression> args) {
        super(type);

        if (!operator.getType().isAssignableFrom(Utils.wrap(type))) {
            throw new IllegalArgumentException(operator.name());
        }

        this.operator = operator;
        this.args = Utils.unmodifiableList(args);
    }

    @Override
    public final Expression<?> getArg(int i) {
        return args.get(i);
    }

    @Override
    public final List<Expression> getArgs() {
        return args;
    }

    @Override
    public final Operator getOperator() {
        return operator;
    }

    @Override
    public final boolean equals(Object o) {
        if(o == this)return true;
        else if(!(o instanceof Operation<?> op)) return false;
        else return op.getOperator() == operator
                    && op.getArgs().equals(args)
                    && op.getType().equals(getType());
    }

    @Override
    public final <R, C> R accept(Visitor<R, C> v, C context) {
        return v.visit(this, context);
    }

}