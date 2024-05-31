package vn.conyeu.javafx.controls;

import javafx.collections.FXCollections;
import javafx.css.*;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.geometry.Orientation;
import javafx.scene.control.Skin;
import javafx.collections.ObservableList;

import javafx.scene.control.Control;
import javafx.scene.layout.Region;
import vn.conyeu.javafx.controls.simple.MeterItem;
import vn.conyeu.javafx.property.FunctionProperty;
import vn.conyeu.javafx.styleable.CssFactory;
import vn.conyeu.javafx.controls.skin.MeterGroupSkin;
import vn.conyeu.javafx.utils.CssUtils;

import java.util.List;
import java.util.function.Function;

public class MeterGroup extends Control {

    public MeterGroup() {
        getStyleClass().setAll("meter-group");
        setItems(FXCollections.observableArrayList());
    }

    public MeterItem addItem(String label, String color, double value) {
        MeterItem meterItem = new MeterItem(label, color, value);
        getItems().add(meterItem);
        return meterItem;
    }

    //@formatter:off

    /** items */
    private ListProperty<MeterItem> items;
    public final ObservableList<MeterItem> getItems() {return itemsProperty().get();}
    public final void setItems(ObservableList<MeterItem> value) {itemsProperty().set(value);}
    public final ListProperty<MeterItem> itemsProperty() {
        if(items == null) {
            items = new SimpleListProperty<>(this, "items");
        }
        return items;
    }

    /** Specifies the label position of the component, valid values are 'start' and 'end'. */
    private StyleableObjectProperty<LabelPos> labelPos;
    public final LabelPos getLabelPos() {return labelPosProperty().get();}
    public final void setLabelPos(LabelPos value) {labelPosProperty().set(value);}
    public final StyleableObjectProperty<LabelPos> labelPosProperty() {
        if(labelPos == null) {
            labelPos = FACTORY.createEnum(this, "labelPos", "-fx-label-pos", LabelPos.class);
        }
        return labelPos;
    }

    /** Specifies the label orientation of the component, valid values are 'horizontal' and 'vertical'. */
    private StyleableObjectProperty<Orientation> labelOrientation;
    public final Orientation getLabelOrientation() {return labelOrientationProperty().get();}
    public final void setLabelOrientation(Orientation value) {labelOrientationProperty().set(value);}
    public final StyleableObjectProperty<Orientation> labelOrientationProperty() {
        if(labelOrientation == null) {
            labelOrientation = FACTORY.createEnum(this, "labelOrientation", "-fx-label-orientation");
        }
        return labelOrientation;
    }

    /** orientation */
    private StyleableObjectProperty<Orientation> orientation;
    public final Orientation getOrientation() {return orientationProperty().get();}
    public final void setOrientation(Orientation value) {orientationProperty().set(value);}
    public final StyleableObjectProperty<Orientation> orientationProperty() {
        if(orientation == null) {
            orientation = FACTORY.createEnum(this, "orientation", "-fx-orientation", Orientation.class,
                    (o, v) -> CssUtils.setPseudo(this, "", o, v));
        }
        return orientation;
    }

    /** templating support for labels, meter items, and content around the meters. */
    private FunctionProperty<MeterItem, Region> labelFactory;
    public final Function<MeterItem, Region> getLabelFactory() {return labelFactoryProperty().get();}
    public final void setLabelFactory(Function<MeterItem, Region> value) {labelFactoryProperty().set(value);}
    public final FunctionProperty<MeterItem, Region> labelFactoryProperty() {
        if(labelFactory == null) {
            labelFactory = new FunctionProperty<>(this, "labelFactory");
        }
        return labelFactory;
    }

    /** Mininum boundary value. */
    private StyleableDoubleProperty min;
    public final void setMin(double value) {minProperty().set(value);}
    public final double getMin() {return minProperty().get();}
    public final StyleableDoubleProperty minProperty() {
        if(min == null) {
            min = FACTORY.createDouble(this, "min", "-fx-min");
        }
        return min;
    }

    /** Maximum boundary value. */
    private StyleableDoubleProperty max;
    public final void setMax(double value) {maxProperty().set(value);}
    public final double getMax() {return maxProperty().get();}
    public final StyleableDoubleProperty maxProperty() {
        if(max == null) {
            max = FACTORY.createDouble(this, "max", "-fx-max");
        }
        return max;
    }

    //@formatter:on

    @Override
    protected Skin<?> createDefaultSkin() {
        return new MeterGroupSkin(this);
    }

    /**
     * {@inheritDoc}
     */
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
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
    static final CssFactory<MeterGroup> FACTORY = new CssFactory<>(Control.getClassCssMetaData()) {
        @Override
        protected void registerCss() {
            FACTORY.createEnumCss("-fx-label-pos", MeterGroup::labelPosProperty, LabelPos.class, LabelPos.END);
            FACTORY.createEnumCss("-fx-label-orientation", MeterGroup::labelOrientationProperty, Orientation.class, Orientation.HORIZONTAL);
            FACTORY.createEnumCss("-fx-orientation", MeterGroup::orientationProperty, Orientation.class, Orientation.HORIZONTAL);
            FACTORY.createSizeCss("-fx-min", MeterGroup::minProperty, 0);
            FACTORY.createSizeCss("-fx-max", MeterGroup::maxProperty, 100);

        }
    };


    /**
     * Specifies the label position of the component, valid values are 'start' and 'end'.
     * */
    public enum LabelPos { START, END}

}