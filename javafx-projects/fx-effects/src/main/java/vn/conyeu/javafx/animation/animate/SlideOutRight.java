package vn.conyeu.javafx.animation.animate;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

public class SlideOutRight extends CachedTimeline {

    public SlideOutRight(Node node) {
        this(node, Duration.seconds(1));
    }

    public SlideOutRight(Node node, Duration duration) {
        super(node, duration, new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(node.translateXProperty(), 0, EASE)
                ),
                new KeyFrame(duration,
                        new KeyValue(node.translateXProperty(), node.getBoundsInParent().getWidth(), EASE)
                )
        ));
    }

    @Override
    protected void stopping() {
        super.stopping();
        node.setTranslateX(0);
    }

}