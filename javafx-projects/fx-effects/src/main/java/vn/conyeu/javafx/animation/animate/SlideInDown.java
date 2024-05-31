package vn.conyeu.javafx.animation.animate;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

public class SlideInDown extends CachedTimeline{

    public SlideInDown(Node node) {
        this(node, Duration.seconds(1));
    }

    public SlideInDown(Node node, Duration duration) {
        super(node, new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(node.translateYProperty(), -node.getBoundsInParent().getHeight(), EASE)
                ),
                new KeyFrame(duration,
                        new KeyValue(node.translateYProperty(), 0, EASE)
                )
        ));
    }

    @Override
    protected void stopping() {
        super.stopping();
        node.setTranslateY(0);
    }
}