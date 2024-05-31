package vn.conyeu.javafx.animation.ripple;

import javafx.animation.Animation;
import javafx.beans.property.*;
import javafx.collections.ListChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Bounds;
import javafx.scene.control.CheckBox;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.javafx.animation.beans.DepthLevel;
import vn.conyeu.javafx.beans.PositionBean;
import vn.conyeu.javafx.collections.ObservableStack;

import java.util.function.Function;
import java.util.function.Supplier;

public interface Ripple {
    Animation getAnimation();

    interface Generator<T extends Ripple> {
        /**
         * @return the region on which the ripple will be generated
         */
        Region getRegion();

        /**
         * Every ripple generator should have a default clip supplier.
         */
        void defaultClipSupplier();

        /**
         * @return the current generator's clip supplier
         */
        Function<Region, Shape> getClipSupplier();

        /**
         * Sets the generator's clip supplier to the specified one.
         * <p>
         * This is responsible for creating the clip node of the generator, which is built and set
         * everytime the ripple is generated, before the animation is started, and defines
         * the bounds beyond which the ripple must not go.
         * <p>
         * Although the supplier accepts any {@link Ripple} it is highly recommended to build clips
         * using {@link ClipType}.
         */
        void setClipSupplier(Function<Region, Shape> clipSupplier);

        /**
         * Every ripple generator should have a default position for the ripples.
         */
        void defaultPositionFunction();

        /**
         * @return the current generator's position function
         */
        Function<MouseEvent, PositionBean> getRipplePositionFunction();

        /**
         * Sets the generator's ripple position function to the specified one.
         * <p>
         * This {@link Function} is responsible for computing the ripple's x and y
         * coordinates before the animation is played. The function takes a MouseEvent as the input
         * (since in most controls the coordinates are the x and y coordinates of the mouse event)
         * and returns a {@link PositionBean} bean.
         */
        void setRipplePositionFunction(Function<MouseEvent, PositionBean> positionFunction);

        /**
         * Every ripple generator should have a default ripple supplier.
         */
        void defaultRippleSupplier();

        /**
         * @return the current generator's ripple supplier
         */
        Supplier<T> getRippleSupplier();

        /**
         * Sets the generator's ripple supplier to the specified one.
         * <p>
         * This {@link Supplier} is responsible for creating the ripple shape before the animation
         * is played.
         */
        void setRippleSupplier(Supplier<T> rippleSupplier);
    }

    enum ClipType {
        CIRCLE,
        RECTANGLE,
        ROUNDED_RECTANGLE,
        NO_CLIP;

        public ClipTypeFactory createFactory() {
            return new ClipTypeFactory(this);
        }

        public ClipTypeFactory createFactory(double arcW, double arcH) {
            return new ClipTypeFactory(this, arcW, arcH);
        }

        public Shape buildShape(Region region) {
            return createFactory().build(region);
        }

        public Shape buildShape(Region region, double arcW, double arcH) {
            return createFactory(arcW, arcH).build(region);
        }

    }

    static RippleGenerator.Builder simple() {
        return RippleGenerator.builder();
    }

    /**
     * Convenience class for building Ripple clip shapes.
     */
    final class ClipTypeFactory {
        private final ClipType clipType;
        private double radius = 0;
        private double arcW = 0, arcH = 0;
        private double offsetW = 0, offsetH = 0;

        public ClipTypeFactory() {
            this(ClipType.NO_CLIP, 0, 0);
        }

        public ClipTypeFactory(ClipType clipType) {
            this(clipType, 0, 0);
        }

        public ClipTypeFactory(ClipType clipType, double arcW, double arcH) {
            this.clipType = clipType;
            this.arcW = arcW;
            this.arcH = arcH;
        }

        public ClipTypeFactory radius(double radius) {
            this.radius = radius;
            return this;
        }

        public ClipTypeFactory arcW(double arcW) {
            this.arcW = arcW;
            return this;
        }

        public ClipTypeFactory arcH(double arcH) {
            this.arcH = arcH;
            return this;
        }

        public ClipTypeFactory offsetW(double offsetW) {
            this.offsetW = offsetW;
            return this;
        }

        public ClipTypeFactory offsetH(double offsetH) {
            this.offsetH = offsetH;
            return this;
        }

        public Shape build(Region region) {
            double w = region.getWidth() + offsetW;
            double h = region.getHeight() + offsetH;

            return switch (clipType) {
                case CIRCLE -> {
                    double radius = this.radius == 0 ? Math.sqrt(Math.pow(w, 2) + Math.pow(h, 2)) / 2 : this.radius;
                    Circle circle = new Circle(radius);
                    circle.setTranslateX(w / 2);
                    circle.setTranslateY(h / 2);
                    yield circle;
                }
                case RECTANGLE -> new Rectangle(w, h);
                case ROUNDED_RECTANGLE -> {
                    Rectangle rectangle = new Rectangle(w, h);
                    rectangle.setArcWidth(arcW);
                    rectangle.setArcHeight(arcH);
                    yield rectangle;
                }
                default -> null;
            };
        }
    }

    /**
     * Abstract class that defines all the properties and behaviors a RippleGenerator should have.
     * <p>
     * Also defines the style class ("ripple-generator") for all generators that extend this class.
     * <p></p>
     * When generating ripples, four are three important information:
     * <p> - Region: the generator must have a reference to the {@code Region} in which it will generate ripples.
     * <p> - Position: the generator must know where you want to generate the ripple, so x and y coordinates ({@link #positionFunction}.
     * <p> - Ripple type/shape: the generator must know what kind of ripple you want to generate (circle, rectangle...), {@link #rippleSupplier}.
     * <p> - Clip/Ripple Bounds: the generator should know the bounds beyond which the ripple must not go. In JavaFX to achieve such behavior there is the clip concept, {@link #clipSupplier}.
     *
     * @param <T> the types of ripple accepted by the generator
     */
    abstract class GeneratorBase<T extends Ripple> extends Region implements Generator<T> {
        protected final ObservableStack<Animation> animationsStack;
        protected final Region region;
        protected Function<MouseEvent, PositionBean> positionFunction;
        protected Function<Region, Shape> clipSupplier;
        protected Supplier<T> rippleSupplier;

        public GeneratorBase(final Region node) {
            region = Asserts.notNull(node);
            getStyleClass().setAll("ripple-generator");
            setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
            prefWidthProperty().bind(node.widthProperty());
            prefHeightProperty().bind(node.heightProperty());

            animationsStack = new ObservableStack<>();
            animationsStack.addListener((ListChangeListener) change -> {
                if (change.getList().isEmpty()) {
                    fireGeneratorEvent(GeneratorEvent.ANIMATION_FINISHED_EVENT);
                }
            });

            setManaged(false);

        }

        /**{@inheritDoc}*/
        public final Region getRegion() {
            return region;
        }

        /**{@inheritDoc}*/
        public final void setClipSupplier(Function<Region, Shape> supplier) {
            this.clipSupplier = supplier;
        }

        /**{@inheritDoc}*/
        public final void setRippleSupplier(Supplier<T> supplier) {
            this.rippleSupplier = supplier;
        }

        /**{@inheritDoc}*/
        public final void setRipplePositionFunction(Function<MouseEvent, PositionBean> function) {
            this.positionFunction = function;
        }

        /**{@inheritDoc}*/
        public final Function<Region, Shape> getClipSupplier() {
            return clipSupplier;
        }

        public final Shape getClipShape() {
            return getClipSupplier().apply(region);
        }

        /**{@inheritDoc}*/
        public final Supplier<T> getRippleSupplier() {
            return rippleSupplier;
        }

        /**{@inheritDoc}*/
        public final Function<MouseEvent, PositionBean> getRipplePositionFunction() {
            return positionFunction;
        }

        /**
         * Abstract method. Every generator must provide a way to generate ripples.
         */
        public abstract void generateRipple(MouseEvent event);

        /**
         * Convenience method to fire {@link GeneratorEvent} events.
         */
        public void fireGeneratorEvent(EventType<? extends GeneratorEvent> eventType) {
            fireEvent(new GeneratorEvent(eventType));
        }

        /**
         * Checks if the mouse event coordinates are within the {@link Bounds} of the region.
         */
        protected boolean isWithinBounds(MouseEvent event) {
            return event == null || region.getLayoutBounds().contains(event.getX(), event.getY());
        }

        //@formatter:off

        /**
         * Specifies if the background of the region should be animated too.
         * <p>
         * The animation generally consists in temporarily adding a shape to the generator,
         * set its fill same as the ripple color, and manipulate its opacity with a timeline.
         */
        private BooleanProperty animateBackground;
        public final boolean isAnimateBackground() {return animateBackgroundProperty().get();}
        public final void setAnimateBackground(boolean value) {animateBackgroundProperty().set(value);}
        public final BooleanProperty animateBackgroundProperty() {
            if(animateBackground == null) {
                animateBackground = new SimpleBooleanProperty(this, "animateBackground", true);
            }
            return animateBackground;
        }

        /**
         * Specifies if the {@link DropShadow} effect of the region should be animated too.
         * Mostly used for {@code Buttons}.
         */
        private BooleanProperty animateShadow;
        public final boolean isAnimateShadow() {return animateShadowProperty().get();}
        public final void setAnimateShadow(boolean value) {animateShadowProperty().set(value);}
        public final BooleanProperty animateShadowProperty() {
            if(animateShadow == null) {
                animateShadow = new SimpleBooleanProperty(this, "animateShadow", false);
            }
            return animateShadow;
        }

        /**
         * Specifies if {@link #isWithinBounds(MouseEvent)} should be called before generating the ripple.
         * <p>
         * The purpose of this property is to disable/bypass the bounds check, it may happen in some cases
         * that the check must be disabled to make the generator work properly. An example is the {@link CheckBox}.
         */
        private BooleanProperty checkBounds;
        public final boolean isCheckBounds() {return checkBoundsProperty().get();}
        public final void setCheckBounds(boolean value) {checkBoundsProperty().set(value);}
        public final BooleanProperty checkBoundsProperty() {
            if(checkBounds == null) {
                checkBounds = new SimpleBooleanProperty(this, "checkBounds", true);
            }
            return checkBounds;
        }

        /**
         * Specifies by how many levels the shadow should be increased.
         * For example if the {@link DropShadow} effect is of {@link DepthLevel#LEVEL1} and the
         * offset is set to 2 then the shadow will shift to {@link DepthLevel#LEVEL3},
         * (reverted at the end of the animation of course).
         */
        private IntegerProperty depthLevelOffset;
        public final int getDepthLevelOffset() {return depthLevelOffsetProperty().get();}
        public final void setDepthLevelOffset(int value) {depthLevelOffsetProperty().set(value);}
        public final IntegerProperty depthLevelOffsetProperty() {
            if(depthLevelOffset == null) {
                depthLevelOffset = new SimpleIntegerProperty(this, "depthLevelOffset", 1);
            }
            return depthLevelOffset;
        }

        /**
         * Specifies the action to perform when a {@link GeneratorEvent#ANIMATION_FINISHED_EVENT} is fired.
         */
        private ObjectProperty<EventHandler<GeneratorEvent>> onAnimationFinished;
        public final EventHandler<GeneratorEvent> getOnAnimationFinished() {return onAnimationFinishedProperty().get();}
        public final void setOnAnimationFinished(EventHandler<GeneratorEvent> value) {onAnimationFinishedProperty().set(value);}
        public final ObjectProperty<EventHandler<GeneratorEvent>> onAnimationFinishedProperty() {
            if(onAnimationFinished == null) {
                onAnimationFinished = new SimpleObjectProperty<>(this, "onAnimationFinished") {
                    protected void invalidated() {
                        setEventHandler(GeneratorEvent.ANIMATION_FINISHED_EVENT, get());
                    }
                };
            }
            return onAnimationFinished;
        }

        //@formatter:on

    }

    /**
     * Events class for RippleGenerators.
     * <p>
     * Defines a new EventType:
     * <p>
     * - ANIMATION_FINISHED_EVENT: when the ripple animation has finished this event is fired by the generator. The tricky part,
     * When the ripple animation is finished? For this purpose an {@link ObservableStack} is used. When a ripple is generated its animation should be added
     * to the stack and removed only when the animation is finished. A change listener added to the stack checks if it is empty. When it's empty it means
     * that all ripple animations have ended, at that point the event is fired <p></p>
     * <p>
     * These events are automatically fired by the generator so they should not be fired by users.
     */
    final class GeneratorEvent extends Event {

        public static final EventType<GeneratorEvent> ANIMATION_FINISHED_EVENT = new EventType<>(Event.ANY, "ANIMATION_FINISHED_EVENT");
        public static final EventType<GeneratorEvent> ANY = ANIMATION_FINISHED_EVENT;

        public GeneratorEvent(EventType<? extends Event> eventType) {
            super(eventType);
        }

    }
}