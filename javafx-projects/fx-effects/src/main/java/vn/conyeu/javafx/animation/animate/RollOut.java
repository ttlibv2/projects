package vn.conyeu.javafx.animation.animate;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a rollout right effect on a node
 * <pre>{@literal @}keyframes rollOut {
 *     0% {
 * 		opacity: 1;
 * 		transform: translateX(0px) rotate(0deg);
 * 	}
 *     100% {
 * 		opacity: 0;
 * 		transform: translateX(100%) rotate(120deg);
 * 	}
 * }
 * 
 * </pre>
 */
public class RollOut extends CachedTimeline {
    /**
     * Create new RollOut
     * 
     * @param node The node to affect
     */
    public RollOut(final Node node) {
        super(node, null);
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }

    @Override protected void starting() {
        super.starting();
        timeline = new Timeline(
                new KeyFrame(Duration.millis(0),    
                    new KeyValue(node.opacityProperty(), 1, WEB_EASE),
                    new KeyValue(node.translateXProperty(), 0, WEB_EASE),
                    new KeyValue(node.rotateProperty(), 0, WEB_EASE)
                ),
                new KeyFrame(Duration.millis(1000),    
                    new KeyValue(node.opacityProperty(), 0, WEB_EASE),
                    new KeyValue(node.translateXProperty(), node.getBoundsInLocal().getWidth(), WEB_EASE),
                    new KeyValue(node.rotateProperty(), 120, WEB_EASE)
                )
            )
            ;
    }

    @Override protected void stopping() {
        super.stopping();
        node.setTranslateX(0); // restore default
        node.setRotate(0); // restore default
    }
}