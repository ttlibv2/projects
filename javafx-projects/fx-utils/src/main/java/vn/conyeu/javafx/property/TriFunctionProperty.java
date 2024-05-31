package vn.conyeu.javafx.property;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import vn.conyeu.commons.function.TriFunction;

/**
 * Simply an {@link ObjectProperty} that wraps a {@link TriFunction}.
 *
 * @param <T> the function's first argument
 * @param <U> the function's second argument
 * @param <V> the function's third argument
 * @param <R> the function's return type
 */
public class TriFunctionProperty<T, U, V, R> extends ReadOnlyObjectWrapper<TriFunction<T, U, V, R>> {

    //================================================================================
    // Constructors
    //================================================================================
    public TriFunctionProperty() {
    }

    public TriFunctionProperty(TriFunction<T, U, V, R> initialValue) {
        super(initialValue);
    }

    public TriFunctionProperty(Object bean, String name) {
        super(bean, name);
    }

    public TriFunctionProperty(Object bean, String name, TriFunction<T, U, V, R> initialValue) {
        super(bean, name, initialValue);
    }
}