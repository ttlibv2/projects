package vn.conyeu.demo.querydsl;


import java.io.Serializable;

/**
 * {@code Operator} represents operator symbols.
 * <p>Implementations should be enums for automatic instance management.</p>
 */
public interface Operator extends Serializable {

    /**
     * Get the unique id for this Operator
     *
     * @return name
     */
    String name();

    /**
     * Get the result type of the operator
     *
     * @return type
     */
    Class<?> getType();

}