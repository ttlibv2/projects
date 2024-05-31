package vn.conyeu.javafx.animation.animate;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

public class ZoomIn extends CachedTimeline {

    public ZoomIn(Node node) {
        this(node,  Duration.millis(100), 0.3);
    }

    public ZoomIn(Node node, Duration duration) {
        this(node, duration, 0.3);
    }

    public ZoomIn(Node node, Duration duration, double startValue) {
        super(node, new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(node.scaleXProperty(), startValue, EASE),
                        new KeyValue(node.scaleYProperty(), startValue, EASE),
                        new KeyValue(node.scaleZProperty(), startValue, EASE)
                ),
                new KeyFrame(duration,
                        new KeyValue(node.scaleXProperty(), 1, EASE),
                        new KeyValue(node.scaleYProperty(), 1, EASE),
                        new KeyValue(node.scaleZProperty(), 1, EASE)
                )
        ));
    }
}