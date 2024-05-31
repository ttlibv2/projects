package vn.conyeu.javafx.animation.animate;


import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import vn.conyeu.javafx.animation.utils.Animations;

/**
 * Extension of {@link Transition} to make some methods use fluent API.
 */
public abstract class FluentTransition extends Transition {

    //================================================================================
    // Methods
    //================================================================================

    /**
     * Sets the transition duration.
     */
    public FluentTransition setDuration(Duration duration) {
        this.setCycleDuration(duration);
        return this;
    }

    /**
     * Sets the transition duration in milliseconds.
     */
    public FluentTransition setDuration(double millis) {
        this.setCycleDuration(Duration.millis(millis));
        return this;
    }

    /**
     * Sets the transition's interpolator.
     */
    public FluentTransition setInterpolatorFluent(Interpolator interpolator) {
        this.setInterpolator(interpolator);
        return this;
    }

    /**
     * Sets the transition's delay.
     */
    public FluentTransition setDelayFluent(Duration duration) {
        this.setDelay(duration);
        return this;
    }

    /**
     * Sets the action to perform at the end of the animation.
     */
    public FluentTransition setOnFinishedFluent(EventHandler<ActionEvent> handler) {
        setOnFinished(handler);
        return this;
    }

    /**
     * Sets the action to perform when the animation stops, please make sure to understand the difference
     * between 'end' and 'stop', see {@link Animations#onStatus(Animation, Status, Runnable, boolean)}.
     */
    public FluentTransition setOnStopped(Runnable action, boolean oneshot) {
        Animations.onStopped(this, action, oneshot);
        return this;
    }
}