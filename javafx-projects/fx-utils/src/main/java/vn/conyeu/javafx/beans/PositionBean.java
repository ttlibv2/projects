package vn.conyeu.javafx.beans;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class PositionBean {
    private final DoubleProperty x = new SimpleDoubleProperty(0);
    private final DoubleProperty y = new SimpleDoubleProperty(0);

    public PositionBean() {
    }

    public PositionBean(double x, double y) {
        setX(x);
        setY(y);
    }

    public static PositionBean of(double x, double y) {
        return new PositionBean(x, y);
    }

    /**
     * @return a new {@code PositionBean} object with both x and y set to 0
     */
    public static PositionBean origin() {
        return of(0, 0);
    }


    @Override
    public String toString() {
        return "X|Y (" + getX() + "; " + getY() + ")";
    }

    public double getX() {
        return x.get();
    }

    /**
     * The x coordinate property.
     */
    public DoubleProperty xProperty() {
        return x;
    }

    public void setX(double xPosition) {
        this.x.set(xPosition);
    }

    public double getY() {
        return y.get();
    }

    /**
     * The y coordinate property
     */
    public DoubleProperty yProperty() {
        return y;
    }

    public void setY(double yPosition) {
        this.y.set(yPosition);
    }
}