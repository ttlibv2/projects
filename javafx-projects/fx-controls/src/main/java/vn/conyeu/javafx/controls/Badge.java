package vn.conyeu.javafx.controls;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import vn.conyeu.javafx.controls.enums.NodeSize;
import vn.conyeu.javafx.controls.enums.Status;
import vn.conyeu.javafx.controls.skin.BadgeSkin;
import vn.conyeu.javafx.styleable.CssFactory;
import vn.conyeu.javafx.utils.Colors;
import vn.conyeu.javafx.utils.CssUtils;
import vn.conyeu.javafx.utils.NodeUtils;

import java.util.List;

public class Badge extends Control {

    public Badge() {
        getStyleClass().setAll("badge");
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new BadgeSkin(this);
    }

    //@formatter:off

    /** content */
    private ObjectProperty<Node> content;
    public final Node getContent() {return contentProperty().get();}
    public final void setContent(Node value) {contentProperty().set(value);}
    public final ObjectProperty<Node> contentProperty() {
        if(content == null) {
            content = new SimpleObjectProperty<>(this, "content");
        }
        return content;
    }

    /** position */
    private StyleableObjectProperty<Pos> position;
    public final Pos getPosition() {return positionProperty().get();}
    public final void setPosition(Pos value) {positionProperty().set(value);}
    public final StyleableObjectProperty<Pos> positionProperty() {
        if(position == null) {
            position = FACTORY.createEnum(this, "position", "-fx-position", Pos.class);
        }
        return position;
    }


    /** Customize Badge dot color */
    private StyleableObjectProperty<Paint> color;
    public final Paint getColor() {return colorProperty().get();}
    public final void setColor(Paint value) {colorProperty().set(value);}
    public final StyleableObjectProperty<Paint> colorProperty() {
        if(color == null) {
            color = FACTORY.createPaint(this, "color", "-fx-badge-color");
        }
        return color;
    }

    /** Number to show in badge */
    private StyleableIntegerProperty count;
    public final void setCount(int value) {countProperty().set(value);}
    public final int getCount() {return countProperty().get();}
    public final StyleableIntegerProperty countProperty() {
        if(count == null) {
            count = FACTORY.createInteger(this, "count", "-fx-badge-count");
        }
        return count;
    }

    /** Max count to show */
    private StyleableIntegerProperty overflowCount;
    public final void setOverflowCount(int value) {overflowCountProperty().set(value);}
    public final int getOverflowCount() {return overflowCountProperty().get();}
    public final StyleableIntegerProperty overflowCountProperty() {
        if(overflowCount == null) {
            overflowCount = FACTORY.createInteger(this, "overflowCount", "-fx-overflow-count");
        }
        return overflowCount;
    }

    /** Whether to show badge when count is zero */
    private StyleableBooleanProperty showZero;
    public final boolean isShowZero() {return showZeroProperty().get();}
    public final void setShowZero(boolean value) {showZeroProperty().set(value);}
    public final StyleableBooleanProperty showZeroProperty() {
        if(showZero == null) {
            showZero = FACTORY.createBoolean(this, "showZero", "-fx-show-zero");
        }
        return showZero;
    }

    /** status */
    private StyleableObjectProperty<Status> status;
    public final Status getStatus() {return statusProperty().get();}
    public final void setStatus(Status value) {statusProperty().set(value);}
    public final StyleableObjectProperty<Status> statusProperty() {
        if(status == null) {
            status = FACTORY.createEnum(this, "status", "-fx-status");
        }
        return status;
    }

    private StringProperty text;
    public final String getText() {return textProperty().get();}
    public final void setText(String value) {textProperty().set(value);}
    public final StringProperty textProperty() {
        if(text == null) {
            text = new SimpleStringProperty(this, "text");
        }
        return text;
    }

    /** set offset of the badge dot, like[left, top] */
    private StyleableDoubleProperty offsetX;
    public final void setOffsetX(double value) {offsetXProperty().set(value);}
    public final double getOffsetX() {return offsetXProperty().get();}
    public final StyleableDoubleProperty offsetXProperty() {
        if(offsetX == null) {
            offsetX = FACTORY.createDouble(this, "offsetX", "-fx-offset-x");
        }
        return offsetX;
    }

    /** set offset of the badge dot, like[left, top] */
    private StyleableDoubleProperty offsetY;
    public final void setOffsetY(double value) {offsetYProperty().set(value);}
    public final double getOffsetY() {return offsetYProperty().get();}
    public final StyleableDoubleProperty offsetYProperty() {
        if(offsetY == null) {
            offsetY = FACTORY.createDouble(this, "offsetY", "-fx-offset-y");
        }
        return offsetY;
    }

    /** type */
    private StyleableObjectProperty<BadgeType> type;
    public final BadgeType getType() {return typeProperty().get();}
    public final void setType(BadgeType value) {typeProperty().set(value);}
    public final StyleableObjectProperty<BadgeType> typeProperty() {
        if(type == null) {
            type = FACTORY.createEnum(this, "type", "-fx-badge-type", BadgeType.class);
        }
        return type;
    }

    /** dotSize */
    private StyleableDoubleProperty dotSize;
    public final void setDotSize(double value) {dotSizeProperty().set(value);}
    public final double getDotSize() {return dotSizeProperty().get();}
    public final StyleableDoubleProperty dotSizeProperty() {
        if(dotSize == null) {
            dotSize = FACTORY.createDouble(this, "dotSize", "-fx-dot-size");
        }
        return dotSize;
    }

    /** onAction */
    private ObjectProperty<EventHandler<ActionEvent>> onAction;
    public final EventHandler<ActionEvent> getOnAction() {return onActionProperty().get();}
    public final void setOnAction(EventHandler<ActionEvent> value) {onActionProperty().set(value);}
    public final ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
        if(onAction == null) {
            onAction = new SimpleObjectProperty<>(this, "onAction") {
                @Override
                protected void invalidated() {
                    setEventHandler(ActionEvent.ACTION, get());
                }
            };
        }
        return onAction;
    }

    //@formatter:on

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
    static final CssFactory<Badge> FACTORY = new CssFactory<>(Control.getClassCssMetaData()) {
        @Override
        protected void registerCss() {
            FACTORY.createPaintCss("-fx-badge-color", Badge::colorProperty, Color.valueOf("#fa4549"));
            FACTORY.createSizeCss("-fx-badge-count", Badge::countProperty, 0);
            FACTORY.createSizeCss("-fx-overflow-count", Badge::overflowCountProperty, 99);
            FACTORY.createBooleanCss("-fx-show-zero", Badge::showZeroProperty, true);
            FACTORY.createEnumCss("-fx-status", Badge::statusProperty, Status.class);
            FACTORY.createSizeCss("-fx-offset-x", Badge::offsetXProperty, -1);
            FACTORY.createSizeCss("-fx-offset-y", Badge::offsetYProperty, -1);
            FACTORY.createEnumCss("-fx-position", Badge::positionProperty, Pos.class, Pos.TOP_RIGHT);
            FACTORY.createEnumCss("-fx-badge-type", Badge::typeProperty, BadgeType.class, BadgeType.COUNT);
            FACTORY.createSizeCss("-fx-dot-size", Badge::dotSizeProperty, 6);

        }
    };

    public enum BadgeType {
        DOT, COUNT, TEXT
    }



}