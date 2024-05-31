package vn.conyeu.javafx.animation.animate;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a bounce out effect on a node
 * <pre>{@literal @}keyframes bounceOut {
 * 	0% {
 * 		transform: scale(1);
 * 	}
 * 	25% {
 * 		transform: scale(.95);
 * 	}
 * 	50% {
 * 		opacity: 1;
 * 		transform: scale(1.1);
 * 	}
 * 	100% {
 * 		opacity: 0;
 * 		transform: scale(.3);
 * 	}	
 * }
 * </pre>
 */
public class BounceOut extends CachedTimeline {
    /**
     * Create new BounceOut
     * 
     * @param node The node to affect
     */
    public BounceOut(final Node node) {
        super(
            node,
            new Timeline(
                    new KeyFrame(Duration.millis(0),    
                        new KeyValue(node.scaleXProperty(), 1, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), 1, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(250),    
                        new KeyValue(node.scaleXProperty(), 0.95, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), 0.95, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(500),    
                        new KeyValue(node.opacityProperty(), 1, WEB_EASE),
                        new KeyValue(node.scaleXProperty(), 1.1, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), 1.1, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(1000),    
                        new KeyValue(node.opacityProperty(), 0, WEB_EASE),
                        new KeyValue(node.scaleXProperty(), 0.3, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), 0.3, WEB_EASE)
                    )
                )
                );
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }
}