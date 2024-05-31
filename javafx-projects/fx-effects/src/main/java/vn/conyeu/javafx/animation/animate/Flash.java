package vn.conyeu.javafx.animation.animate;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a flash effect on a node
 * <pre>{@literal @}keyframes flash {
 * 	0%, 50%, 100% {opacity: 1;}	
 * 	25%, 75% {opacity: 0;}
 * }
 * 
 * </pre>
 */
public class Flash extends CachedTimeline {
    /**
     * Create new Flash
     * 
     * @param node The node to affect
     */
    public Flash(final Node node) {
        super(
            node,
            new Timeline(
                    new KeyFrame(Duration.millis(0),    new KeyValue(node.opacityProperty(), 1, WEB_EASE)),
                    new KeyFrame(Duration.millis(250),  new KeyValue(node.opacityProperty(), 0, WEB_EASE)),
                    new KeyFrame(Duration.millis(500),  new KeyValue(node.opacityProperty(), 1, WEB_EASE)),
                    new KeyFrame(Duration.millis(750),  new KeyValue(node.opacityProperty(), 0, WEB_EASE)),
                    new KeyFrame(Duration.millis(1000),  new KeyValue(node.opacityProperty(), 1, WEB_EASE))
                )
                
            );
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }
}