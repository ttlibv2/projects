package vn.conyeu.javafx.property;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

import java.util.function.Consumer;

/**
 * Simply an {@link ObjectProperty} that wraps a {@link Consumer}.
 *
 * @param <T> the consumer's input type
 */
public class ConsumerProperty<T> extends ReadOnlyObjectWrapper<Consumer<T>> {

    //================================================================================
    // Constructors
    //================================================================================
    public ConsumerProperty() {
    }

    public ConsumerProperty(Consumer<T> initialValue) {
        super(initialValue);
    }

    public ConsumerProperty(Object bean, String name) {
        super(bean, name);
    }

    public ConsumerProperty(Object bean, String name, Consumer<T> initialValue) {
        super(bean, name, initialValue);
    }
}