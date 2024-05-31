package vn.conyeu.javafx.property;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

import java.util.function.BiFunction;

/**
 * Simply an {@link ObjectProperty} that wraps a {@link BiFunction}.
 *
 * @param <T> the function's first argument
 * @param <U> the function's second argument
 * @param <R> the function's return type
 */
public class BiFunctionProperty<T, U, R> extends ReadOnlyObjectWrapper<BiFunction<T, U, R>> {

    //================================================================================
    // Constructors
    //================================================================================
    public BiFunctionProperty() {
    }

    public BiFunctionProperty(BiFunction<T, U, R> initialValue) {
        super(initialValue);
    }

    public BiFunctionProperty(Object bean, String name) {
        super(bean, name);
    }

    public BiFunctionProperty(Object bean, String name, BiFunction<T, U, R> initialValue) {
        super(bean, name, initialValue);
    }
}