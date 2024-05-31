package vn.conyeu.javafx.controls;

import javafx.collections.FXCollections;
import javafx.css.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Skin;
import javafx.collections.ObservableList;

import javafx.scene.control.Control;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import javafx.util.StringConverter;
import vn.conyeu.javafx.animation.beans.DepthLevel;
import vn.conyeu.javafx.controls.virtual.FlowCell;
import vn.conyeu.javafx.property.FunctionProperty;
import vn.conyeu.javafx.styleable.CssFactory;
import vn.conyeu.javafx.controls.skin.FlowViewSkin;

import java.util.List;
import java.util.function.Function;

public abstract class FlowView<T, C extends FlowCell<T>> extends Control {

    public FlowView() {
        setItems(FXCollections.observableArrayList());
    }

    /**
     * Abstract method called automatically to set a default factory for the cells.
     */
    protected abstract void setDefaultCellFactory();

    //@formatter:off

    /**
     * The items list property.
     */
    private ListProperty<T> items;
    public final ObservableList<T> getItems() {return itemsProperty().get();}
    public final void setItems(ObservableList<T> value) {itemsProperty().set(value);}
    public final ListProperty<T> itemsProperty() {
        if(items == null) {
            items = new SimpleListProperty<>(this, "items");
        }
        return items;
    }

    /**  the function used to build the list cells */
    private FunctionProperty<T, C> cellFactory;
    public final Function<T, C> getCellFactory() {return cellFactoryProperty().get();}
    public final void setCellFactory(Function<T, C> value) {cellFactoryProperty().set(value);}
    public final FunctionProperty<T, C> cellFactoryProperty() {
        if(cellFactory == null) {
            cellFactory = new FunctionProperty<>(this, "cellFactory");
        }
        return cellFactory;
    }

    /**
     * Specifies the {@link StringConverter} used to convert a generic
     * item to a String. It is used by the list cells.
     */
    private ObjectProperty<StringConverter<T>> converter;
    public final StringConverter<T> getConverter() {return converterProperty().get();}
    public final void setConverter(StringConverter<T> value) {converterProperty().set(value);}
    public final ObjectProperty<StringConverter<T>> converterProperty() {
        if(converter == null) {
            converter = new SimpleObjectProperty<>(this, "converter");
        }
        return converter;
    }

    /**
     * Specifies the color of the scrollbars' track.
     */
    private StyleableObjectProperty<Paint> trackColor;
    public final Paint getTrackColor() {return trackColorProperty().get();}
    public final void setTrackColor(Paint value) {trackColorProperty().set(value);}
    public final StyleableObjectProperty<Paint> trackColorProperty() {
        if(trackColor == null) {
            trackColor = FACTORY.createPaint(this, "trackColor", "-fx-track-color");
        }
        return trackColor;
    }

    /**
     * Specifies the color of the scrollbars' thumb.
     */
    private StyleableObjectProperty<Paint> thumbColor;
    public final Paint getThumbColor() {return thumbColorProperty().get();}
    public final void setThumbColor(Paint value) {thumbColorProperty().set(value);}
    public final StyleableObjectProperty<Paint> thumbColorProperty() {
        if(thumbColor == null) {
            thumbColor = FACTORY.createPaint(this, "thumbColor", "-fx-thumb-color");
        }
        return thumbColor;
    }

    /**
     * Specifies the color of the scrollbars' thumb when mouse hover.
     */
    private StyleableObjectProperty<Paint> thumbHoverColor;
    public final Paint getThumbHoverColor() {return thumbHoverColorProperty().get();}
    public final void setThumbHoverColor(Paint value) {thumbHoverColorProperty().set(value);}
    public final StyleableObjectProperty<Paint> thumbHoverColorProperty() {
        if(thumbHoverColor == null) {
            thumbHoverColor = FACTORY.createPaint(this, "thumbHoverColor", "-fx-thumb-hover-color");
        }
        return thumbHoverColor;
    }

    /**
     * Specifies the time after which the scrollbars are hidden.
     */
    private StyleableObjectProperty<Duration> hideAfter;
    public final Duration getHideAfter() {return hideAfterProperty().get();}
    public final void setHideAfter(Duration value) {hideAfterProperty().set(value);}
    public final StyleableObjectProperty<Duration> hideAfterProperty() {
        if(hideAfter == null) {
            hideAfter = FACTORY.createDuration(this, "hideAfter", "-fx-hide-after");
        }
        return hideAfter;
    }

    /**
     * Specifies if the scrollbars should be hidden when the mouse is not on the list.
     */
    private StyleableBooleanProperty hideScrollBars;
    public final boolean isHideScrollBars() {return hideScrollBarsProperty().get();}
    public final void setHideScrollBars(boolean value) {hideScrollBarsProperty().set(value);}
    public final StyleableBooleanProperty hideScrollBarsProperty() {
        if(hideScrollBars == null) {
            hideScrollBars = FACTORY.createBoolean(this, "hideScrollBars", "-fx-hide-scroll-bars");
        }
        return hideScrollBars;
    }

    /**
     * Specifies the shadow strength around the control.
     */
    private StyleableObjectProperty<DepthLevel> depthLevel;
    public final DepthLevel getDepthLevel() {return depthLevelProperty().get();}
    public final void setDepthLevel(DepthLevel value) {depthLevelProperty().set(value);}
    public final StyleableObjectProperty<DepthLevel> depthLevelProperty() {
        if(depthLevel == null) {
            depthLevel = FACTORY.createEnum(this, "depthLevel", "-fx-depth-level", DepthLevel.class);
        }
        return depthLevel;
    }

    //@formatter:on

    @Override
    protected Skin<?> createDefaultSkin() {
        return new FlowViewSkin(this);
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
    static final CssFactory<FlowView> FACTORY = new CssFactory<>(Control.getClassCssMetaData()) {
        @Override
        protected void registerCss() {
            FACTORY.createPaintCss("-fx-track-color", FlowView::trackColorProperty);
            FACTORY.createPaintCss("-fx-thumb-color", FlowView::thumbColorProperty);
            FACTORY.createPaintCss("-fx-thumb-hover-color", FlowView::thumbHoverColorProperty);
            FACTORY.createDurationCss("-fx-hide-after", FlowView::hideAfterProperty);
            FACTORY.createBooleanCss("-fx-hide-scroll-bars", FlowView::hideScrollBarsProperty, false);
            FACTORY.createEnumCss("-fx-depth-level", FlowView::depthLevelProperty, DepthLevel.class);


        }
    };

}