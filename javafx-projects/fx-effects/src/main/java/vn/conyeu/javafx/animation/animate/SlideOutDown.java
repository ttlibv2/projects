package vn.conyeu.javafx.animation.animate;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

public class SlideOutDown extends CachedTimeline {

    public SlideOutDown(Node node, Duration duration) {
        super(node, new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(node.translateYProperty(), 0, EASE)
                ),
                new KeyFrame(duration,
                        new KeyValue(node.translateYProperty(), node.getBoundsInParent().getHeight(), EASE)
                )
        ));
    }

    @Override
    protected void stopping() {
        super.stopping();
        node.setTranslateY(0);
    }
}