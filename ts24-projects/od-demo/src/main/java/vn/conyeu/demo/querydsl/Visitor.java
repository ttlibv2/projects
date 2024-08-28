package vn.conyeu.demo.querydsl;

/**
 * {@code Visitor} defines a visitor signature for {@link Expression} instances.
 * @param <R> Return type
 * @param <C> Context type
 */
public interface Visitor<R, C> {

    /**
     * Visit a Constant instance with the given context
     *
     * @param expr    expression to visit
     * @param context context of the visit or null, if not used
     * @return visit result
     */
    R visit(Constant expr, C context);

    /**
     * Visit an Operation instance with the given context
     *
     * @param expr    expression to visit
     * @param context context of the visit or null, if not used
     * @return visit result
     */
    R visit(Operation<?> expr, C context);

    /**
     * Visit a Path instance with the given context
     *
     * @param expr    expression to visit
     * @param context context of the visit or null, if not used
     * @return visit result
     */
    R visit(Path<?> expr, C context);

    /**
     * Visit a TemplateExpression instance with the given context
     *
     * @param expr    expression to visit
     * @param context context of the visit or null, if not used
     * @return visit result
     */
    R visit(TemplateExpression expr, C context);
}