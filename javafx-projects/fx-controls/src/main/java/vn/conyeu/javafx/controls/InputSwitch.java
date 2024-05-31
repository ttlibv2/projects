package vn.conyeu.javafx.controls;

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
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.*;
import javafx.collections.ObservableList;

import vn.conyeu.javafx.controls.enums.NodeSize;
import vn.conyeu.javafx.property.SupplierProperty;
import vn.conyeu.javafx.styleable.CssFactory;
import vn.conyeu.javafx.controls.skin.InputSwitchSkin;
import vn.conyeu.javafx.utils.CssUtils;

import java.util.List;
import java.util.function.Supplier;

public class InputSwitch extends Control {

    public InputSwitch() {
        getStyleClass().setAll("switch");
    }

    //@formatter:off

    /** selected */
    private BooleanProperty selected;
    public final boolean isSelected() {return selectedProperty().get();}
    public final void setSelected(boolean value) {selectedProperty().set(value);}
    public final BooleanProperty selectedProperty() {
        if(selected == null) {
            selected = new SimpleBooleanProperty(this, "selected"){
                @Override
                protected void invalidated() {
                    CssUtils.setPseudoSelected(InputSwitch.this, get());
                }
            };
        }
        return selected;
    }
    
    /** content to be shown when the state is selected */
    private SupplierProperty<Label> selectedView;
    public final Supplier<Label> getSelectedView() {return selectedViewProperty().get();}
    public final void setSelectedView(Supplier<Label> value) {selectedViewProperty().set(value);}
    public final SupplierProperty<Label> selectedViewProperty() {
        if(selectedView == null) {
            selectedView = new SupplierProperty<>(this, "selectedView");
        }
        return selectedView;
    }
    
    /** content to be shown when the state is un-selected */
    private SupplierProperty<Label> unSelectedView;
    public final Supplier<Label> getUnSelectedView() {return unSelectedViewProperty().get();}
    public final void setUnSelectedView(Supplier<Label> value) {unSelectedViewProperty().set(value);}
    public final SupplierProperty<Label> unSelectedViewProperty() {
        if(unSelectedView == null) {
            unSelectedView = new SupplierProperty<>(this, "unSelectedView");
        }
        return unSelectedView;
    }

    /** loading state of switch */
    private BooleanProperty loading;
    public final boolean isLoading() {return loadingProperty().get();}
    public final void setLoading(boolean value) {loadingProperty().set(value);}
    public final BooleanProperty loadingProperty() {
        if(loading == null) {
            loading = new SimpleBooleanProperty(this, "loading");
        }
        return loading;
    }

    /** size */
    private StyleableObjectProperty<NodeSize> size;
    public final NodeSize getSize() {return sizeProperty().get();}
    public final void setSize(NodeSize value) {sizeProperty().set(value);}
    public final StyleableObjectProperty<NodeSize> sizeProperty() {
        if(size == null) {
            size = FACTORY.createEnum(this, "size", "-fx-switch-size", NodeSize.class);
        }
        return size;
    }

    /** onSelected */
    private ObjectProperty<EventHandler<InputSwitchEvent>> onSelected;
    public final EventHandler<InputSwitchEvent> getOnSelected() {return onSelectedProperty().get();}
    public final void setOnSelected(EventHandler<InputSwitchEvent> value) {onSelectedProperty().set(value);}
    public final ObjectProperty<EventHandler<InputSwitchEvent>> onSelectedProperty() {
        if(onSelected == null) {
            onSelected = new SimpleObjectProperty<>(this, "onSelected") {
                @Override
                protected void invalidated() {
                    setEventHandler(InputSwitchEvent.SELECTED, get());
                }
            };
        }
        return onSelected;
    }

    //@formatter:on

    @Override
    protected Skin<?> createDefaultSkin() {
        return new InputSwitchSkin(this);
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
    static final CssFactory<InputSwitch> FACTORY = new CssFactory<>(Control.getClassCssMetaData()) {
        @Override
        protected void registerCss() {
            FACTORY.createEnumCss("-fx-switch-size", InputSwitch::sizeProperty, NodeSize.class);

        }
    };

    public void fire() {
      if(!isDisabled()) {
          setSelected(!isSelected());
          fireEvent(new InputSwitchEvent());
      }
    }

    public static class InputSwitchEvent extends Event {
        public static final EventType<InputSwitchEvent> ANY = new EventType<>(Event.ANY, "InputSwitchEventAny");
        public static final EventType<InputSwitchEvent> SELECTED = new EventType<>(ANY, "InputSwitchEventSelected");

        public InputSwitchEvent() {
            super(SELECTED);
        }
    }

}