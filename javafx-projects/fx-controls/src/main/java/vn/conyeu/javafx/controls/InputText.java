package vn.conyeu.javafx.controls;

import javafx.css.*;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Skin;

import javafx.scene.control.Control;
import vn.conyeu.javafx.controls.enums.Status;
import vn.conyeu.javafx.styleable.CssFactory;
import vn.conyeu.javafx.controls.skin.InputTextSkin;

import java.util.List;

public class InputText extends Control {

    public InputText() {
    }

    //@formatter:off

    /** minRow */
    private StyleableIntegerProperty minRow;
    public final void setMinRow(int value) {minRowProperty().set(value);}
    public final int getMinRow() {return minRowProperty().get();}
    public final StyleableIntegerProperty minRowProperty() {
        if(minRow == null) {
            minRow = FACTORY.createInteger(this, "minRow", "-fx-min-row");
        }
        return minRow;
    }

    /** maxRow */
    private StyleableIntegerProperty maxRow;
    public final void setMaxRow(int value) {maxRowProperty().set(value);}
    public final int getMaxRow() {return maxRowProperty().get();}
    public final StyleableIntegerProperty maxRowProperty() {
        if(maxRow == null) {
            maxRow = FACTORY.createInteger(this, "maxRow", "-fx-max-row");
        }
        return maxRow;
    }

    /** autoRow */
    private StyleableBooleanProperty autoRow;
    public final boolean isAutoRow() {return autoRowProperty().get();}
    public final void setAutoRow(boolean value) {autoRowProperty().set(value);}
    public final StyleableBooleanProperty autoRowProperty() {
        if(autoRow == null) {
            autoRow = FACTORY.createBoolean(this, "autoRow", "-fx-auto-row");
        }
        return autoRow;
    }

    /** status */
    private StyleableObjectProperty<Status> status;
    public final Status getStatus() {return statusProperty().get();}
    public final void setStatus(Status value) {statusProperty().set(value);}
    public final StyleableObjectProperty<Status> statusProperty() {
        if(status == null) {
            status = FACTORY.createEnum(this, "status", "-fx-status", Status.class);
        }
        return status;
    }

    /** text */
    private StringProperty text;
    public final String getText() {return textProperty().get();}
    public final void setText(String value) {textProperty().set(value);}
    public final StringProperty textProperty() {
        if(text == null) {
            text = new SimpleStringProperty(this, "text");
        }
        return text;
    }

    //@formatter:on

    @Override
    protected Skin<?> createDefaultSkin() {
        return new InputTextSkin(this);
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
    static final CssFactory<InputText> FACTORY = new CssFactory<>(Control.getClassCssMetaData()) {
        @Override
        protected void registerCss() {
            FACTORY.createSizeCss("-fx-min-row", InputText::minRowProperty, 1);
            FACTORY.createSizeCss("-fx-max-row", InputText::maxRowProperty, -1);
            FACTORY.createBooleanCss("-fx-auto-row", InputText::autoRowProperty, false);
            FACTORY.createEnumCss("-fx-status", InputText::statusProperty, Status.class);
        }
    };

}