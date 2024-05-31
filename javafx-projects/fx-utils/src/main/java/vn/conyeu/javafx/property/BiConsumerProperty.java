package vn.conyeu.javafx.property;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

import java.util.function.BiConsumer;

/**
 * Simply an {@link ObjectProperty} that wraps a {@link BiConsumer}.
 *
 * @param <T> the consumer's first argument
 * @param <U> the consumer's second argument
 */
public class BiConsumerProperty<T, U> extends ReadOnlyObjectWrapper<BiConsumer<T, U>> {

    //================================================================================
    // Constructors
    //================================================================================
    public BiConsumerProperty() {
    }

    public BiConsumerProperty(BiConsumer<T, U> initialValue) {
        super(initialValue);
    }

    public BiConsumerProperty(Object bean, String name) {
        super(bean, name);
    }

    public BiConsumerProperty(Object bean, String name, BiConsumer<T, U> initialValue) {
        super(bean, name, initialValue);
    }
}