package vn.conyeu.javafx.controls;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.*;
import javafx.geometry.Orientation;
import javafx.scene.control.Skin;

import javafx.scene.control.Control;
import vn.conyeu.javafx.beans.IntegerRange;
import vn.conyeu.javafx.controls.virtual.FlowCell;
import vn.conyeu.javafx.controls.virtual.FlowManager;
import vn.conyeu.javafx.controls.virtual.FlowState;
import vn.conyeu.javafx.controls.virtual.OrientationHelper;
import vn.conyeu.javafx.property.FunctionProperty;
import vn.conyeu.javafx.styleable.CssFactory;
import vn.conyeu.javafx.controls.skin.VirtualFlowSkin;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class VirtualFlow<T, C extends FlowCell<T>> extends Control {
    private final FlowManager<T, C> manager = new FlowManager<>(this);

    public VirtualFlow() {
        this(FXCollections.observableArrayList(), null);
    }

    public VirtualFlow(ObservableList<T> items) {
       this(items, null);
    }

    public VirtualFlow(ObservableList<T> items, Function<T, C> cellFactory) {
        getStyleClass().setAll("virtual-flow");
        setItems(items == null? FXCollections.observableArrayList():items);
        setOrientation(Orientation.VERTICAL);
        setCellFactory(cellFactory);
    }

    /**
     * Basically a setter for {@link #needsViewportLayoutProperty()}.
     * This sets the property to true causing the virtual flow skin to catch the change through a listener
     * which then orders the viewport container to re-compute the layout.
     */
    public void requestViewportLayout() {
        needsViewportLayout.set(true);
    }

    public void scrollBy(double pixels) {
        OrientationHelper helper = getOrientationHelper();
        if (helper != null) helper.scrollBy(pixels);
    }

    /**
     * Scroll to the given pixel.
     */
    public void scrollToPixel(double pixel) {
        OrientationHelper helper = getOrientationHelper();
        if (helper != null) helper.scrollToPixel(pixel);
    }

    /**
     * S scroll to the given cell index.
     */
    public void scrollToIndex(int index) {
        OrientationHelper helper = getOrientationHelper();
        if (helper != null) helper.scrollToIndex(index);
    }

    /**
     * Scroll to the first cell.
     */
    public void scrollToFirst() {
        scrollToIndex(0);
    }

    /**
     * Tells the current {@link #orientationHelperProperty()} to scroll
     * to the last cell.
     */
    public void scrollToLast() {
        scrollToIndex(getItems().size() - 1);
    }

    protected void cellSizeChanged() {
        OrientationHelper helper = getOrientationHelper();
        helper.computeEstimatedLength();

        if (getWidth() != 0.0 && getHeight() != 0.0) { // TODO test with w and h = 0 initially
            manager.init();
            scrollToPixel(0);
        }
    }

    //@formatter:off

    /**
     * Specifies the {@link ObservableList} used to store the items.
     * <p>
     * This is an {@link ObjectProperty} so that it can also be bound to other properties.
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

    /**
     * Specifies the function used to build the cells.
     */
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
     * Specifies the vertical position in the viewport.
     */
    private DoubleProperty vPos;
    public final double getVPos() {return vPosProperty().get();}
    public final void setVPos(double value) {vPosProperty().set(value);}
    public final DoubleProperty vPosProperty() {
        if(vPos == null) {
            vPos = new SimpleDoubleProperty(this, "vPos", 0.0);
        }
        return vPos;
    }

    /**
     * Specifies the horizontal position in the viewport.
     */
    private DoubleProperty hPos;
    public final double getHPos() {return hPosProperty().get();}
    public final void setHPos(double value) {hPosProperty().set(value);}
    public final DoubleProperty hPosProperty() {
        if(hPos == null) {
            hPos = new SimpleDoubleProperty(this, "hPos", 0.0);
        }
        return hPos;
    }

    /**
     * Specifies the orientation of the virtual flow.
     * <p>
     * It is also possible to set this property via CSS with the {@code "-fx-orientation"} property.
     */
    private StyleableObjectProperty<Orientation> orientation;
    public final Orientation getOrientation() {return orientationProperty().get();}
    public final void setOrientation(Orientation value) {orientationProperty().set(value);}
    public final StyleableObjectProperty<Orientation> orientationProperty() {
        if(orientation == null) {
            orientation = FACTORY.createEnum(this, "orientation", "-fx-orientation",
                    Orientation.class, (o, n) -> {
                        Orientation orientation = getOrientation();
                        OrientationHelper helper = getOrientationFactory().apply(orientation);
                        setOrientationHelper(helper);
                        manager.reset();
                    });
        }
        return orientation;
    }

    /**
     * The current built helper for the current {@link #orientationProperty()},
     * see {@link OrientationHelper}.
     */
    private ReadOnlyObjectWrapper<OrientationHelper> orientationHelper;
    public final OrientationHelper getOrientationHelper() {return orientationHelperPropertyImpl().get();}
    public final ReadOnlyObjectProperty<OrientationHelper> orientationHelperProperty() {return orientationHelperPropertyImpl().getReadOnlyProperty();}
    protected final void setOrientationHelper(OrientationHelper orientationHelper) {orientationHelperPropertyImpl().set(orientationHelper);}
    protected final ReadOnlyObjectWrapper<OrientationHelper> orientationHelperPropertyImpl() {
        if(orientationHelper == null) {
            orientationHelper = new ReadOnlyObjectWrapper<>(this, "orientationHelper"){
                @Override
                public void set(OrientationHelper newValue) {
                    if(get() != null)get().dispose();
                    super.set(newValue);
                }
            };
        }
        return orientationHelper;
    }

    /**
     * To allow more customization on how the cells are laid out, the virtual flow allows you
     * to specify a function used to build a {@link OrientationHelper} which is responsible for some core actions
     * about cells and layout. This way you could implement your own helper and set it through this factory since the
     * {@link #orientationHelperProperty()} is intended to be read-only.
     */
    private FunctionProperty<Orientation, OrientationHelper> orientationFactory;
    public final Function<Orientation, OrientationHelper> getOrientationFactory() {return orientationFactoryProperty().get();}
    public final void setOrientationFactory(Function<Orientation, OrientationHelper> value) {orientationFactoryProperty().set(value);}
    public final FunctionProperty<Orientation, OrientationHelper> orientationFactoryProperty() {
        if(orientationFactory == null) {
            orientationFactory = new FunctionProperty<>(this, "orientationFactory",
                    ori -> OrientationHelper.valueOf(this, ori)){

                @Override
                protected void invalidated() {
                    Orientation orientation = getOrientation();
                    OrientationHelper helper = get().apply(orientation);
                    setOrientationHelper(helper);
                }
            };
        }
        return orientationFactory;
    }

    /**
     * Specifies the total length of all the cells.
     */
    private DoubleProperty estimatedLength;
    public final double getEstimatedLength() {return estimatedLengthProperty().get();}
    public final void setEstimatedLength(double value) {estimatedLengthProperty().set(value);}
    public final DoubleProperty estimatedLengthProperty() {
        if(estimatedLength == null) {
            estimatedLength = new SimpleDoubleProperty(this, "estimatedLength");
        }
        return estimatedLength;
    }

    /**
     * Specifies the max breadth among the currently visible cells.
     */
    private DoubleProperty maxBreadth;
    public final double getMaxBreadth() {return maxBreadthProperty().get();}
    public final void setMaxBreadth(double value) {maxBreadthProperty().set(value);}
    public final DoubleProperty maxBreadthProperty() {
        if(maxBreadth == null) {
            maxBreadth = new SimpleDoubleProperty(this, "maxBreadth");
        }
        return maxBreadth;
    }

    /**
     * Specifies whether the viewport needs to compute the layout of its content.
     * <p>
     * Since this is read-only, layout requests must be sent by using {@link #requestViewportLayout()}.
     */
    private final ReadOnlyBooleanWrapper needsViewportLayout = new ReadOnlyBooleanWrapper(false);
    public final boolean isNeedsViewportLayout() {return needsViewportLayout.get();}
    public final void setNeedsViewportLayout(boolean flag) {needsViewportLayout.set(flag);}
    public final ReadOnlyBooleanProperty needsViewportLayoutProperty() {return needsViewportLayout.getReadOnlyProperty();}

    /**
     * Specifies whether cells should be resized to be the same size of the viewport in the opposite
     * direction of the current {@link #orientationProperty()}.
     * <p>
     * It is also possible to set this property via CSS with the {@code "-fx-fit-to-breadth"} property.
     */
    private StyleableBooleanProperty fitToBreadth;
    public final boolean isFitToBreadth() {return fitToBreadthProperty().get();}
    public final void setFitToBreadth(boolean value) {fitToBreadthProperty().set(value);}
    public final StyleableBooleanProperty fitToBreadthProperty() {
        if(fitToBreadth == null) {
            CssMetaData<VirtualFlow, Boolean> cssMetaData = FACTORY.getCssMetaData("-fx-fit-to-breadth");
            fitToBreadth = new SimpleStyleableBooleanProperty(cssMetaData, this, "fitToBreadth", true){
                @Override
                protected void invalidated() {
                    OrientationHelper helper = getOrientationHelper();
                    if (helper != null) {
                        helper.invalidatePos();
                        requestViewportLayout();
                    }
                }
            };
        }
        return fitToBreadth;
    }

    /**
     * Specifies the cells' size:
     * <p> - Orientation.VERTICAL:   size -> height
     * <p> - Orientation.HORIZONTAL: size -> width
     * <p>
     * It is also possible to set this property via CSS with the {@code "-fx-cell-size"} property.
     */
    private StyleableDoubleProperty cellSize;
    public final void setCellSize(double value) {cellSizeProperty().set(value);}
    public final double getCellSize() {return cellSizeProperty().get();}
    public final StyleableDoubleProperty cellSizeProperty() {
        if(cellSize == null) {
            cellSize = FACTORY.createDouble(this, "cellSize", "-fx-cell-size", (o, size) -> cellSizeChanged());
        }
        return cellSize;
    }

    /**
     * Used by the viewport's clip to set its border radius.
     * This is useful when you want to make a rounded virtual flow, this
     * prevents the content from going outside the view.
     * <p></p>
     * This is mostly useful if not using the flow with {@code VirtualScrollPane}, this is the same as
     * {@code VirtualScrollPane#clipBorderRadiusProperty()}.
     * <p></p>
     * <b>Side note:</b> the clip is a {@code Rectangle}, now for some
     * fucking reason the rectangle's arcWidth and arcHeight values used to make
     * it round do not act like the border-radius or background-radius properties,
     * instead their value is usually 2 / 2.5 times the latter.
     * So for a border radius of 5 you want this value to be at least 10/13.
     * <p>
     * It is also possible to set this property via CSS with the {@code "-fx-clip-border-radius"} property.
     */
    private StyleableDoubleProperty clipBorderRadius;
    public final void setClipBorderRadius(double value) {clipBorderRadiusProperty().set(value);}
    public final double getClipBorderRadius() {return clipBorderRadiusProperty().get();}
    public final StyleableDoubleProperty clipBorderRadiusProperty() {
        if(clipBorderRadius == null) {
            clipBorderRadius = FACTORY.createDouble(this, "clipBorderRadius", "-fx-clip-border-radius");
        }
        return clipBorderRadius;
    }

    /**
     * Specifies the current {@link FlowState} which describes the state of the viewport. This property is useful
     * to listen to any change happening in the viewport.
     */
    public final FlowState<T, C> getState() {return stateProperty().get();}
    public final void setState(FlowState<T, C> value) {stateProperty().set(value);}
    public final ObjectProperty<FlowState<T, C>> stateProperty() {
        return manager.stateProperty();
    }

    /**
     * Specifies the last range of displayed items by the viewport as an {@link IntegerRange}.
     */
    public final IntegerRange getLastRange() {return lastRangeProperty().get();}
    public final void setLastRange(IntegerRange value) {lastRangeProperty().set(value);}
    public final ObjectProperty<IntegerRange> lastRangeProperty() {
        return manager.lastRangeProperty();
    }

    //@formatter:on

    /**
     * Delegate method for {@code FlowState#getCells()}.
     */
    public Map<Integer, C> getIndexedCells() {
        return manager.getState().getCells();
    }

    public FlowManager<T, C> getViewportManager() {
        return manager;
    }


    @Override
    protected Skin<?> createDefaultSkin() {
        return new VirtualFlowSkin(this);
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
    static final CssFactory<VirtualFlow> FACTORY = new CssFactory<>(Control.getClassCssMetaData()) {
        @Override
        protected void registerCss() {
            FACTORY.createEnumCss("-fx-orientation", VirtualFlow::orientationProperty, Orientation.class, Orientation.VERTICAL);
            FACTORY.createBooleanCss("-fx-fit-to-breadth", VirtualFlow::fitToBreadthProperty, true);
            FACTORY.createSizeCss("-fx-cell-size", VirtualFlow::cellSizeProperty, 32);
            FACTORY.createSizeCss("-fx-clip-border-radius", VirtualFlow::clipBorderRadiusProperty, 0);

        }
    };

}