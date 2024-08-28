package vn.conyeu.demo.querydsl;

import java.util.List;

/**
 * {@code TemplateExpression} provides base types for custom expressions with integrated
 * serialization templates
 *
 * @author tiwe
 * @param <T> expression type
 */
public interface TemplateExpression<T> extends Expression<T> {

    /**
     * Get the argument with the given index
     *
     * @param index zero based index of element
     * @return element at index
     */
    Object getArg(int index);

    /**
     * Get the arguments of the custom expression
     *
     * @return expression argument
     */
    List<?> getArgs();

    /**
     * Get the serialization template for this custom expression
     *
     * @return template
     */
    Template getTemplate();

}