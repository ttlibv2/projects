package vn.conyeu.javafx.animation.animate;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a rotate out effect on a node
 * <pre>{@literal @}keyframes rotateOut {
 * 	0% {
 * 		transform-origin: center center;
 * 		transform: rotate(0);
 * 		opacity: 1;
 * 	}
 * 	100% {
 * 		transform-origin: center center;
 * 		transform: rotate(200deg);
 * 		opacity: 0;
 * 	}
 * }
 * 
 * </pre>
 */
public class RotateOut extends CachedTimeline {
    /**
     * Create new RotateOut
     * 
     * @param node The node to affect
     */
    public RotateOut(final Node node) {
        super(
            node,
            new Timeline(
                    new KeyFrame(Duration.millis(0),    
                        new KeyValue(node.opacityProperty(), 1, WEB_EASE),
                        new KeyValue(node.rotateProperty(), 0, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(1000),    
                        new KeyValue(node.opacityProperty(), 0, WEB_EASE),
                        new KeyValue(node.rotateProperty(), 200, WEB_EASE)
                    )
                )
                );
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }

    @Override protected void stopping() {
        super.stopping();
        node.setRotate(0); // Restore default value
    }
}