package vn.conyeu.javafx.animation.animate;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a pulse effect on the given node
 * <pre>@keyframes pulse {
 *     0% { transform: scale(1); }	
 * 	50% { transform: scale(1.1); }
 *     100% { transform: scale(1); }
 * }
 *
 * </pre>
 */
public class Pulse extends CachedTimeline {
    /**
     * Create new Pulse
     * 
     * @param node The node to affect
     */
    public Pulse(final Node node) {
        super(
            node,
            new Timeline(
                    new KeyFrame(Duration.millis(0), 
                        new KeyValue(node.scaleXProperty(), 1, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), 1, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(500), 
                        new KeyValue(node.scaleXProperty(), 1.1, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), 1.1, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(1000), 
                        new KeyValue(node.scaleXProperty(), 1, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), 1, WEB_EASE)
                    )
                )
                
            );
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }

    @Override
    protected void stopping() {
        super.stopping();
    }
}