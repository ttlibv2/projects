package vn.conyeu.javafx.animation.utils;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.text.Text;
import javafx.util.Duration;
import vn.conyeu.javafx.animation.animate.*;

import java.util.Objects;

/**
 * Utility class to easily build animations of any sort
 */
public final class Animations {

    public static TimelineBuilder timelineBuilder() {
        return TimelineBuilder.build();
    }

    public static Timeline buildTimeline(KeyFrame... frames) {
        return timelineBuilder().add(frames).getAnimation();
    }

    public static AbstractBuilder.ParallelBuilder parallelBuilder() {
        return AbstractBuilder.ParallelBuilder.build();
    }

    public static AbstractBuilder.SequentialBuilder sequentialBuilder() {
        return AbstractBuilder.SequentialBuilder.build();
    }

    public static AbstractBuilder.PauseBuilder pauseBuilder() {
        return AbstractBuilder.PauseBuilder.build();
    }


    /**
     * Temporarily disables the given node for the specified duration.
     */
    public static void disableTemporarily(Duration duration, Node node) {
        node.setDisable(true);
        AbstractBuilder.pauseBuilder()
                .setOnFinished(event -> node.setDisable(false))
                .setDuration(duration).getAnimation().play();
    }

    /**
     * Calls {@link #disableTemporarily(Duration, Node)} by converting the given millis value
     * with {@link Duration#millis(double)}.
     */
    public static void disableTemporarily(double millis, Node node) {
        disableTemporarily(Duration.millis(millis), node);
    }

    /**
     * Executes the given onFinished action after the specified duration of time.
     * (Uses a PauseTransition)
     */
    public static void executeLater(Duration duration, EventHandler<ActionEvent> onFinished) {
        AbstractBuilder.pauseBuilder().setDuration(duration).setOnFinished(onFinished).getAnimation().play();
    }

    /**
     * Calls {@link #executeLater(Duration, EventHandler)} by converting the given millis value
     * with {@link Duration#millis(double)}.
     */
    public static void executeLater(double millis, EventHandler<ActionEvent> onFinished) {
        executeLater(Duration.millis(millis), onFinished);
    }

    /**
     * Sets the text of the given {@link Labeled} with a fade out/fade in transition.
     *
     * @param labeled  the labeled control to change the text to
     * @param duration the fade in and fade out speed
     * @param nexText  the new text to set
     * @return an instance of {@link AbstractBuilder}
     */
    public static AbstractBuilder transitionText(Labeled labeled, Duration duration, String nexText) {
        return AbstractBuilder.sequentialBuilder()
                .hide(AnimationsData.of(labeled, duration, event -> labeled.setText(nexText)))
                .show(AnimationsData.of(labeled, duration));
    }

    /**
     * Calls {@link #transitionText(Labeled, Duration, String)} by converting the given millis value
     * with {@link Duration#millis(double)}.
     */
    public static AbstractBuilder transitionText(Labeled labeled, double millis, String nexText) {
        return transitionText(labeled, Duration.millis(millis), nexText);
    }

    /**
     * Sets the text of the given {@link Text} with a fade out/fade in transition.
     *
     * @param text     the text control to change the text to
     * @param duration the fade in and fade out speed
     * @param nexText  the new text to set
     * @return an instance of {@link AbstractBuilder}
     */
    public static AbstractBuilder transitionText(Text text, Duration duration, String nexText) {
        return AbstractBuilder.sequentialBuilder()
                .hide(AnimationsData.of(text, duration, event -> text.setText(nexText)))
                .show(AnimationsData.of(text, duration));
    }

    /**
     * Calls {@link #transitionText(Text, Duration, String)} by converting the given millis value
     * with {@link Duration#millis(double)}.
     */
    public static AbstractBuilder transitionText(Text text, double millis, String nexText) {
        return transitionText(text, Duration.millis(millis), nexText);
    }

    /**
     * @return true if the given animation status is RUNNING, otherwise false
     */
    public static boolean isPlaying(Animation animation) {
        return animation.getStatus() == Animation.Status.RUNNING;
    }

    /**
     * @return true if the given animation status is PAUSED, otherwise false
     */
    public static boolean isPaused(Animation animation) {
        return animation.getStatus() == Animation.Status.PAUSED;
    }

    public static boolean isStopped(Animation animation) {
        return animation.getStatus() == Animation.Status.STOPPED;
    }


    /**
     * Allows to perform a given action as a {@link Runnable} as soon as the given {@link Animation} reaches the given
     * {@link Animation.Status}. This is simply done by attaching an {@link InvalidationListener} to the animation's
     * {@link Animation#statusProperty()}.
     * <p></p>
     * <b>Trivia</b>
     * <p></p>
     * {@link Animation}s have a feature that allows user to specify an action to perform as soon as it ends, I'm talking
     * about the {@link Animation#onFinishedProperty()}. There is an issue though. If the animation is stopped,
     * {@link Animation#stop()}, the action won't trigger. 'Stop' and 'Finish' are two different states, but JavaFX devs
     * didn't make such distinction explicit, in fact when an animation ends, its status property will be set to
     * {@link Animation.Status#STOPPED}.
     * <p>
     * It may be useful in some occasions to perform an action once the animation stops, rather than just on finish.
     * And so I created this generic method to fill this gap, working on any state you need.
     *
     * @param oneShot specifies whether the listener should be removed after the first execution
     */
    public static void onStatus(Animation animation, Animation.Status status, Runnable action, boolean oneShot) {
        if (animation == null) return;
        InvalidationListener l = new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if (animation.getStatus() == status) {
                    action.run();
                    if (oneShot) animation.statusProperty().removeListener(this);
                }
            }
        };
        animation.statusProperty().addListener(l);
    }

    /**
     * Convenience method for {@link #onStatus(Animation, Animation.Status, Runnable, boolean)}, performs the given
     * action on {@link Animation.Status#PAUSED}.
     */
    public static void onPaused(Animation animation, Runnable action, boolean oneShot) {
        onStatus(animation, Animation.Status.PAUSED, action, oneShot);
    }

    /**
     * Convenience method for {@link #onStatus(Animation, Animation.Status, Runnable, boolean)}, performs the given
     * action on {@link Animation.Status#STOPPED}.
     */
    public static void onStopped(Animation animation, Runnable action, boolean oneShot) {
        onStatus(animation, Animation.Status.STOPPED, action, oneShot);
    }

    //==================== Attention seekers =================

    public static Bounce bounce(Node node) {
        return new Bounce(node);
    }

    public static BounceIn bounceIn(Node node) {
        return new BounceIn(node);
    }

    public static BounceInDown bounceInDown(Node node) {
        return new BounceInDown(node);
    }

    public static BounceInLeft bounceInLeft(Node node) {
        return new BounceInLeft(node);
    }

    public static BounceInRight bounceInRight(Node node) {
        return new BounceInRight(node);
    }

    public static BounceInUp bounceInUp(Node node) {
        return new BounceInUp(node);
    }

    public static BounceOut bounceOut(Node node) {
        return new BounceOut(node);
    }

    public static BounceOutDown bounceOutDown(Node node) {
        return new BounceOutDown(node);
    }

    public static BounceOutLeft bounceOutLeft(Node node) {
        return new BounceOutLeft(node);
    }

    public static BounceOutRight bounceOutRight(Node node) {
        return new BounceOutRight(node);
    }

    public static BounceOutUp bounceOutUp(Node node) {
        return new BounceOutUp(node);
    }

    public static FadeIn fadeIn(Node node) {
        return new FadeIn(node);
    }

    public static FadeInDown fadeInDown(Node node) {
        return new FadeInDown(node);
    }

    public static FadeInUp fadeInUp(Node node) {
        return new FadeInUp(node);
    }

    public static FadeInLeft fadeInLeft(Node node) {
        return new FadeInLeft(node);
    }

    public static FadeInRight fadeInRight(Node node) {
        return new FadeInRight(node);
    }

    public static FadeInUpBig fadeInUpBig(Node node) {
        return new FadeInUpBig(node);
    }

    public static FadeInLeftBig fadeInLeftBig(Node node) {
        return new FadeInLeftBig(node);
    }

    public static FadeInRightBig fadeInRightBig(Node node) {
        return new FadeInRightBig(node);
    }

    public static FadeInDownBig fadeInDownBig(Node node) {
        return new FadeInDownBig(node);
    }
    /**
     * Gradually decreases the opacity of the node from 1 to 0,
     * making it disappear from the scene with a fading-out effect.
     *
     * @param node     The node to be animated.
     */
    public static FadeOut fadeOut(Node node) {
        return new FadeOut(node);
    }

    /**
     * Gradually decreases the opacity of the node from 1 to 0,
     * making it disappear from the scene with a fading-out effect.
     *
     * @param node     The node to be animated.
     * @param duration The animation duration.
     */
    public static FadeOut fadeOut(Node node, Duration duration) {
        return new FadeOut(node, duration);
    }

    public static FadeOutDown fadeOutDown(Node node) {
        return new FadeOutDown(node);
    }

    public static FadeOutUp fadeOutUp(Node node) {
        return new FadeOutUp(node);
    }

    public static FadeOutLeft fadeOutLeft(Node node) {
        return new FadeOutLeft(node);
    }

    public static FadeOutRight fadeOutRight(Node node) {
        return new FadeOutRight(node);
    }

    public static FadeOutDownBig fadeOutDownBig(Node node) {
        return new FadeOutDownBig(node);
    }

    public static FadeOutUpBig fadeOutUpBig(Node node) {
        return new FadeOutUpBig(node);
    }

    public static FadeOutLeftBig fadeOutLeftBig(Node node) {
        return new FadeOutLeftBig(node);
    }

    public static FadeOutRightBig fadeOutRightBig(Node node) {
        return new FadeOutRightBig(node);
    }

    /**
     * See {@link #zoomOut(Node, Duration, double)}.
     */
    public static ZoomOut zoomOut(Node node, Duration duration) {
        return zoomOut(node, duration, 0.3);
    }

    /**
     * Reduces the scale of the node, creating a shrinking effect that starts from
     * its original size and gradually zooms out to a smaller size, emphasizing
     * the node’s exit.
     *
     * @param node     The node to be animated.
     * @param duration The animation duration.
     * @param endValue The target zoom value.
     */
    public static ZoomOut zoomOut(Node node, Duration duration, double endValue) {
        Objects.requireNonNull(node, "Node cannot be null!");
        Objects.requireNonNull(duration, "Duration cannot be null!");
        return new ZoomOut(node, duration, endValue);
    }

    /**
     * See {@link #zoomIn(Node, Duration, double)}.
     */
    public static ZoomIn zoomIn(Node node, Duration duration) {
        return zoomIn(node, duration, 0.3);
    }

    /**
     * Increases the scale of the node, starting from a smaller size and gradually
     * zooming it to the regular size, emphasizing the node’s entrance.
     *
     * @param node       The node to be animated.
     * @param duration   The animation duration.
     * @param startValue The initial zoom value.
     */
    public static ZoomIn zoomIn(Node node, Duration duration, double startValue) {
        Objects.requireNonNull(node, "Node cannot be null!");
        Objects.requireNonNull(duration, "Duration cannot be null!");
        return new ZoomIn(node, duration, startValue);
    }

    /**
     * Applies an animated effect to the node, causing it to slide into view
     * from the top side.
     *
     * @param node     The node to be animated.
     * @param duration The animation duration.
     */
    public static SlideInDown slideInDown(Node node, Duration duration) {
        Objects.requireNonNull(node, "Node cannot be null!");
        Objects.requireNonNull(duration, "Duration cannot be null!");
        return new SlideInDown(node, duration);
    }

    /**
     * Applies an animated effect to the node, causing it to slide out of view
     * through the bottom side.
     *
     * @param node     The node to be animated.
     * @param duration The animation duration.
     */
    public static SlideOutDown slideOutDown(Node node, Duration duration) {
        Objects.requireNonNull(node, "Node cannot be null!");
        Objects.requireNonNull(duration, "Duration cannot be null!");
        return new SlideOutDown(node, duration);
    }

    /**
     * Applies an animated effect to the node, causing it to slide into view
     * from the left side.
     *
     * @param node     The node to be animated.
     * @param duration The animation duration.
     */
    public static SlideInLeft slideInLeft(Node node, Duration duration) {
        Objects.requireNonNull(node, "Node cannot be null!");
        Objects.requireNonNull(duration, "Duration cannot be null!");
        return new SlideInLeft(node, duration);
    }

    /**
     * Applies an animated effect to the node, causing it to slide out of view
     * through the left side.
     *
     * @param node     The node to be animated.
     * @param duration The animation duration.
     */
    public static SlideOutLeft slideOutLeft(Node node, Duration duration) {
        Objects.requireNonNull(node, "Node cannot be null!");
        Objects.requireNonNull(duration, "Duration cannot be null!");
        return new SlideOutLeft(node, duration);
    }

    /**
     * Applies an animated effect to the node, causing it to slide into view
     * from the right side.
     *
     * @param node     The node to be animated.
     * @param duration The animation duration.
     */
    public static SlideInRight slideInRight(Node node, Duration duration) {
        Objects.requireNonNull(node, "Node cannot be null!");
        Objects.requireNonNull(duration, "Duration cannot be null!");
        return new SlideInRight(node, duration);
    }

    /**
     * Applies an animated effect to the node, causing it to slide out of view
     * through the right side.
     *
     * @param node     The node to be animated.
     * @param duration The animation duration.
     */
    public static SlideOutRight slideOutRight(Node node, Duration duration) {
        Objects.requireNonNull(node, "Node cannot be null!");
        Objects.requireNonNull(duration, "Duration cannot be null!");
        return new SlideOutRight(node, duration);
    }

    /**
     * Applies an animated effect to the node, causing it to slide into view
     * from the bottom side.
     *
     * @param node     The node to be animated.
     * @param duration The animation duration.
     */
    public static SlideInUp slideInUp(Node node, Duration duration) {
        Objects.requireNonNull(node, "Node cannot be null!");
        Objects.requireNonNull(duration, "Duration cannot be null!");
        return new SlideInUp(node, duration);
    }

    /**
     * Applies an animated effect to the node, causing it to slide out of view
     * through the top side.
     *
     * @param node     The node to be animated.
     * @param duration The animation duration.
     */
    public static SlideOutUp slideOutUp(Node node, Duration duration) {
        Objects.requireNonNull(node, "Node cannot be null!");
        Objects.requireNonNull(duration, "Duration cannot be null!");
        return new SlideOutUp(node, duration);
    }
}