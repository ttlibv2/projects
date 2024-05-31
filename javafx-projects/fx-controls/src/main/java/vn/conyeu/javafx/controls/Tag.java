package vn.conyeu.javafx.controls;

import javafx.css.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.Skin;

import javafx.scene.control.Control;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import javafx.util.StringConverter;
import vn.conyeu.javafx.controls.enums.Status;
import vn.conyeu.javafx.styleable.CssFactory;
import vn.conyeu.javafx.controls.skin.TagSkin;
import vn.conyeu.javafx.utils.Colors;
import vn.conyeu.javafx.utils.Converters;
import vn.conyeu.javafx.utils.CssUtils;

import java.util.List;

public class Tag<T> extends Control {

    public Tag() {
        getStyleClass().setAll("tag");
        setConverter(Converters.defaultConverter());
    }

    //@formatter:off

    /** value */
    private ObjectProperty<T> value;
    public final T getValue() {return valueProperty().get();}
    public final void setValue(T value) {valueProperty().set(value);}
    public final ObjectProperty<T> valueProperty() {
        if(value == null) {
            value = new SimpleObjectProperty<>(this, "value");
        }
        return value;
    }

    /** converter */
    private ObjectProperty<StringConverter<T>> converter;
    public final StringConverter<T> getConverter() {return converterProperty().get();}
    public final void setConverter(StringConverter<T> value) {converterProperty().set(value);}
    public final ObjectProperty<StringConverter<T>> converterProperty() {
        if(converter == null) {
            converter = new SimpleObjectProperty<>(this, "converter");
        }
        return converter;
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

    /** checked */
    private BooleanProperty checked;
    public final boolean isChecked() {return checkedProperty().get();}
    public final void setChecked(boolean value) {checkedProperty().set(value);}
    public final BooleanProperty checkedProperty() {
        if(checked == null) {
            checked = new SimpleBooleanProperty(this, "checked"){
                @Override
                protected void invalidated() {
                    updatePseudoClassChecked();
                }
            };
        }
        return checked;
    }

    private void updatePseudoClassChecked() {
        CssUtils.setPseudo(this, "checked", TagMode.CHECKED == getMode() && isChecked());
    }

    /** mode */
    private StyleableObjectProperty<TagMode> mode;
    public final TagMode getMode() {return modeProperty().get();}
    public final void setMode(TagMode value) {modeProperty().set(value);}
    public final void setVisibleClose() { setMode(TagMode.CLOSE);}
    public final StyleableObjectProperty<TagMode> modeProperty() {
        if(mode == null) {
            mode = FACTORY.createEnum(this, "mode", "-fx-tag-mode", TagMode.class, (o, mode) -> {
                CssUtils.setPseudo(this, "has-", o, mode);
                updatePseudoClassChecked();
            });
        }
        return mode;
    }

    /** status */
    private StyleableObjectProperty<Status> status;
    public final Status getStatus() {return statusProperty().get();}
    public final void setStatus(Status value) {statusProperty().set(value);}
    public final StyleableObjectProperty<Status> statusProperty() {
        if(status == null) {
            status = FACTORY.createEnum(this, "status", "-fx-tag-status", Status.class,
                    (o, status) -> CssUtils.registerPseudoClass(this, Status.class, status));
        }
        return status;
    }

    /** color */
    private StyleableObjectProperty<Paint> color;
    public final Paint getColor() {return colorProperty().get();}
    public final void setColor(Paint value) {colorProperty().set(value);}
    public final StyleableObjectProperty<Paint> colorProperty() {
        if(color == null) {
            CssMetaData<Tag, Paint> cssMetaData = FACTORY.getCssMetaData("-fx-tag-color");
            color = new SimpleStyleableObjectProperty<>(cssMetaData, this, "color");
        }
        return color;
    }

    /** textFill */
    private StyleableObjectProperty<Paint> textFill;
    public final Paint getTextFill() {return textFillProperty().get();}
    public final void setTextFill(Paint value) {textFillProperty().set(value);}
    public final StyleableObjectProperty<Paint> textFillProperty() {
        if(textFill == null) {
            textFill = FACTORY.createPaint(this, "textFill", "-fx-tag-text-fill");
        }
        return textFill;
    }

    /** closeFactory */
    private ObjectProperty<Callback<String, Node>> closeFactory;
    public final Callback<String, Node> getCloseFactory() {return closeFactoryProperty().get();}
    public final void setCloseFactory(Callback<String, Node> value) {
        closeFactoryProperty().set(value);}
    public final ObjectProperty<Callback<String, Node>> closeFactoryProperty() {
        if(closeFactory == null) {
            closeFactory = new SimpleObjectProperty<>(this, "closeFactory");
        }
        return closeFactory;
    }

    /** closeIcon */
    private StyleableStringProperty closeIcon;
    public final String getCloseIcon() {return closeIconProperty().get();}
    public final void setCloseIcon(String value) {closeIconProperty().set(value);}
    public final StyleableStringProperty closeIconProperty() {
        if(closeIcon == null) {
            closeIcon = FACTORY.createString(this, "closeIcon", "-fx-close-icon");
        }
        return closeIcon;
    }

    /** visibleClose */
    private StyleableBooleanProperty visibleClose;
    public final boolean isVisibleClose() {return visibleCloseProperty().get();}
    public final void setVisibleClose(boolean value) {visibleCloseProperty().set(value);}
    public final StyleableBooleanProperty visibleCloseProperty() {
        if(visibleClose == null) {
            visibleClose = FACTORY.createBoolean(this, "visibleClose", "-fx-visible-close");
        }
        return visibleClose;
    }

    /** closeGap */
    private StyleableDoubleProperty closeGap;
    public final void setCloseGap(double value) {closeGapProperty().set(value);}
    public final double getCloseGap() {return closeGapProperty().get();}
    public final StyleableDoubleProperty closeGapProperty() {
        if(closeGap == null) {
            closeGap = FACTORY.createDouble(this, "closeGap", "-fx-close-gap");
        }
        return closeGap;
    }
    /** onClose */
    private ObjectProperty<EventHandler<TagEvent>> onClose;
    public final EventHandler<TagEvent> getOnClose() {return onCloseProperty().get();}
    public final void setOnClose(EventHandler<TagEvent> value) {onCloseProperty().set(value);}
    public final ObjectProperty<EventHandler<TagEvent>> onCloseProperty() {
        if(onClose == null) {
            onClose = new SimpleObjectProperty<>(this, "onClose") {
                @Override
                protected void invalidated() {
                    setEventHandler(TagEvent.CLOSE, get());
                }
            };
        }
        return onClose;
    }

    /** onChecked */
    private ObjectProperty<EventHandler<CheckedEvent>> onChecked;
    public final EventHandler<CheckedEvent> getOnChecked() {return onCheckedProperty().get();}
    public final void setOnChecked(EventHandler<CheckedEvent> value) {onCheckedProperty().set(value);}
    public final ObjectProperty<EventHandler<CheckedEvent>> onCheckedProperty() {
        if(onChecked == null) {
            onChecked = new SimpleObjectProperty<>(this, "onChecked") {
                @Override
                protected void invalidated() {
                    setEventHandler(CheckedEvent.CHECKED, get());
                }
            };
        }
        return onChecked;
    }

    /** onClick */
    private ObjectProperty<EventHandler<TagEvent>> onClick;
    public final EventHandler<TagEvent> getOnClick() {return onClickProperty().get();}
    public final void setOnClick(EventHandler<TagEvent> value) {onClickProperty().set(value);}
    public final ObjectProperty<EventHandler<TagEvent>> onClickProperty() {
        if(onClick == null) {
            onClick = new SimpleObjectProperty<>(this, "onClick") {
                @Override
                protected void invalidated() {
                    setEventHandler(TagEvent.CLICK, get());
                }
            };
        }
        return onClick;
    }

    public void fireCloseEvent() {
        fireEvent(new TagEvent(TagEvent.CLOSE));
    }

    public void fireClickEvent() {
        if(getMode() == TagMode.CHECKED) {
            setChecked(!isChecked());
            fireEvent(new CheckedEvent(isChecked()));
        }
        else {
            fireEvent(new TagEvent(TagEvent.CLICK));
        }
    }

    //@formatter:on

    @Override
    protected Skin<?> createDefaultSkin() {
        return new TagSkin(this);
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
    static final CssFactory<Tag> FACTORY = new CssFactory<>(Control.getClassCssMetaData()) {
        @Override
        protected void registerCss() {
            FACTORY.createPaintCss("-fx-tag-color", Tag::colorProperty);
            FACTORY.createEnumCss("-fx-tag-status", Tag::statusProperty, Status.class);
            FACTORY.createEnumCss("-fx-tag-mode", Tag::modeProperty, TagMode.class);
            FACTORY.createPaintCss("-fx-tag-text-fill", Tag::textFillProperty, Color.BLACK);
            FACTORY.createStringCss("-fx-close-icon", Tag::closeIconProperty, "far-xmark");
            FACTORY.createBooleanCss("-fx-visible-close", Tag::visibleCloseProperty, true);
            FACTORY.createSizeCss("-fx-close-gap", Tag::closeGapProperty, 5);

        }
    };

    public boolean isCloseMode() {
        return getMode() == TagMode.CLOSE;
    }

    public static class TagEvent extends Event {
        public static final EventType<TagEvent> ANY = new EventType<>(Event.ANY, "TAG_ANY");
        public static final EventType<TagEvent> CLOSE = new EventType<>(ANY, "TAG_CLOSE");
        public static final EventType<TagEvent> CLICK = new EventType<>(ANY, "TAG_CLICK");

        public TagEvent(EventType<? extends TagEvent> eventType) {
            super(eventType);
        }
    }

    public static class CheckedEvent extends TagEvent {
        public static final EventType<CheckedEvent> CHECKED = new EventType<>(TagEvent.ANY, "TAG_CHECKED");
        public static final EventType<CheckedEvent> ANY = CHECKED;
        private final boolean isChecked;

        public CheckedEvent(boolean isChecked) {
            super(CHECKED);
            this.isChecked = isChecked;
        }

        /**
         * Returns the isChecked
         */
        public boolean isChecked() {
            return isChecked;
        }
    }

    public enum TagMode  {
        CHECKED, CLOSE
    }
}