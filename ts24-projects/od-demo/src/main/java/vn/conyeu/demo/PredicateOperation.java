package vn.conyeu.demo;


import com.querydsl.core.types.Visitor;
import vn.conyeu.demo.querydsl.Predicate;

public class PredicateOperation implements Predicate {

    @Override
    public <R, C> R accept(Visitor<R, C> v, C context) {
        return null;
    }

    @Override
    public Class<? extends Boolean> getType() {
        return null;
    }

    @Override
    public Predicate not() {
        return null;
    }
}