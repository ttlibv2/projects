package vn.conyeu.javafx.animation.animate;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

public class SlideOutUp extends CachedTimeline {

    public SlideOutUp(Node node) {
        this(node, Duration.seconds(1));
    }

    public SlideOutUp(Node node, Duration duration) {
        super(node, duration, new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(node.translateYProperty(), 0, EASE)
                ),
                new KeyFrame(duration,
                        new KeyValue(node.translateYProperty(), -node.getBoundsInParent().getHeight(), EASE)
                )
        ));
    }

    @Override
    protected void stopping() {
        super.stopping();
        node.setTranslateY(0);
    }

}