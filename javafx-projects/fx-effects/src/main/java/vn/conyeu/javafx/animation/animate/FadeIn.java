package vn.conyeu.javafx.animation.animate;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a fade in effect on a node
 * <pre>{@literal @}keyframes fadeIn {
 * 	0% {opacity: 0;}	
 * 	100% {opacity: 1;}
 * }
 * 
 * </pre>
 */
public class FadeIn extends CachedTimeline {
    /**
     * Create new FadeIn
     * 
     * @param node The node to affect
     */
    public FadeIn(final Node node) {
        super(
            node,
            new Timeline(
                    new KeyFrame(Duration.millis(0),    new KeyValue(node.opacityProperty(), 0, WEB_EASE)),
                    new KeyFrame(Duration.millis(1000),  new KeyValue(node.opacityProperty(), 1, WEB_EASE))
                )
                
            );
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }
}