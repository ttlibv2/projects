package vn.conyeu.javafx.controls.simple;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.Serializable;

public class MeterItem implements Serializable {
    private final ObservableList<String> styleClass = FXCollections.observableArrayList();

    public MeterItem(String label, String color, double value) {
        getStyleClass().setAll("meter-item");
        setLabel(label);
        setColor(Color.valueOf(color));
        setValue(value);
    }

    public final MeterItem label(String value) {
        setLabel(value);
        return this;
    }

    public final MeterItem color(Paint value) {
        setColor(value);
        return this;
    }

    public final MeterItem iconLiteral(String value) {
        setIconLiteral(value);
        return this;
    }

    public final MeterItem value(double value) {
        setValue(value);
        return this;
    }

    public final MeterItem visibleDot(boolean value) {
        setVisibleDot(value);
        return this;
    }

    public final MeterItem styleClass(String... styleClass) {
        getStyleClass().setAll(styleClass);
        return this;
    }

//@formatter:off

    /** label */
    private StringProperty label;
    public final String getLabel() {return labelProperty().get();}
    public final void setLabel(String value) {labelProperty().set(value);}
    public final StringProperty labelProperty() {
        if(label == null) {
            label = new SimpleStringProperty(this, "label");
        }
        return label;
    }

    /** color */
    private ObjectProperty<Paint> color;
    public final Paint getColor() {return colorProperty().get();}
    public final void setColor(Paint value) {colorProperty().set(value);}
    public final ObjectProperty<Paint> colorProperty() {
        if(color == null) {
            color = new SimpleObjectProperty<>(this, "color");
        }
        return color;
    }

    /** iconLiteral */
    private StringProperty iconLiteral;
    public final String getIconLiteral() {return iconLiteralProperty().get();}
    public final void setIconLiteral(String value) {iconLiteralProperty().set(value);}
    public final StringProperty iconLiteralProperty() {
        if(iconLiteral == null) {
            iconLiteral = new SimpleStringProperty(this, "iconLiteral");
        }
        return iconLiteral;
    }

    /** icon */
    private ObjectProperty<Node> icon;
    public final Node getIcon() {return iconProperty().get();}
    public final void setIcon(Node value) {iconProperty().set(value);}
    public final ObjectProperty<Node> iconProperty() {
        if(icon == null) {
            icon = new SimpleObjectProperty<>(this, "icon");
        }
        return icon;
    }

    /** value */
    private DoubleProperty value;
    public final double getValue() {return valueProperty().get();}
    public final void setValue(double value) {valueProperty().set(value);}
    public final DoubleProperty valueProperty() {
        if(value == null) {
            value = new SimpleDoubleProperty(this, "value");
        }
        return value;
    }

    /** visibleDot */
    private BooleanProperty visibleDot;
    public final boolean isVisibleDot() {return visibleDotProperty().get();}
    public final void setVisibleDot(boolean value) {visibleDotProperty().set(value);}
    public final BooleanProperty visibleDotProperty() {
        if(visibleDot == null) {
            visibleDot = new SimpleBooleanProperty(this, "visibleDot", true);
        }
        return visibleDot;
    }

    /** id */
    private StringProperty id;
    public final String getId() {return idProperty().get();}
    public final void setId(String value) {idProperty().set(value);}
    public final StringProperty idProperty() {
        if(id == null) {
            id = new SimpleStringProperty(this, "id");
        }
        return id;
    }

    /** styleClass */
    public final ObservableList<String> getStyleClass() { return styleClass; }

    //@formatter:on

    public interface MeterItemNode {
        void dispose();
    }


}