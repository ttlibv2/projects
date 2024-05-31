package vn.conyeu.javafx.property;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Paint;

public class PaintProperty extends SimpleObjectProperty<Paint> {

    /**
     * The constructor of {@code ObjectProperty}
     */
    public PaintProperty() {
    }

    /**
     * The constructor of {@code ObjectProperty}
     *
     * @param initialValue the initial value of the wrapped value
     */
    public PaintProperty(Paint initialValue) {
        super(initialValue);
    }

    /**
     * The constructor of {@code ObjectProperty}
     *
     * @param bean the bean of this {@code ObjectProperty}
     * @param name the name of this {@code ObjectProperty}
     */
    public PaintProperty(Object bean, String name) {
        super(bean, name);
    }

    /**
     * The constructor of {@code ObjectProperty}
     *
     * @param bean         the bean of this {@code ObjectProperty}
     * @param name         the name of this {@code ObjectProperty}
     * @param initialValue the initial value of the wrapped value
     */
    public PaintProperty(Object bean, String name, Paint initialValue) {
        super(bean, name, initialValue);
    }
}