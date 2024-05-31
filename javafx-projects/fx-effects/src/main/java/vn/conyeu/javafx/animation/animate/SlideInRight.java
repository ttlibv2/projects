package vn.conyeu.javafx.animation.animate;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

public class SlideInRight extends CachedTimeline {

    public SlideInRight(Node node) {
        this(node, Duration.seconds(1));
    }

    public SlideInRight(Node node, Duration duration) {
        super(node, duration, new Timeline( new KeyFrame(Duration.ZERO,
                new KeyValue(node.translateXProperty(), node.getBoundsInParent().getWidth(), EASE)
        ),
                new KeyFrame(duration,
                        new KeyValue(node.translateXProperty(), 0, EASE)
                )));
    }

    @Override
    protected void stopping() {
        super.stopping();
        node.setTranslateX(0);
    }

}