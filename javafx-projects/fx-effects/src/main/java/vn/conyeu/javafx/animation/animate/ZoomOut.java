package vn.conyeu.javafx.animation.animate;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

public class ZoomOut extends CachedTimeline {


    public ZoomOut(final Node node) {
        this(node, Duration.millis(100), 0.3);
    }

    public ZoomOut(final Node node, Duration duration) {
        this(node, duration, 0.3);
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
    public ZoomOut(final Node node, Duration duration, double endValue) {
        super(node,  new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(node.scaleXProperty(), 1, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), 1, WEB_EASE),
                        new KeyValue(node.scaleZProperty(), 1, WEB_EASE)
                ),
                new KeyFrame(duration,
                        new KeyValue(node.scaleXProperty(), endValue, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), endValue, WEB_EASE),
                        new KeyValue(node.scaleZProperty(), endValue, WEB_EASE)
                )
                )
        );
    }

    @Override
    protected void stopping() {
        super.stopping();
        node.setScaleX(1);
        node.setScaleY(1);
        node.setScaleZ(1);
    }
}