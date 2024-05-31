package vn.conyeu.javafx.property;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

import java.util.function.Predicate;

/**
 * Simply an {@link ObjectProperty} that wraps a {@link Predicate}.
 *
 * @param <T> the predicate's input type
 */
public class PredicateProperty<T> extends ReadOnlyObjectWrapper<Predicate<T>> {

    //================================================================================
    // Constructors
    //================================================================================
    public PredicateProperty() {
    }

    public PredicateProperty(Predicate<T> initialValue) {
        super(initialValue);
    }

    public PredicateProperty(Object bean, String name) {
        super(bean, name);
    }

    public PredicateProperty(Object bean, String name, Predicate<T> initialValue) {
        super(bean, name, initialValue);
    }
}