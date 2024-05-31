package vn.conyeu.javafx.animation.animate;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a fade out effect on a node
 * <pre>{@literal @}keyframes fadeOut {
 * 	0% {opacity: 1;}	
 * 	100% {opacity: 0;}
 * }
 * 
 * </pre>
 */
public class FadeOut extends CachedTimeline {

    /**
     * Create new FadeOut
     *
     * @param node The node to affect
     */
    public FadeOut(final Node node) {
        this(node, Duration.seconds(1));

    }

    /**
     * Create new FadeOut
     * 
     * @param node The node to affect
     */
    public FadeOut(final Node node, final Duration duration) {
        super(
            node,
            new Timeline(
                    new KeyFrame(Duration.ZERO,    new KeyValue(node.opacityProperty(), 1, WEB_EASE)),
                    new KeyFrame(duration,  new KeyValue(node.opacityProperty(), 0, WEB_EASE))
                )
                
            );
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }

    @Override
    protected void stopping() {
        super.stopping();
        node.setOpacity(1);
    }
}