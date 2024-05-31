package vn.conyeu.javafx.animation.animate;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Camera;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 * Animate a flip in y effect on the given node
 * <pre>{@literal @}keyframes flipInY {
 *     0% {
 *         transform: perspective(400px) rotateY(90deg);
 *         opacity: 0;
 *     }
 *     40% {
 *         transform: perspective(400px) rotateY(-10deg);
 *     }
 *     70% {
 *         transform: perspective(400px) rotateY(10deg);
 *     }
 *     100% {
 *         transform: perspective(400px) rotateY(0deg);
 *         opacity: 1;
 *     }
 * }
 *
 * </pre>
 */
public class FlipInY extends CachedTimeline {
    private Camera oldCamera;
    
    /**
     * Create new FlipInY
     * 
     * @param node The node to affect
     */
    public FlipInY(final Node node) {
        super(
            node,
            new Timeline(
                    new KeyFrame(Duration.millis(0), 
                        new KeyValue(node.rotateProperty(), -90, WEB_EASE),
                        new KeyValue(node.opacityProperty(), 0, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(400), 
                        new KeyValue(node.rotateProperty(), 10, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(700), 
                        new KeyValue(node.rotateProperty(), -10, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(1000), 
                        new KeyValue(node.rotateProperty(), 0, WEB_EASE),
                        new KeyValue(node.opacityProperty(), 1, WEB_EASE)
                    )
                )
                
            );
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }

    @Override protected void starting() {
        super.starting();
        node.setRotationAxis(Rotate.Y_AXIS);
        oldCamera = node.getScene().getCamera();
        node.getScene().setCamera(new PerspectiveCamera());
    }

    @Override protected void stopping() {
        super.stopping();
        node.setRotate(0);
        node.setRotationAxis(Rotate.Z_AXIS);
        node.getScene().setCamera(oldCamera);
    }
}