package vn.conyeu.javafx.animation.utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.stage.Window;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.List;

/**
 * Builder class to easily create a {@link Timeline} with fluent api.
 */
public class TimelineBuilder {
    final Timeline timeline = new Timeline();

    /**
     * @return a new TimelineBuilder instance. Equivalent to calling the constructor,
     * it's just a way to omit the new keyword
     */
    public static TimelineBuilder build() {
        return new TimelineBuilder();
    }

    public TimelineBuilder add(List<KeyFrame> keyFrames) {
        timeline.getKeyFrames().addAll(keyFrames);
        return this;
    }

    /**
     * Adds the specified KeyFrames to the timeline.
     */
    public TimelineBuilder add(KeyFrame... keyFrames) {
        timeline.getKeyFrames().addAll(Arrays.asList(keyFrames));
        return this;
    }

    /**
     * Builds a KeyFrame to hide the given Window by fading it out.
     *
     * @param duration the fade animation speed
     */
    public TimelineBuilder hide(Duration duration, Window window) {
        add(KeyFrames.of(duration, window.opacityProperty(), 0));
        return this;
    }

    /**
     * Calls {@link #hide(Duration, Window)} by converting the given millis value
     * with {@link Duration#millis(double)}.
     */
    public TimelineBuilder hide(double millis, Window window) {
        return hide(Duration.millis(millis), window);
    }

    /**
     * Builds the KeyFrames to hide the given node by fading it out.
     *
     * @param duration the fade animation speed
     */
    public TimelineBuilder hide(Duration duration, Node node) {
        add(
                KeyFrames.of(Duration.ZERO, node.opacityProperty(), 1.0, Interpolators.INTERPOLATOR_V1),
                KeyFrames.of(duration, node.opacityProperty(), 0, Interpolators.INTERPOLATOR_V1)
        );
        return this;
    }


    public TimelineBuilder hide(double millis, Node node) {
        return hide(Duration.millis(millis), node);
    }

    /**
     * Builds the KeyFrames to hide the specified node in the AnimationsData bean, by fading it out.
     */
    public final TimelineBuilder hide(AnimationsData data) {
        add(
                KeyFrames.of(Duration.ZERO, data.node().opacityProperty(), 1.0),
                KeyFrames.of(data.duration(), data.node().opacityProperty(), 0.0)
        );
        setOnFinished(data.onFinished());
        return this;
    }

    /**
     * Builds a KeyFrame to show the given Window by fading it in.
     *
     * @param duration the fade animation speed
     */
    public TimelineBuilder show(Duration duration, Window window) {
        add(KeyFrames.of(duration, window.opacityProperty(), 1.0));
        return this;
    }

    /**
     * Calls {@link #show(Duration, Window)} by converting the given millis value
     * with {@link Duration#millis(double)}.
     */
    public TimelineBuilder show(double millis, Window window) {
        return show(Duration.millis(millis), window);
    }

    /**
     * Builds the KeyFrames to show the given node by fading it in.
     *
     * @param duration the fade animation speed
     */
    public TimelineBuilder show(Duration duration, Node node) {
        add(
                KeyFrames.of(Duration.ZERO, node.opacityProperty(), 0.0),
                KeyFrames.of(duration, node.opacityProperty(), 1.0)
        );
        return this;
    }

    /**
     * Calls {@link #show(Duration, Node)} by converting the given millis value
     * with {@link Duration#millis(double)}.
     */
    public TimelineBuilder show(double millis, Node node) {
        return show(Duration.millis(millis), node);
    }

    /**
     * Creates and adds a fade in animation for each given {@link AnimationsData}.
     */
    public final TimelineBuilder show(AnimationsData data) {
        add(
                KeyFrames.of(Duration.ZERO, data.node().opacityProperty(), 0.0),
                KeyFrames.of(data.duration(), data.node().opacityProperty(), 1.0)
        );
        setOnFinished(data.onFinished());
        return this;
    }

    /**
     * Sets the timeline cycle count.
     */
    public TimelineBuilder setCycleCount(int cycleCount) {
        timeline.setCycleCount(cycleCount);
        return this;
    }

    /**
     * Sets the timeline delay.
     */
    public TimelineBuilder setDelay(Duration delay) {
        timeline.setDelay(delay);
        return this;
    }

    /**
     * Sets the timeline delay.
     */
    public TimelineBuilder setDelay(double millis) {
        timeline.setDelay(Duration.millis(millis));
        return this;
    }

    /**
     * Sets the timeline rate/speed.
     */
    public TimelineBuilder setRate(double rate) {
        timeline.setRate(rate);
        return this;
    }

    /**
     * Sets the action to perform when the timeline ends.
     */
    public TimelineBuilder setOnFinished(EventHandler<ActionEvent> onFinished) {
        timeline.setOnFinished(onFinished);
        return this;
    }

    /**
     * @return the instance of the Timeline
     */
    public Timeline getAnimation() {
        return timeline;
    }


}