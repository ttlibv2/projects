package vn.conyeu.javafx.property;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

import java.util.function.BiPredicate;

/**
 * Simply an {@link ObjectProperty} that wraps a {@link BiPredicate}.
 *
 * @param <T> the predicate's first argument
 * @param <U> the predicate's second argument
 */
public class BiPredicateProperty<T, U> extends ReadOnlyObjectWrapper<BiPredicate<T, U>> {

    //================================================================================
    // Constructors
    //================================================================================
    public BiPredicateProperty() {
    }

    public BiPredicateProperty(BiPredicate<T, U> initialValue) {
        super(initialValue);
    }

    public BiPredicateProperty(Object bean, String name) {
        super(bean, name);
    }

    public BiPredicateProperty(Object bean, String name, BiPredicate<T, U> initialValue) {
        super(bean, name, initialValue);
    }
}