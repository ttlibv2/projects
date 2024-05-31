package vn.conyeu.javafx.controls;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.*;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import vn.conyeu.javafx.controls.skin.SpeedDialSkin;
import vn.conyeu.javafx.property.FunctionProperty;
import vn.conyeu.javafx.property.SupplierProperty;
import vn.conyeu.javafx.styleable.CssFactory;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class SpeedDial extends Control {

    public SpeedDial() {
       this(FXCollections.observableArrayList());
    }

    public SpeedDial(DialItem...items) {
        this(FXCollections.observableArrayList(items));
    }

    public SpeedDial(ObservableList<DialItem> items) {
        getStyleClass().setAll("speed-dial");
        setItems(items == null ? FXCollections.observableArrayList(): items);
    }

    //@formatter:off

    /** Specifies the opening direction of actions. */
    private StyleableObjectProperty<Direction> direction;
    public final Direction getDirection() {return directionProperty().get();}
    public final void setDirection(Direction value) {directionProperty().set(value);}
    public final StyleableObjectProperty<Direction> directionProperty() {
        if(direction == null) {
            direction = FACTORY.createEnum(this, "direction", "-fx-dial-direction", Direction.class);
        }
        return direction;
    }

    /** Specifies the opening type of actions. */
    private StyleableObjectProperty<DialType> dialType;
    public final DialType getDialType() {return dialTypeProperty().get();}
    public final void setDialType(DialType value) {dialTypeProperty().set(value);}
    public final StyleableObjectProperty<DialType> dialTypeProperty() {
        if(dialType == null) {
            dialType = FACTORY.createEnum(this, "dialType", "-fx-dial-type", DialType.class);
        }
        return dialType;
    }
    /** Transition delay step for each action item. */
    private StyleableIntegerProperty transitionDelay;
    public final void setTransitionDelay(int value) {transitionDelayProperty().set(value);}
    public final int getTransitionDelay() {return transitionDelayProperty().get();}
    public final StyleableIntegerProperty transitionDelayProperty() {
        if(transitionDelay == null) {
            transitionDelay = FACTORY.createInteger(this, "transitionDelay", "-fx-transition-delay");
        }
        return transitionDelay;
    }

    /** Radius for *circle types. */
    private StyleableDoubleProperty circleRadius;
    public final void setCircleRadius(double value) {circleRadiusProperty().set(value);}
    public final double getCircleRadius() {return circleRadiusProperty().get();}
    public final StyleableDoubleProperty circleRadiusProperty() {
        if(circleRadius == null) {
            circleRadius = FACTORY.createDouble(this, "circleRadius", "-fx-circle-radius");
        }
        return circleRadius;
    }
    
    /** Whether to show a mask element behind the speeddial. */
    private StyleableBooleanProperty mask;
    public final boolean isMask() {return maskProperty().get();}
    public final void setMask(boolean value) {maskProperty().set(value);}
    public final StyleableBooleanProperty maskProperty() {
        if(mask == null) {
            mask = FACTORY.createBoolean(this, "mask", "-fx-dial-mask");
        }
        return mask;
    }

    /** Whether the actions close when clicked outside. */
    private StyleableBooleanProperty hideOnClickOutside;
    public final boolean isHideOnClickOutside() {return hideOnClickOutsideProperty().get();}
    public final void setHideOnClickOutside(boolean value) {hideOnClickOutsideProperty().set(value);}
    public final StyleableBooleanProperty hideOnClickOutsideProperty() {
        if(hideOnClickOutside == null) {
            hideOnClickOutside = FACTORY.createBoolean(this, "hideOnClickOutside", "-fx-hide-onclick-outside");
        }
        return hideOnClickOutside;
    }

    /** items */
    private ListProperty<DialItem> items;
    public final ObservableList<DialItem> getItems() {return itemsProperty().get();}
    public final void setItems(ObservableList<DialItem> value) {itemsProperty().set(value);}
    public final ListProperty<DialItem> itemsProperty() {
        if(items == null) {
            items = new SimpleListProperty<>(this, "items");
        }
        return items;
    }

    /** buttonSuppiler */
    private SupplierProperty<Node> buttonSuppiler;
    public final Supplier<Node> getButtonSuppiler() {return buttonSuppilerProperty().get();}
    public final void setButtonSuppiler(Supplier<Node> value) {buttonSuppilerProperty().set(value);}
    public final SupplierProperty<Node> buttonSuppilerProperty() {
        if(buttonSuppiler == null) {
            buttonSuppiler = new SupplierProperty<>(this, "buttonSuppiler");
        }
        return buttonSuppiler;
    }

    /** itemFactory */
    private FunctionProperty<DialItem, Node> itemFactory;
    public final Function<DialItem, Node> getItemFactory() {return itemFactoryProperty().get();}
    public final void setItemFactory(Function<DialItem, Node> value) {itemFactoryProperty().set(value);}
    public final FunctionProperty<DialItem, Node> itemFactoryProperty() {
        if(itemFactory == null) {
            itemFactory = new FunctionProperty<>(this, "itemFactory");
        }
        return itemFactory;
    }

    /** onButtonClick */
    private ObjectProperty<EventHandler<DialEvent>> onButtonClick;
    public final EventHandler<DialEvent> getOnButtonClick() {return onButtonClickProperty().get();}
    public final void setOnButtonClick(EventHandler<DialEvent> value) {onButtonClickProperty().set(value);}
    public final ObjectProperty<EventHandler<DialEvent>> onButtonClickProperty() {
        if(onButtonClick == null) {
            onButtonClick = new SimpleObjectProperty<>(this, "onButtonClick") {
                @Override
                protected void invalidated() {
                    setEventHandler(DialEvent.BTN_CLICK, get());
                }
            };
        }
        return onButtonClick;
    }

    /** onShow */
    private ObjectProperty<EventHandler<DialEvent>> onShow;
    public final EventHandler<DialEvent> getOnShow() {return onShowProperty().get();}
    public final void setOnShow(EventHandler<DialEvent> value) {onShowProperty().set(value);}
    public final ObjectProperty<EventHandler<DialEvent>> onShowProperty() {
        if(onShow == null) {
            onShow = new SimpleObjectProperty<>(this, "onShow") {
                @Override
                protected void invalidated() {
                    setEventHandler(DialEvent.SHOW, get());
                }
            };
        }
        return onShow;
    }

    /** onHide */
    private ObjectProperty<EventHandler<DialEvent>> onHide;
    public final EventHandler<DialEvent> getOnHide() {return onHideProperty().get();}
    public final void setOnHide(EventHandler<DialEvent> value) {onHideProperty().set(value);}
    public final ObjectProperty<EventHandler<DialEvent>> onHideProperty() {
        if(onHide == null) {
            onHide = new SimpleObjectProperty<>(this, "onHide") {
                @Override
                protected void invalidated() {
                    setEventHandler(DialEvent.HIDE, get());
                }
            };
        }
        return onHide;
    }

    //@formatter:on

    @Override
    protected Skin<?> createDefaultSkin() {
        return new SpeedDialSkin(this);
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
    static final CssFactory<SpeedDial> FACTORY = new CssFactory<>(Control.getClassCssMetaData()) {
        @Override
        protected void registerCss() {
            FACTORY.createEnumCss("-fx-dial-direction", SpeedDial::directionProperty, Direction.class);
            FACTORY.createSizeCss("-fx-transition-delay", SpeedDial::transitionDelayProperty);
            FACTORY.createEnumCss("-fx-dial-type", SpeedDial::dialTypeProperty, DialType.class);
            FACTORY.createSizeCss("-fx-circle-radius", SpeedDial::circleRadiusProperty);
            FACTORY.createBooleanCss("-fx-dial-mask", SpeedDial::maskProperty);
            FACTORY.createBooleanCss("-fx-hide-onclick-outside", SpeedDial::hideOnClickOutsideProperty);

        }
    };

    public enum Direction {
        up, down, left, right, up_left, up_right, down_left, down_right
    }

    public enum DialType {
        linear, circle, semi_circle, quarter_circle
    }

    public static class DialEvent extends Event {
        public static final EventType<DialEvent> ANY = new EventType<>(Event.ANY, "DialEventAny");
        public static final EventType<DialEvent> BTN_CLICK = new EventType<>(ANY, "DialEventBtnClick");
        public static final EventType<DialEvent> SHOW = new EventType<>(ANY, "DialEventShow");
        public static final EventType<DialEvent> HIDE = new EventType<>(ANY, "DialEventHide");

        public DialEvent(EventType<? extends DialEvent> eventType) {
            super(eventType);
        }
    }

    public static class DialItem {

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

        /** graphic */
        private ObjectProperty<Node> graphic;
        public final Node getGraphic() {return graphicProperty().get();}
        public final void setGraphic(Node value) {graphicProperty().set(value);}
        public final ObjectProperty<Node> graphicProperty() {
            if(graphic == null) {
                graphic = new SimpleObjectProperty<>(this, "graphic");
            }
            return graphic;
        }

        //@formatter:on

    }
}