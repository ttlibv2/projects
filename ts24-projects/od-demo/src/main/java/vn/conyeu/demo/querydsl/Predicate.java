package vn.conyeu.demo.querydsl;

public interface Predicate extends Expression<Boolean> {

    /**
     * Get the negation of the expression
     *
     * @return negation
     */
    Predicate not();

}