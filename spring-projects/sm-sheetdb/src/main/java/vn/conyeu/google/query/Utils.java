package vn.conyeu.google.query;

import com.querydsl.core.types.*;

import java.util.Arrays;
import java.util.Collection;


public final class Utils {

    public static Predicate extractPredicate(Predicate expr) {
        return (Predicate) ExpressionUtils.extract(expr);
    }

    /**
     * Create a new Operation expression
     *
     * @param operator operator
     * @param args operation arguments
     * @return operation expression
     */
    public static PredicateOperation predicate(Operator operator, Expression<?>... args) {
        return ExpressionUtils.predicate(operator, Arrays.asList(args));
    }

    /**
     * Create the intersection of the given arguments
     *
     * @param left lhs of expression
     * @param right rhs of expression
     * @return left and right
     */
    public static Predicate and(Predicate left, Predicate right) {
        left = extractPredicate(left);right = extractPredicate(right);
        if (left == null) return right;
        else if (right == null) return left;
        else return predicate(GOps.AND, left, right);
    }

    /**
     * Create a {@code left or right} expression
     *
     * @param left lhs of expression
     * @param right rhs of expression
     * @return left or right
     */
    public static Predicate or(Predicate left, Predicate right) {
        left = extractPredicate(left);
        right = extractPredicate(right);
        if (left == null) return right;
        else if (right == null) return left;
        else return predicate(GOps.OR, left, right);
    }
 
    /**
     * Create the union of the given arguments
     *
     * @param exprs predicates
     * @return union
     */
    public static Predicate anyOf(Predicate... exprs) {
        Predicate rv = null;
        for (Predicate b : exprs) {
            if (b != null) {
                rv = rv == null ? b : or(rv,b);
            }
        }
        return rv;
    }

    /**
     * Create the intersection of the given arguments
     *
     * @param exprs predicates
     * @return intersection
     */
    public static Predicate allOf(Predicate... exprs) {
        Predicate rv = null;
        for (Predicate b : exprs) {
            if (b != null) {
                rv = rv == null ? b : and(rv,b);
            }
        }
        return rv;
    }
}