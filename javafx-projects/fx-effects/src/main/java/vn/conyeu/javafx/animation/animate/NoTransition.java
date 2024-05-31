package vn.conyeu.javafx.animation.animate;

import javafx.animation.Timeline;
import javafx.util.Duration;

public class NoTransition extends CachedTimeline {
    public NoTransition() {
        super(null, new Timeline());
        setCycleDuration(Duration.seconds(0.001));
    }

    @Override
    protected void starting() {
    }

    @Override
    protected void stopping() {
    }

    @Override
    protected void interpolate(double d) {
    }
}