package vn.conyeu.javafx.animation.ripple;

import javafx.animation.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.css.*;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import vn.conyeu.javafx.animation.beans.DepthLevel;
import vn.conyeu.javafx.animation.utils.Animations;
import vn.conyeu.javafx.animation.utils.Interpolators;
import vn.conyeu.javafx.animation.utils.KeyFrames;
import vn.conyeu.javafx.beans.PositionBean;
import vn.conyeu.javafx.styleable.CssFactory;
import vn.conyeu.javafx.utils.FxUtils;
import vn.conyeu.javafx.utils.NodeUtils;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Concrete implementation of {@link Ripple.GeneratorBase<CircleRipple>}.
 * <p>
 * This is the most basic ripple generator that generates circular ripples.
 * Accepted ripples are {@link CircleRipple} or subclasses.
 * <p></p>
 * Usage example:
 * <pre>
 * {@code
 *      Region region = ...
 *      RippleGenerator generator = new RippleGenerator(region);
 *      generator.setRipplePositionFunction(mouseEvent -> ...)
 *      region.getChildren().add(generator)
 *      region.addEventHandler(MouseEvent.MOUSE_PRESSED, generator::generateRipple)
 * }
 * </pre>
 */
public class RippleGenerator extends Ripple.GeneratorBase<RippleGenerator.CircleRipple> {

    public static Builder builder() {
        return new Builder();
    }

    //==============================================

    private final Builder builderGenerator;

    public RippleGenerator(Region node) {
       this(node, null);
    }

    private RippleGenerator(Region region, Builder builder) {
        super(region);
        defaultClipSupplier();
        defaultPositionFunction();
        defaultRippleSupplier();
        builderGenerator =  applyBuilder(builder);
    }

    @Override
    public void defaultClipSupplier() {
        setClipSupplier(Ripple.ClipType.RECTANGLE::buildShape);
    }

    @Override
    public void defaultPositionFunction() {
        setRipplePositionFunction(e -> new PositionBean(e.getX(), e.getY()));
    }

    @Override
    public void defaultRippleSupplier() {
        setRippleSupplier(CircleRipple::new);
    }

    /**
     * Generates a ripple.
     * <p></p>
     * <p> - If {@link #checkBoundsProperty()} is true calls {@link #isWithinBounds(MouseEvent)}. Exits if returns false.
     * <p> - Sets the generator clip/bounds by calling {@link #buildClip()}.
     * <p> - Computes the ripple coordinates by calling {@link #getRipplePositionFunction()} applied on the passed mouse event.
     * <p> - Creates the ripple by calling {@link #getRippleSupplier()}. Sets the center and fill properties of the ripple.
     * <p> - Creates the animations by calling {@link CircleRipple#getAnimation()}
     * <p> - If {@link #animateBackgroundProperty()} and {@link #animateShadowProperty()} are true, creates the respective animations
     * by calling {@link #getBackgroundAnimation()} and {@link #getShadowAnimation()}.
     * <p> - The animations are added to a {@link ParallelTransition} which is added to the animation stack.
     * <p> - The ripple is added to the generator's children list.
     * <p> - The animation starts.
     *
     * @see Ripple.GeneratorEvent
     */
    public void generateRipple(MouseEvent event) {
        if (isPaused()) return;
        if (isCheckBounds() && !isWithinBounds(event)) return;

        if (getClip() != null) setClip(null);

        setClip(buildClip());

        PositionBean position = getRipplePositionFunction().apply(event);

        CircleRipple ripple = getRippleSupplier().get();
        ripple.centerXProperty().bind(position.xProperty());
        ripple.centerYProperty().bind(position.yProperty());
        ripple.setFill(getRippleColor());
        ripple.setOpacity(getRippleOpacity());

        Animation rippleAnimation = ripple.getAnimation();
        rippleAnimation.setRate(getAnimationSpeed());

        ParallelTransition transition = new ParallelTransition(rippleAnimation);

        if (isAnimateBackground()) {
            Animation backgroundAnimation = getBackgroundAnimation();
            transition.getChildren().add(backgroundAnimation);
        }

        if (isAnimateShadow()) {
            Animation shadowAnimation = getShadowAnimation();
            transition.getChildren().add(shadowAnimation);
        }

        transition.setOnFinished(end -> getChildren().remove(ripple));
        getChildren().add(ripple);
        animationsStack.add(transition);
        transition.play();
    }

    /**
     * Responsible for building the ripple generator's clip,
     * which avoids ripple ending outside the region.
     *
     * @see NodeUtils#parseCornerRadius(Region)
     * @see NodeUtils#setBackground(Region, Paint, CornerRadii)
     */
    private Node buildClip() {
        if (!isAutoClip()) return getClipSupplier().apply(region);
        else {
            Region clip = new Region();
            clip.resizeRelocate(0, 0, region.getWidth(), region.getHeight());
            NodeUtils.setBackground(clip, Color.WHITE, NodeUtils.parseCornerRadius(region));
            return clip;
        }
    }

    /**
     * Attempts to compute the radius multiplier from the
     * starting ripple radius and the x position at which the ripple will be generated.
     * <p>
     * Currently, the value is computed by finding the region' side closest to the specified x coordinate.
     * The final radius is computed as the starting radius plus "Math.abs(xCoordinate - nearestBound)".
     * Then the multiplier is calculated as the final radius divided by the starting radius.
     * <p>
     * This method works in most cases. Sometimes the computed multiplier is not enough to cover the entire region.
     * This is probably because the right way would be to consider the farthermost vertex rather than side.
     * However, it's not a big issue since you can also manipulate the initial radius or set the multiplier manually.
     *
     * @param xCoordinate the x coordinate at which the ripple will be generated
     * @see #radiusMultiplierProperty()
     */
    protected double computeRadiusMultiplier(double xCoordinate) {
        final Bounds bounds = region.getLayoutBounds();
        final double distanceFromMax = Math.abs(bounds.getMaxX() - xCoordinate);
        final double nearestBound = distanceFromMax < xCoordinate ? 0 : bounds.getMaxX();
        final double finalRadius = getRippleRadius() + Math.abs(xCoordinate - nearestBound);
        return finalRadius / getRippleRadius();
    }

    /**
     * Builds the animation of the {@link DropShadow} effect.
     * If the region's effect is not a DropShadow or its level is not recognized by {@link DepthLevel#from(DropShadow)}
     * then an empty animation is returned.
     * <p>
     * If the effect is recognized then builds two new DropShadow effects, a start one and an end one.
     * The start effect is the same level as the region's one and the end is computed by {@link DepthLevel#shadowOf(DepthLevel, int)}
     * using {@link #depthLevelOffsetProperty()} as argument.
     * <p>
     * <b>N.B: as stated above the animation works only for shadows defined by {@link DepthLevel}</b>
     */
    protected Animation getShadowAnimation() {
        Timeline animation = new Timeline();

        if (region.getEffect() instanceof DropShadow shadowEffect) {
            DepthLevel level = DepthLevel.from(shadowEffect);

            if (level != null) {
                DropShadow startShadow = DepthLevel.shadowOf(level);
                DropShadow endShadow = DepthLevel.shadowOf(level, getDepthLevelOffset());

                // Spread
                KeyValue keyValue5 = new KeyValue(shadowEffect.spreadProperty(), endShadow.getSpread());
                KeyValue keyValue6 = new KeyValue(shadowEffect.spreadProperty(), startShadow.getSpread());

                //Radius
                KeyValue keyValue7 = new KeyValue(shadowEffect.radiusProperty(), endShadow.getRadius());
                KeyValue keyValue8 = new KeyValue(shadowEffect.radiusProperty(), startShadow.getRadius());

                // Offsets
                KeyValue keyValue9 = new KeyValue(shadowEffect.offsetXProperty(), endShadow.getOffsetX());
                KeyValue keyValue10 = new KeyValue(shadowEffect.offsetXProperty(), startShadow.getOffsetX());
                KeyValue keyValue11 = new KeyValue(shadowEffect.offsetYProperty(), endShadow.getOffsetY());
                KeyValue keyValue12 = new KeyValue(shadowEffect.offsetYProperty(), startShadow.getOffsetY());

                KeyFrame keyFrame5 = new KeyFrame(Duration.millis(350), keyValue5, keyValue7, keyValue9, keyValue11);
                KeyFrame keyFrame6 = new KeyFrame(Duration.millis(700), keyValue6, keyValue8, keyValue10, keyValue12);

                animation.getKeyFrames().setAll(keyFrame5, keyFrame6);
            }
        }
        return animation;
    }

    /**
     * Builds the background animation. It consists in a temporary
     * shape added to the generator's children list in position 0.
     * The opacity of the shape is increased to the value specified by {@link #backgroundOpacityProperty()},
     * and then dropped to 0. When the opacity is 0 it is removed from the children list.
     */
    protected Animation getBackgroundAnimation() {
        if (getClipSupplier() == null || getClipShape() == null) {
            throw new NullPointerException("RippleGenerator cannot animate background because clip supplier is null!");
        }

        Shape shape = getClipShape();
        shape.setFill(getRippleColor());
        shape.setOpacity(0);
        shape.opacityProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() == 0) {
                getChildren().remove(shape);
            }
        });

        return Animations.buildTimeline(
                KeyFrames.of(Duration.ZERO, event -> getChildren().add(0, shape)),
                KeyFrames.of(Duration.millis(300), shape.opacityProperty(), getBackgroundOpacity()),
                KeyFrames.of(Duration.millis(450), shape.opacityProperty(), 0, Interpolator.LINEAR)
        );
    }

    public Builder cloneBuilder() {
        return new Builder(builderGenerator);
    }

    private Builder applyBuilder(Builder builder) {
        if(builder == null) return new Builder();
        else {
            FxUtils.applyConsumer(builder.clipSupplier, this::setClipSupplier);
            FxUtils.applyConsumer(builder.rippleSupplier, this::setRippleSupplier);
            FxUtils.applyConsumer(builder.ripplePositionFunction, this::setRipplePositionFunction);
            FxUtils.applyConsumer(builder.rippleColor, this::setRippleColor);
            FxUtils.applyConsumer(builder.rippleOpacity, this::setRippleOpacity);
            FxUtils.applyConsumer(builder.rippleRadius, this::setRippleRadius);
            FxUtils.applyConsumer(builder.animateBackground, this::setAnimateBackground);
            FxUtils.applyConsumer(builder.animateShadow, this::setAnimateShadow);
            FxUtils.applyConsumer(builder.checkBounds, this::setCheckBounds);
            FxUtils.applyConsumer(builder.depthLevelOffset, this::setDepthLevelOffset);
            FxUtils.applyConsumer(builder.onAnimationFinished, this::setOnAnimationFinished);
            FxUtils.applyConsumer(builder.computeRadiusMultiplier, this::setComputeRadiusMultiplier);
            FxUtils.applyConsumer(builder.radiusMultiplier, this::setRadiusMultiplier);
            FxUtils.applyConsumer(builder.animationSpeed, this::setAnimationSpeed);
            FxUtils.applyConsumer(builder.autoClip, this::setAutoClip);
            FxUtils.applyConsumer(builder.backgroundOpacity, this::setBackgroundOpacity);
            FxUtils.applyConsumer(builder.paused, this::setPaused);
            FxUtils.applyConsumer(builder.snapToPixel, this::setSnapToPixel);
            FxUtils.applyConsumer(builder.background, this::setBackground);
            FxUtils.applyConsumer(builder.border, this::setBorder);
            FxUtils.applyConsumer(builder.padding, this::setPadding);
            FxUtils.applyConsumer(builder.opaqueInsets, this::setOpaqueInsets);
            FxUtils.applyConsumer(builder.minWidth, this::setMinWidth);
            FxUtils.applyConsumer(builder.minHeight, this::setMinHeight);
            FxUtils.applyConsumer(builder.prefWidth, this::setPrefWidth);
            FxUtils.applyConsumer(builder.prefHeight, this::setPrefHeight);
            FxUtils.applyConsumer(builder.maxWidth, this::setMaxWidth);
            FxUtils.applyConsumer(builder.maxHeight, this::setMaxHeight);
            FxUtils.applyConsumer(builder.shape, this::setShape);
            FxUtils.applyConsumer(builder.scaleShape, this::setScaleShape);
            FxUtils.applyConsumer(builder.centerShape, this::setCenterShape);
            FxUtils.applyConsumer(builder.cacheShape, this::setCacheShape);
            return builder;
        }
    }

    //@formatter:off

    /**
     * Specifies if the ripple's radius multiplier should be computed automatically.
     * If this is true the value specified by {@link #radiusMultiplierProperty()} will be ignored
     * and {@link #computeRadiusMultiplier(double)} will be called instead.
     */
    private BooleanProperty computeRadiusMultiplier;
    public final boolean isComputeRadiusMultiplier() {return computeRadiusMultiplierProperty().get();}
    public final void setComputeRadiusMultiplier(boolean value) {computeRadiusMultiplierProperty().set(value);}
    public final BooleanProperty computeRadiusMultiplierProperty() {
        if(computeRadiusMultiplier == null) {
            computeRadiusMultiplier = new SimpleBooleanProperty(this, "computeRadiusMultiplier", false);
        }
        return computeRadiusMultiplier;
    }

    /**
     * Specifies the multiplier used to obtain the final ripple's radius.
     * <p></p>
     * If you are still wondering what the heck is this multiplier then read this.
     * <p></p>
     * The ripple is basically a shape, in this case a circle with initial radius 0, created at specified coordinates.
     * <p>
     * Generally speaking:
     * <p>
     * The first phase of the ripple animation consists in increasing the radius to the value specified by {@link #rippleRadiusProperty()}.
     * <p>
     * The second phase is to further expand that radius (like a ripple in a lake lol) to make the circle cover entirely or almost the region.
     * <p>
     * The last phase is to drop the circle opacity to 0 and remove the ripple.
     */
    private DoubleProperty radiusMultiplier;
    public final double getRadiusMultiplier() {return radiusMultiplierProperty().get();}
    public final void setRadiusMultiplier(double value) {radiusMultiplierProperty().set(value);}
    public final DoubleProperty radiusMultiplierProperty() {
        if(radiusMultiplier == null) {
            radiusMultiplier = new SimpleDoubleProperty(this, "radiusMultiplier", 2.0);
        }
        return radiusMultiplier;
    }

    /**
     * Specifies the speed on the ripples' animation.
     * This is done by setting the animations rate property, {@link Animation#setRate(double)}
     */
    private StyleableDoubleProperty animationSpeed;
    public final void setAnimationSpeed(double value) {animationSpeedProperty().set(value);}
    public final double getAnimationSpeed() {return animationSpeedProperty().get();}
    public final StyleableDoubleProperty animationSpeedProperty() {
        if(animationSpeed == null) {
            animationSpeed = FACTORY.createDouble(this, "animationSpeed", "-fx-animation-speed");
        }
        return animationSpeed;
    }

    /**
     * Specifies whether the generator should try to {@link #buildClip()} automatically,
     * this means also trying to fetch the background/border radius.
     * <p>
     * <b>EXPERIMENTAL, may not work in all situations</b>
     */
    private StyleableBooleanProperty autoClip;
    public final boolean isAutoClip() {return autoClipProperty().get();}
    public final void setAutoClip(boolean value) {autoClipProperty().set(value);}
    public final StyleableBooleanProperty autoClipProperty() {
        if(autoClip == null) {
            autoClip = FACTORY.createBoolean(this, "autoClip", "-fx-auto-clip");
        }
        return autoClip;
    }

    /**
     * Specifies the strength of the background animation.
     */
    private StyleableDoubleProperty backgroundOpacity;
    public final void setBackgroundOpacity(double value) {backgroundOpacityProperty().set(value);}
    public final double getBackgroundOpacity() {return backgroundOpacityProperty().get();}
    public final StyleableDoubleProperty backgroundOpacityProperty() {
        if(backgroundOpacity == null) {
            backgroundOpacity = FACTORY.createDouble(this, "backgroundOpacity", "-fx-background-opacity");
        }
        return backgroundOpacity;
    }

    /**
     * Property to enable/disable the ripple generator.
     */
    private StyleableBooleanProperty paused;
    public final boolean isPaused() {return pausedProperty().get();}
    public final void setPaused(boolean value) {pausedProperty().set(value);}
    public final StyleableBooleanProperty pausedProperty() {
        if(paused == null) {
            paused = FACTORY.createBoolean(this, "paused", "-fx-paused");
        }
        return paused;
    }

    /**
     * Specifies the ripples' color.
     */
    private StyleableObjectProperty<Paint> rippleColor;
    public final Paint getRippleColor() {return rippleColorProperty().get();}
    public final void setRippleColor(Paint value) {rippleColorProperty().set(value);}
    public final StyleableObjectProperty<Paint> rippleColorProperty() {
        if(rippleColor == null) {
            rippleColor = FACTORY.createPaint(this, "rippleColor", "-fx-ripple-color");
        }
        return rippleColor;
    }

    /**
     * Specifies the initial ripple's opacity.
     */
    private StyleableDoubleProperty rippleOpacity;
    public final void setRippleOpacity(double value) {rippleOpacityProperty().set(value);}
    public final double getRippleOpacity() {return rippleOpacityProperty().get();}
    public final StyleableDoubleProperty rippleOpacityProperty() {
        if(rippleOpacity == null) {
            rippleOpacity = FACTORY.createDouble(this, "rippleOpacity", "-fx-ripple-opacity");
        }
        return rippleOpacity;
    }

    /**
     * Specifies the ripples' initial radius.
     */
    private StyleableDoubleProperty rippleRadius;
    public final void setRippleRadius(double value) {rippleRadiusProperty().set(value);}
    public final double getRippleRadius() {return rippleRadiusProperty().get();}
    public final StyleableDoubleProperty rippleRadiusProperty() {
        if(rippleRadius == null) {
            rippleRadius = FACTORY.createDouble(this, "radiusProperty", "-fx-ripple-radius");
        }
        return rippleRadius;
    }

    //@formatter:on


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
    static final CssFactory<RippleGenerator> FACTORY = new CssFactory<>(Ripple.GeneratorBase.getClassCssMetaData()) {
        @Override
        protected void registerCss() {
            FACTORY.createSizeCss("-fx-animation-speed", RippleGenerator::animationSpeedProperty, 1);
            FACTORY.createBooleanCss("-fx-auto-clip", RippleGenerator::autoClipProperty, false);
            FACTORY.createSizeCss("-fx-background-opacity", RippleGenerator::backgroundOpacityProperty, .3);
            FACTORY.createBooleanCss("-fx-paused", RippleGenerator::pausedProperty, false);
            FACTORY.createPaintCss("-fx-ripple-color", RippleGenerator::rippleColorProperty, Color.valueOf("#f4fcfa"));
            FACTORY.createSizeCss("-fx-ripple-opacity", RippleGenerator::rippleOpacityProperty, 1);
            FACTORY.createSizeCss("-fx-ripple-radius", RippleGenerator::rippleRadiusProperty, 10);

        }
    };

    public final class CircleRipple extends Circle implements Ripple {
        private double xPosition = -1;

        private CircleRipple() {
            setRadius(0);
        }

        @Override
        public Animation getAnimation() {
            double mul = getRadiusMultiplier();
            if (isComputeRadiusMultiplier()) {
                mul = computeRadiusMultiplier(xPosition);
            }

            return Animations.buildTimeline(
                    KeyFrames.of(150, radiusProperty(), getRippleRadius()),
                    KeyFrames.of(400, radiusProperty(), (getRippleRadius() * mul)),
                    KeyFrames.of(1200, opacityProperty(), 0, Interpolators.INTERPOLATOR_V2),
                    KeyFrames.of(500, event -> animationsStack.pop())
            );
        }

        public void setxPosition(double xPosition) {
            this.xPosition = xPosition;
        }

    }

    public static final class Builder {
        private Function<Region, Shape> clipSupplier;
        private Supplier<CircleRipple> rippleSupplier;
        private Function<MouseEvent, PositionBean> ripplePositionFunction;
        private Boolean animateBackground;
        private Boolean animateShadow;
        private Boolean checkBounds;
        private Integer depthLevelOffset;
        private EventHandler<Ripple.GeneratorEvent> onAnimationFinished;
        private Boolean computeRadiusMultiplier;
        private Double radiusMultiplier;
        private Double animationSpeed;
        private Boolean autoClip;
        private Double backgroundOpacity;
        private Boolean paused;
        private Paint rippleColor;
        private Double rippleOpacity;
        private Double rippleRadius;
        private Boolean snapToPixel;
        private Background background;
        private Border border;
        private Insets padding;
        private Insets opaqueInsets;
        private Double minWidth;
        private Double minHeight;
        private Double prefWidth;
        private Double prefHeight;
        private Double maxWidth;
        private Double maxHeight;
        private Shape shape;
        private Boolean scaleShape;
        private Boolean centerShape;
        private Boolean cacheShape;

        public Builder() {
        }

        Builder(Builder other) {
            this.clipSupplier = other.clipSupplier;
            this.rippleSupplier = other.rippleSupplier;
            this.ripplePositionFunction = other.ripplePositionFunction;
            this.animateBackground = other.animateBackground;
            this.animateShadow = other.animateShadow;
            this.checkBounds = other.checkBounds;
            this.depthLevelOffset = other.depthLevelOffset;
            this.onAnimationFinished = other.onAnimationFinished;
            this.computeRadiusMultiplier = other.computeRadiusMultiplier;
            this.radiusMultiplier = other.radiusMultiplier;
            this.animationSpeed = other.animationSpeed;
            this.autoClip = other.autoClip;
            this.backgroundOpacity = other.backgroundOpacity;
            this.paused = other.paused;
            this.rippleColor = other.rippleColor;
            this.rippleOpacity = other.rippleOpacity;
            this.rippleRadius = other.rippleRadius;
            this.snapToPixel = other.snapToPixel;
            this.border = other.border;
            this.padding = other.padding;
            this.opaqueInsets = other.opaqueInsets;
            this.minWidth = other.minWidth;
            this.minHeight = other.minHeight;
            this.prefWidth = other.prefWidth;
            this.prefHeight = other.prefHeight;
            this.maxWidth = other.maxWidth;
            this.maxHeight = other.maxHeight;
            this.shape = other.shape;
            this.scaleShape = other.scaleShape;
            this.centerShape = other.centerShape;
            this.cacheShape = other.cacheShape;
            this.background = other.background;
        }

        private <T> void apply(T value, Consumer<T> consumer) {
            if (value != null) consumer.accept(value);
        }


        public Builder clipSupplier(Function<Region, Shape> supplier) {
            this.clipSupplier = supplier;
            return this;
        }

        public Builder rippleSupplier(Supplier<CircleRipple> supplier) {
            this.rippleSupplier = supplier;
            return this;
        }

        public Builder ripplePositionFunction(Function<MouseEvent, PositionBean> function) {
            this.ripplePositionFunction = function;
            return this;
        }

        public Builder animateBackground(boolean value) {
            this.animateBackground = value;
            return this;
        }

        public Builder animateShadow(boolean value) {
            this.animateShadow = value;
            return this;
        }

        public Builder checkBounds(boolean value) {
            this.checkBounds = value;
            return this;
        }

        public Builder depthLevelOffset(int value) {
            this.depthLevelOffset = value;
            return this;
        }

        public Builder onAnimationFinished(EventHandler<Ripple.GeneratorEvent> value) {
            this.onAnimationFinished = value;
            return this;
        }

        public Builder computeRadiusMultiplier(boolean value) {
            this.computeRadiusMultiplier = value;
            return this;
        }

        public Builder radiusMultiplier(double value) {
            this.radiusMultiplier = value;
            return this;
        }

        public Builder animationSpeed(double value) {
            this.animationSpeed = value;
            return this;
        }

        public Builder autoClip(boolean value) {
            this.autoClip = value;
            return this;
        }

        public Builder backgroundOpacity(double value) {
            this.backgroundOpacity = value;
            return this;
        }

        public Builder paused(boolean value) {
            this.paused = value;
            return this;
        }

        public Builder rippleColor(Paint value) {
            this.rippleColor = value;
            return this;
        }

        public Builder rippleOpacity(double value) {
            this.rippleOpacity = value;
            return this;
        }

        public Builder rippleRadius(double value) {
            this.rippleRadius = value;
            return this;
        }

        public Builder snapToPixel(boolean value) {
            this.snapToPixel = value;
            return this;
        }

        public Builder padding(Insets value) {
            this.padding = value;
            return this;
        }

        public Builder background(Background value) {
            this.background = value;
            return this;
        }

        public Builder border(Border value) {
            this.border = value;
            return this;
        }

        public Builder opaqueInsets(Insets value) {
            this.opaqueInsets = value;
            return this;
        }

        public Builder minWidth(double value) {
            this.minWidth = value;
            return this;
        }

        public Builder minHeight(double value) {
            this.minHeight = value;
            return this;
        }

        public Builder prefWidth(double value) {
            this.prefWidth = value;
            return this;
        }

        public Builder prefHeight(double value) {
            this.prefHeight = value;
            return this;
        }

        public Builder maxWidth(double value) {
            this.maxWidth = value;
            return this;
        }

        public Builder maxHeight(double value) {
            this.maxHeight = value;
            return this;
        }

        public Builder shape(Shape value) {
            this.shape = value;
            return this;
        }

        public Builder scaleShape(boolean value) {
            this.scaleShape = value;
            return this;
        }

        public Builder centerShape(boolean value) {
            this.centerShape = value;
            return this;
        }

        public Builder cacheShape(boolean value) {
            this.cacheShape = value;
            return this;
        }

        public RippleGenerator build(Region region) {
            Builder cloneBuilder = new Builder(this);
            return new RippleGenerator(region, cloneBuilder);
        }

    }
}