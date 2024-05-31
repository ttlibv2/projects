package vn.conyeu.javafx.beans;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * This bean contains two {@link DoubleProperty} to keep track/specify the sizes of something
 * in terms of width and height.
 */
public final class SizeBean {

    public SizeBean(double width, double height) {
        setWidth(width);
        setHeight(height);
    }

    public static SizeBean of(double width, double height) {
        return new SizeBean(width, height);
    }

    /** width */
    private DoubleProperty width;
    public final double getWidth() {return widthProperty().get();}
    public final void setWidth(double value) {widthProperty().set(value);}
    public final DoubleProperty widthProperty() {
        if(width == null) {
            width = new SimpleDoubleProperty(this, "width", 0.0);
        }
        return width;
    }

    /** height */
    private DoubleProperty height;
    public final double getHeight() {return heightProperty().get();}
    public final void setHeight(double value) {heightProperty().set(value);}
    public final DoubleProperty heightProperty() {
        if(height == null) {
            height = new SimpleDoubleProperty(this, "height", 0.0);
        }
        return height;
    }
}