package vn.conyeu.javafx.animation.utils;

import javafx.animation.*;
import javafx.beans.binding.BooleanExpression;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.stage.Window;
import javafx.util.Duration;
import vn.conyeu.commons.function.Callable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * <p>
 * This builder, designed with fluent api, allows you to create simple and complex animations with just a few lines of code.</p>
 * <p>
 * The builder keeps the reference of the "main" animation (depending on the subclass can be ParallelTransition or SequentialTransition, in
 * the AbstractBuilder the type is a generic {@link Animation}), and defines and abstract method that subclasses must implement
 * to properly add animations to the "main".</p>
 */
public abstract class AbstractBuilder<S extends AbstractBuilder<S>> {
    protected Animation animation;
    private final S self = (S) this;

    /**
     * Adds the given animation to the "main" animation.
     */
    protected abstract void addAnimation(Animation animation);

    /**
     * @return the "main" animation instance
     */
    public abstract Animation getAnimation();

    protected void init(Animation animation) {
        this.animation = animation;
    }

    /**
     * Adds the given animation to the "main" animation by calling {@link #addAnimation(Animation)}.
     */
    public S add(Animation animation) {
        addAnimation(animation);
        return self;
    }

    public S addPauseBuilder(Consumer<PauseBuilder> supplier) {
        PauseBuilder pauseBuilder = PauseBuilder.build();
        supplier.accept(pauseBuilder);
        return add(pauseBuilder.getAnimation());
    }

    /**
     * Sets the given onFinished action to the given animation and then adds it to the
     * "main" animation by calling {@link #addAnimation(Animation)}.
     */
    public S add(Animation animation, EventHandler<ActionEvent> onFinished) {
        animation.setOnFinished(onFinished);
        addAnimation(animation);
        return self;
    }

    /**
     * Gets the animation from the supplier and adds it to the "main" animation by calling {@link #addAnimation(Animation)}.
     */
    public S add(Supplier<Animation> animationSupplier) {
        addAnimation(animationSupplier.get());
        return self;
    }

    /**
     * Gets the animation from the supplier, sets the given onFinished action to it and then adds it to the
     * "main" animation by calling {@link #addAnimation(Animation)}.
     */
    public S add(Supplier<Animation> animationSupplier, EventHandler<ActionEvent> onFinished) {
        Animation animation = animationSupplier.get();
        animation.setOnFinished(onFinished);
        addAnimation(animation);
        return self;
    }

    /**
     * Builds a {@link Timeline} with the given frames and adds it to the "main" animation by calling {@link #addAnimation(Animation)}.
     */
    public S add(KeyFrame... keyFrames) {
        addAnimation(new Timeline(keyFrames));
        return self;
    }

    /**
     * For each given window builds and adds an animation that hides the window by fading it out.
     *
     * @param duration the fade animation speed
     */
    public S hide(Duration duration, Window... windows) {
        for (Window window : windows) {
            addAnimation(TimelineBuilder.build()
                    .add(KeyFrames.of(duration, window.opacityProperty(), 0))
                    .getAnimation());
        }
        return self;
    }

    public S hide(double millis, Window... windows) {
        return hide(Duration.millis(millis), windows);
    }

    /**
     * For each given node builds and adds an animation that hides the node by fading it out.
     *
     * @param duration the fade animation speed
     */
    public S hide(Duration duration, Node... nodes) {
        for (Node node : nodes) {
            addAnimation(TimelineBuilder.build().hide(duration, node).getAnimation());
        }
        return self;
    }

    /**
     * Calls {@link #hide(Duration, Node...)} by converting the given millis value
     * with {@link Duration#millis(double)}.
     */
    public S hide(double millis, Node... nodes) {
        return hide(Duration.millis(millis), nodes);
    }

    /**
     * Creates and adds a fade out animation for each given {@link AnimationsData}.
     */
    public final S hide(AnimationsData... data) {
        for (AnimationsData animData : data) {
            addAnimation(TimelineBuilder.build().hide(animData).getAnimation());
        }
        return self;
    }

    /**
     * For each given window builds and adds an animation that shows the window by fading it in.
     *
     * @param duration the fade animation speed
     */
    public S show(Duration duration, Window... windows) {
        for (Window window : windows) {
            addAnimation(TimelineBuilder.build().show(duration, window).getAnimation());
        }
        return self;
    }

    /**
     * Calls {@link #show(Duration, Window...)} by converting the given millis value
     * with {@link Duration#millis(double)}.
     */
    public S show(double millis, Window... windows) {
        return show(Duration.millis(millis), windows);
    }

    /**
     * For each given node builds and adds an animation that shows the node by fading it in.
     *
     * @param duration the fade animation speed
     */
    public S show(Duration duration, Node... nodes) {
        for (Node node : nodes) {
            addAnimation(TimelineBuilder.build().show(duration, node).getAnimation());
        }
        return self;
    }

    /**
     * Calls {@link #show(Duration, Node...)} by converting the given millis value
     * with {@link Duration#millis(double)}.
     */
    public S show(double millis, Node... nodes) {
        return show(Duration.millis(millis), nodes);
    }

    /**
     * Creates and adds a fade in animation for each given {@link AnimationsData}.
     */
    public final S show(AnimationsData... data) {
        for (AnimationsData animData : data) {
            addAnimation(TimelineBuilder.build().show(animData).getAnimation());
        }
        return self;
    }

    /**
     * Sets the action to perform when the "main" animation ends.
     */
    public S setOnFinished(EventHandler<ActionEvent> onFinished) {
        animation.setOnFinished(onFinished);
        return self;
    }

    /**
     * Sets the "main" animation cycle count.
     */
    public S setCycleCount(int cycleCount) {
        animation.setCycleCount(cycleCount);
        return self;
    }

    /**
     * Sets the "main" animation delay.
     */
    public S setDelay(Duration delay) {
        animation.setDelay(delay);
        return self;
    }

    /**
     * Sets the "main" animation delay.
     */
    public S setDelay(double millis) {
        animation.setDelay(Duration.millis(millis));
        return self;
    }

    /**
     * Sets the "main" animation rate/speed.
     */
    public S setRate(double rate) {
        animation.setRate(rate);
        return self;
    }

    public static SequentialBuilder sequentialBuilder() {
        return new SequentialBuilder();
    }

    public static class SequentialBuilder extends AbstractBuilder {
        //================================================================================
        // Properties
        //================================================================================
        private final SequentialTransition sequentialTransition = new SequentialTransition();

        //================================================================================
        // Constructors
        //================================================================================
        public SequentialBuilder() {
            init(sequentialTransition);
        }

        //================================================================================
        // Static Methods
        //================================================================================

        /**
         * @return a new SequentialBuilder instance. Equivalent to calling the constructor,
         * it's just a way to omit the new keyword
         */
        public static SequentialBuilder build() {
            return new SequentialBuilder();
        }

        //================================================================================
        // Override Methods
        //================================================================================
        @Override
        protected void addAnimation(Animation animation) {
            sequentialTransition.getChildren().add(animation);
        }

        @Override
        public SequentialTransition getAnimation() {
            return sequentialTransition;
        }

    }

    public static ParallelBuilder parallelBuilder() {
        return new ParallelBuilder();
    }

    /**
     * Implementation of {@link AbstractBuilder} that uses a {@link ParallelTransition} as "main" animation.
     */
    public static class ParallelBuilder extends AbstractBuilder {
        //================================================================================
        // Properties
        //================================================================================
        private final ParallelTransition parallelTransition = new ParallelTransition();

        //================================================================================
        // Constructors
        //================================================================================
        public ParallelBuilder() {
            init(parallelTransition);
        }

        //================================================================================
        // Static Methods
        //================================================================================

        /**
         * @return a new ParallelBuilder instance. Equivalent to calling the constructor,
         * it's just a way to omit the new keyword
         */
        public static ParallelBuilder build() {
            return new ParallelBuilder();
        }

        //================================================================================
        // Override Methods
        //================================================================================
        @Override
        protected void addAnimation(Animation animation) {
            parallelTransition.getChildren().add(animation);
        }

        @Override
        public ParallelTransition getAnimation() {
            return parallelTransition;
        }
    }

    public static PauseBuilder pauseBuilder() {
        return PauseBuilder.build();
    }

    /**
     * Builder class to easily create a {@link PauseTransition} with fluent api.
     */
    public static class PauseBuilder {
        //================================================================================
        // Properties
        //================================================================================
        private final PauseTransition pauseTransition = new PauseTransition();

        //================================================================================
        // Static Methods
        //================================================================================

        /**
         * @return a new PauseBuilder instance. Equivalent to calling the constructor,
         * it's just a way to omit the new keyword
         */
        public static PauseBuilder build() {
            return new PauseBuilder();
        }

        //================================================================================
        // Methods
        //================================================================================

        public PauseBuilder setDelay(Duration duration) {
            pauseTransition.setDelay(duration);
            return this;
        }

        public PauseBuilder setDelay(double millis) {
            pauseTransition.setDelay(Duration.millis(millis));
            return this;
        }

        /**
         * Sets the pause transition duration.
         */
        public PauseBuilder setDuration(Duration duration) {
            pauseTransition.setDuration(duration);
            return this;
        }

        /**
         * Calls {@link #setDuration(Duration)} by converting the given millis value
         * with {@link Duration#millis(double)}.
         */
        public PauseBuilder setDuration(double millis) {
            pauseTransition.setDuration(Duration.millis(millis));
            return this;
        }

        /**
         * Sets the action to perform when the pause transition ends.
         */
        public PauseBuilder setOnFinished(EventHandler<ActionEvent> onFinished) {
            pauseTransition.setOnFinished(onFinished);
            return this;
        }

        /**
         * @return the instance of the PauseTransition
         */
        public PauseTransition getAnimation() {
            return pauseTransition;
        }

        /**
         * This method can be considered an utility.
         * <p></p>
         * A {@link PauseTransition} with the previously set duration runs while the given boolean boolean expression
         * is false. When the expression is evaluated and it is false the given retryAction is run and the transition
         * is restarted. When it ends the expression is re-evaluated. When the expression becomes true the onSuccessAction is run.
         * <p></p>
         * So you have a {@link PauseTransition} that runs every tot unit of time and stops only when the given expression is true.
         *
         * @param booleanExpression the expression to check at a fixed time rate
         * @param retryAction       the action to perform when the expression is false
         * @param onSuccessAction   the action to perform when the expression is true
         */
        public void runWhile(BooleanExpression booleanExpression, Runnable retryAction, Runnable onSuccessAction) {
            setOnFinished(event -> {
                if (!booleanExpression.get()) {
                    retryAction.run();
                    getAnimation().playFromStart();
                } else {
                    onSuccessAction.run();
                }
            });
            getAnimation().play();
        }

        /**
         * Same method as {@link #runWhile(BooleanExpression, Runnable, Runnable)} but instead of running
         * until the given expression is true, it is limited to a maximum number of retries.
         *
         * @param maxRetryCount the max number of times the transition can be restarted
         */
        public void runWhile(BooleanExpression booleanExpression, Runnable retryAction, Runnable onSuccessAction, int maxRetryCount) {
            AtomicInteger retryCount = new AtomicInteger(0);
            setOnFinished(event -> {
                if (!booleanExpression.get() && retryCount.get() < maxRetryCount) {
                    retryCount.getAndIncrement();
                    retryAction.run();
                    getAnimation().playFromStart();
                } else {
                    onSuccessAction.run();
                }
            });
            getAnimation().play();
        }
    }
}