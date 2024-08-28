package vn.conyeu.demo.querydsl;

import java.util.List;

public class PredicateOperation extends OperationImpl<Boolean> implements Predicate {
    private transient volatile Predicate not;

    public PredicateOperation(Operator operator, List<Expression> args) {
        super(Boolean.class, operator, args);
    }

    @Override
    public Predicate not() {
        if (not == null) {
            not = new PredicateOperation(GOps.NOT, List.of(this));
        }
        return not;
    }

}