package vn.conyeu.javafx.animation.animate;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 * Animate a rotate out up right effect on a node
 * <pre>{@literal @}keyframes rotateOutUpRight {
 * 	0% {
 * 		transform-origin: right bottom;
 * 		transform: rotate(0);
 * 		opacity: 1;
 * 	}
 * 	100% {
 * 		transform-origin: right bottom;
 * 		transform: rotate(90deg);
 * 		opacity: 0;
 * 	}
 * }
 * 
 * </pre>
 */
public class RotateOutUpRight extends CachedTimeline {
    private Rotate rotate;
    /**
     * Create new RotateOutUpRight
     * 
     * @param node The node to affect
     */
    public RotateOutUpRight(final Node node) {
        super(node, null);
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }

    @Override protected void starting() {
        super.starting();
        rotate = new Rotate(0,
                node.getBoundsInLocal().getWidth(),
                node.getBoundsInLocal().getHeight());
        timeline = new Timeline(
                new KeyFrame(Duration.millis(0),    
                    new KeyValue(node.opacityProperty(), 1, WEB_EASE),
                    new KeyValue(rotate.angleProperty(), 0, WEB_EASE)
                ),
                new KeyFrame(Duration.millis(1000),    
                    new KeyValue(node.opacityProperty(), 0, WEB_EASE),
                    new KeyValue(rotate.angleProperty(), 90, WEB_EASE)
                )
            )
            ;
        node.getTransforms().add(rotate);
    }

    @Override protected void stopping() {
        super.stopping();
        node.getTransforms().remove(rotate);
    }
}