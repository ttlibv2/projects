package vn.conyeu.javafx.controls;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.css.*;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.javafx.animation.ripple.Ripple;
import vn.conyeu.javafx.animation.ripple.RippleGenerator;
import vn.conyeu.javafx.beans.PositionBean;
import vn.conyeu.javafx.icons.core.IconHandler;
import vn.conyeu.javafx.icons.core.SimpleIcon;
import vn.conyeu.javafx.icons.font.FontIcon;
import vn.conyeu.javafx.styleable.CssFactory;

import java.util.List;
import java.util.function.Function;

public class IconWrapper extends StackPane {

    private final RippleGenerator generator = new RippleGenerator(this){
        @Override
        public void defaultClipSupplier() {
            setClipSupplier(Ripple.ClipType.CIRCLE::buildShape);
        }
    };

    private final EventHandler<MouseEvent> rHandler;

    /**
     * Create constructor
     */
    public IconWrapper() {
        getStyleClass().setAll("icon-wrapper");
        setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        addEventHandler(MouseEvent.MOUSE_PRESSED, event -> requestFocus());
        rHandler = e -> {
            if (e.getButton() == MouseButton.PRIMARY)
                generator.generateRipple(e);
        };


    }

    public IconWrapper(Node icon) {
        this();
        setIcon(icon);
    }

    /**
     * Create constructor
     *
     * @param icon the icon
     * @param size the size of container
     */
    public IconWrapper(Node icon, double size) {
        this();
        setIcon(icon);
        setSize(size);
    }

    /**
     * Create constructor
     *
     * @param literal     the literal icon
     * @param size        the size icon
     * @param wrapperSize the size of container
     */
    public IconWrapper(String literal, double size, double wrapperSize) {
        this(new FontIcon(literal, size), wrapperSize);
    }

    /**
     * Create constructor
     *
     * @param literal     the literal icon
     * @param size        the size icon
     * @param color       the color icon
     * @param wrapperSize the size of container
     */
    public IconWrapper(String literal, double size, Color color, double wrapperSize) {
        this(new FontIcon(literal, size, color), wrapperSize);
    }

    /**
     * @return an unmodifiable list of the StackPane children
     */
    public ObservableList<Node> getChildren() {
        return getChildrenUnmodifiable();
    }

    /**
     * Calls {@link #enableRippleGenerator(boolean, Function)}, the function to determine the ripple location
     * uses the {@link MouseEvent#getX()} and {@link MouseEvent#getY()} coordinates.
     */
    public IconWrapper enableRippleGenerator(boolean enable) {
        return enableRippleGenerator(enable, e -> PositionBean.of(e.getX(), e.getY()));
    }

    /**
     * Enables or disables the ripple effect for this wrapper depending on the given boolean flag.
     * <p>If the flag is false the ripple generator is removed from the container and set to null.</p>
     * <p>If the flag is true a new ripple generator is created, the given function determines where ripple effects will be
     * generated. An {@link EventHandler} is also added to generate ripples on {@link MouseEvent#MOUSE_PRESSED} when
     * the clicked button is the primary.</p>
     *
     * @throws IllegalStateException if the boolean flag is true and the ripple generator is already present
     */
    public IconWrapper enableRippleGenerator(boolean enable, Function<MouseEvent, PositionBean> positionFunction) {
        if (!enable) {
            removeEventHandler(MouseEvent.MOUSE_PRESSED, rHandler);
            super.getChildren().remove(generator);
        }
        else if(!getSuperNodes().contains(generator)){
            generator.setRipplePositionFunction(positionFunction);
            addEventHandler(MouseEvent.MOUSE_PRESSED, rHandler);
            tryAddNode(generator);
            setEnableRipple(true);
        }
        return this;
    }

    /**
     * Makes this container round by applying a {@link Circle} clip on it.
     * <p>If the given boolean flag is false the clip is removed.</p>
     * <p>If the given boolean flag is true and the clip is already set, simply returns.</p>
     */
    public IconWrapper makeRound(boolean state) {
        if (!state) setClip(null);
        else if(getClip() == null){
            Circle circle = new Circle();
            circle.radiusProperty().bind(widthProperty().divide(2.0));
            circle.centerXProperty().bind(widthProperty().divide(2.0));
            circle.centerYProperty().bind(heightProperty().divide(2.0));
            setClip(circle);
            setRound(true);
        }
        return this;
    }

    /**
     * Makes this container round by applying a {@link Circle} clip on it, uses the given radius for the circle.
     * <p></p>
     * If the given boolean flag is false the clip is removed.
     * <p>
     * If the given boolean flag is true and the clip is already set, simply returns.
     */
    public IconWrapper makeRound(boolean state, double radius) {
        if (!state) setClip(null);
        else if(getClip() == null){
            Circle circle = new Circle(radius);
            circle.centerXProperty().bind(widthProperty().divide(2.0));
            circle.centerYProperty().bind(heightProperty().divide(2.0));
            setClip(circle);
            setRound(true);
        }
        return this;
    }

    public IconWrapper setIcon(IconHandler handler, String literal) {
        Asserts.notNull(handler, "@IconHandler");
        Asserts.notBlank(literal, "@String");
        setIcon(handler.createIcon(literal));
        return this;
    }

    public IconWrapper addStyleClasses(String...styleClass) {
        getStyleClass().addAll(styleClass);
        return this;
    }

    public IconWrapper cursor(Cursor cursor) {
        setCursor(cursor);
        return this;
    }

    /**
     * @return the {@link RippleGenerator} instance for this wrapper, note that
     * if the generator is not enabled this will return null
     */
    public final RippleGenerator getRippleGenerator() {
        return generator;
    }

    //@formatter:off

    /**
     * Specifies the currently contained {@link SimpleIcon}.
     */
    private ObjectProperty<Node> icon;
    public final Node getIcon() {return iconProperty().get();}
    public final void setIcon(Node value) {iconProperty().set(value);}
    public final ObjectProperty<Node> iconProperty() {
        if(icon == null) {
            icon = new SimpleObjectProperty<>(this, "icon"){
                @Override
                public void set(Node newValue) {
                    changeIcon(get(), newValue);
                    super.set(newValue);
                }
            };
        }
        return icon;
    }

    /**
     * Specifies the size of this container, when set to -1 it will figure out the value automatically at layout time.
     * <p> Settable in CSS via the property: '-fx-size'.</p>
     */
    private StyleableDoubleProperty size;
    public final void setSize(double value) {sizeProperty().set(value);}
    public final double getSize() {return sizeProperty().get();}
    public final StyleableDoubleProperty sizeProperty() {
        if(size == null) {
            CssMetaData<IconWrapper, Number> cssMetaData = FACTORY.getCssMetaData("-fx-size");
            size = new SimpleStyleableDoubleProperty(cssMetaData, this, "size", -1.0){
                @Override
                protected void invalidated() {
                    setPrefSize(getSize(), getSize());
                }
            };
        }
        return size;
    }

    /**
     * A useful property to enable ripple effect from CSS, the property will automatically call
     * {@link #enableRippleGenerator(boolean)}.
     * <p>Settable in CSS via the property: '-fx-enable-ripple'.</p>
     */
    private StyleableBooleanProperty enableRipple;
    public final boolean isEnableRipple() {return enableRippleProperty().get();}
    public final void setEnableRipple(boolean value) {enableRippleProperty().set(value);}
    public final StyleableBooleanProperty enableRippleProperty() {
        if(enableRipple == null) {
            enableRipple = FACTORY.createBoolean(this, "enableRipple", "-fx-enable-ripple",
                    (o, state) -> enableRippleGenerator(state));
        }
        return enableRipple;
    }

    /**
     * A useful property to make this container round from CSS, the property will automatically call
     * {@link #makeRound(boolean)}.
     * <p>Settable in CSS via the property: '-fx-round'.</p>
     */
    private StyleableBooleanProperty round;
    public final boolean isRound() {return roundProperty().get();}
    public final void setRound(boolean value) {roundProperty().set(value);}
    public final StyleableBooleanProperty roundProperty() {
        if(round == null) {
            round = FACTORY.createBoolean(this, "round", "-fx-round",
                    (o, state) -> makeRound(state));
        }
        return round;
    }

    //@formatter:on

    private List<Node> getSuperNodes() {
        return super.getChildren();
    }

    private void changeIcon(Node oldIcon, Node newIcon) {
        if(oldIcon != null) super.getChildren().remove(oldIcon);
        if(newIcon != null) tryAddNode(newIcon);
    }
    private void tryAddNode( Node node) {
        List<Node> children = getSuperNodes();
        if(!children.contains(node)) {
            if (generator == node) children.add(0, node);
            else children.add(node);
        }

    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        Node icon = getIcon();
        if (getScene() == null || getScene().getWindow() == null) return;
        if (icon != null && getSize() == Region.USE_COMPUTED_SIZE) {
            final double iW = snapSizeX(icon.prefWidth(-1)), iH = snapSizeY(icon.prefHeight(-1));
            final double left = snappedLeftInset(), right = snappedRightInset();
            final double top = snappedTopInset(), bottom = snappedBottomInset();
            setSize(Math.max(left + iW + right, top + iH + bottom));
        }
    }

    @Override
    protected double computeMinWidth(double height) {
        //if(getIcon() == null)return super.computeMinWidth(height);
        //else return getIcon().minWidth(height) + getInsets().getLeft() + getInsets().getRight();
        return prefWidth(height);
    }

    @Override
    protected double computeMinHeight(double width) {
        //if(getIcon() == null)return super.computeMinHeight(width);
        //else return getIcon().minHeight(width) + getInsets().getTop() + getInsets().getBottom();
        return prefHeight(width);
    }

    @Override
    protected double computePrefWidth(double height) {
        if(getIcon() == null) return super.computePrefWidth(height);
        else return getIcon().prefWidth(height)  + getInsets().getLeft() + getInsets().getRight();
    }

    @Override
    protected double computePrefHeight(double width) {
        if(getIcon() == null)return super.computePrefHeight(width);
        else return getIcon().prefHeight(width) + getInsets().getTop() + getInsets().getBottom();
    }

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
    static final CssFactory<IconWrapper> FACTORY = new CssFactory<>(StackPane.getClassCssMetaData()) {
        @Override
        protected void registerCss() {
            FACTORY.createSizeCss("-fx-size", IconWrapper::sizeProperty, -1);
            FACTORY.createBooleanCss("-fx-enable-ripple", IconWrapper::enableRippleProperty, false);
            FACTORY.createBooleanCss("-fx-round", IconWrapper::roundProperty, false);

        }
    };
}