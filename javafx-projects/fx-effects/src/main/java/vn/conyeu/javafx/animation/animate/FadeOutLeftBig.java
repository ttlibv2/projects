package vn.conyeu.javafx.animation.animate;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a fade out left big effect on a node
 * <pre>{@literal @}keyframes fadeOutLeftBig {
 * 	0% {
 * 		opacity: 1;
 * 		transform: translateX(0);
 * 	}
 * 	100% {
 * 		opacity: 0;
 * 		transform: translateX(-2000px);
 * 	}
 * }
 * 
 * </pre>
 */
public class FadeOutLeftBig extends CachedTimeline {
    /**
     * Create new FadeOutLeftBig
     * 
     * @param node The node to affect
     */
    public FadeOutLeftBig(final Node node) {
        super(node, null);
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }

    @Override protected void starting() {
        double endX = -node.localToScene(0, 0).getX() -node.getBoundsInParent().getWidth();
        timeline = new Timeline(
                    new KeyFrame(Duration.millis(0),    
                        new KeyValue(node.opacityProperty(), 1, WEB_EASE),
                        new KeyValue(node.translateXProperty(), 0, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(1000),    
                        new KeyValue(node.opacityProperty(), 0, WEB_EASE),
                        new KeyValue(node.translateXProperty(), endX, WEB_EASE)
                    )
                );
        super.starting();
    }

    @Override protected void stopping() {
        super.stopping();
        node.setTranslateX(0); // restore default
    }
    
    
}