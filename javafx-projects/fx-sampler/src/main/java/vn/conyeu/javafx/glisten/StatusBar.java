package vn.conyeu.javafx.glisten;

import javafx.css.*;
import javafx.css.SimpleStyleableBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

import javafx.collections.ObservableList;

import javafx.scene.control.Control;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import vn.conyeu.javafx.styleable.CssFactory;

import java.util.List;

public class StatusBar extends Region {

    public StatusBar() {
        getStyleClass().setAll("status-bar");
    }

    //@formatter:off

    /** color */
    private StyleableObjectProperty<Paint> color;
    public final Paint getColor() {return colorProperty().get();}
    public final void setColor(Paint value) {colorProperty().set(value);}
    public final StyleableObjectProperty<Paint> colorProperty() {
        if(color == null) {
            color = FACTORY.createPaint(this, "color", "-fx-color");
        }
        return color;
    }

    //@formatter:on

    /**
     * {@inheritDoc}
     */
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

    /**
     * Gets the {@code CssMetaData} associated with this class, which may include the {@code CssMetaData} of its superclasses.
     *
     * @return the {@code CssMetaData}
     */
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return FACTORY.getCssMetaData();
    }

    /**
     * Methods for creating instances of StyleableProperty
     */
    static final CssFactory<StatusBar> FACTORY = new CssFactory<>(Region.getClassCssMetaData()) {
        @Override
        protected void registerCss() {
            FACTORY.createPaintCss("-fx-color", StatusBar::colorProperty, Color.BLACK);
        }
    };
}