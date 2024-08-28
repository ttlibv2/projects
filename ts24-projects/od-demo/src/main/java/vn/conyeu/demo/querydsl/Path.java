package vn.conyeu.demo.querydsl;

import java.lang.reflect.AnnotatedElement;

public interface Path<T> extends Expression<T> {

    /**
     * Get the metadata for this path
     *
     * @return path metadata
     */
    PathMetadata getMetadata();

    /**
     * Get the root for this path
     *
     * @return root of path
     */
    Path getRoot();

    /**
     * Return the annotated element related to the given path
     * <p>For property paths the annotated element contains the annotations of the
     * related field and/or getter method and for all others paths the annotated element
     * is the expression type.</p>
     *
     * @return annotated element
     */
    AnnotatedElement getAnnotatedElement();



}