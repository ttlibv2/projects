package vn.conyeu.demo.querydsl;

public interface Expression<T> {

    /**
     * Accept the visitor with the given context
     *
     * @param <R> return type
     * @param <C> context type
     * @param v visitor
     * @param context context of visit
     * @return result of visit
     */
    <R,C> R accept(Visitor<R,C> v, C context);

    /**
     * Get the java type for this expression
     *
     * @return type of expression
     */
    Class<? extends T> getType();

}