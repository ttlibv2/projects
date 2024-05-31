package vn.conyeu.javafx.property;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

import java.util.Comparator;

/**
 * Simply an {@link ObjectProperty} that wraps a {@link Comparator}.
 *
 * @param <T> the type of objects that may be compared by the comparator
 */
public class ComparatorProperty<T> extends ReadOnlyObjectWrapper<Comparator<T>> {

    //================================================================================
    // Constructors
    //================================================================================
    public ComparatorProperty() {
    }

    public ComparatorProperty(Comparator<T> initialValue) {
        super(initialValue);
    }

    public ComparatorProperty(Object bean, String name) {
        super(bean, name);
    }

    public ComparatorProperty(Object bean, String name, Comparator<T> initialValue) {
        super(bean, name, initialValue);
    }
}