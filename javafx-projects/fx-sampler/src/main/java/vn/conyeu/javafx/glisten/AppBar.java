package vn.conyeu.javafx.glisten;

import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleablePropertyFactory;
import javafx.css.SimpleStyleableIntegerProperty;
import javafx.css.SimpleStyleableBooleanProperty;
import javafx.css.SimpleStyleableDoubleProperty;
import javafx.css.SimpleStyleableBooleanProperty;
import javafx.css.SimpleStyleableFloatProperty;
import javafx.css.SimpleStyleableLongProperty;
import javafx.css.SimpleStyleableStringProperty;
import javafx.css.SimpleStyleableObjectProperty;
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
import javafx.scene.layout.Pane;
import vn.conyeu.javafx.styleable.CssFactory;

import java.util.List;

public class AppBar extends Pane {

    public AppBar() {
    }

    //@formatter:off

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
    static final CssFactory<AppBar> FACTORY = new CssFactory<>(Pane.getClassCssMetaData()) {
        @Override
        protected void registerCss() {
        }
    };
}