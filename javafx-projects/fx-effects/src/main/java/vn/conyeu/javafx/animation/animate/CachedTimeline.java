package vn.conyeu.javafx.animation.animate;

import javafx.animation.Interpolator;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.util.Duration;

public abstract class CachedTimeline extends Transition {
    protected static final Interpolator WEB_EASE = Interpolator.SPLINE(0.25, 0.1, 0.25, 1.0);
    public static final Interpolator EASE = WEB_EASE;

    //private final Runnable pauseListener = this::pause;
    //private final Runnable resumeListener = this::play;
    private CacheHint oldCacheHint = CacheHint.DEFAULT;

    protected final Node node;
    private final boolean useCache;
    private final boolean opacityInterpolated;
    private boolean oldCache = false;
    protected Timeline timeline;
    protected Duration duration = Duration.seconds(1);

    /**
     * Create new CachedTimeline
     *
     * @param node The node that is being animated by the timeline
     * @param timeline The timeline for the animation, it should be from 0 to 1 seconds
     */
    public CachedTimeline(final Node node, final Duration duration, final Timeline timeline) {
        this(node, duration, timeline, true, false);
    }

    /**
     * Create new CachedTimeline
     *
     * @param node The node that is being animated by the timeline
     * @param timeline The timeline for the animation, it should be from 0 to 1 seconds
     */
    public CachedTimeline(final Node node, final Timeline timeline) {
        this(node, timeline, true);
    }

    /**
     * Create new CachedTimeline
     *
     * @param node The node that is being animated by the timeline
     * @param timeline The timeline for the animation, it should be from 0 to 1 seconds
     * @param useCache When true the node is cached as image during the animation
     */
    public CachedTimeline(Node node, Timeline timeline, boolean useCache) {
        this(node, timeline, useCache, false);
    }

    public CachedTimeline(Node node, Timeline timeline, boolean useCache, boolean opacityInterpolated) {
        this(node, Duration.seconds(1), timeline, useCache, opacityInterpolated);
    }

    /**
     * Create new CachedTimeline
     *
     * @param node The node that is being animated by the timeline
     * @param timeline The timeline for the animation, it should be from 0 to 1 seconds
     * @param useCache When true the node is cached as image during the animation
     * @param opacityInterpolated - When true opacity is interpolated during the transition
     */
    public CachedTimeline(Node node, Duration duration, Timeline timeline, boolean useCache, boolean opacityInterpolated) {
        this.node = node;
        this.timeline = timeline;
        this.useCache = useCache;
        this.opacityInterpolated = opacityInterpolated;
        this.registerStatusListener();

        if (this instanceof HideTransition) {
            hideNode();
        }
    }

    private void registerStatusListener() {
        statusProperty().addListener((obs, ov, nv) -> {
            switch (nv) {
                case RUNNING -> starting();
                case STOPPED -> stopping();
                case PAUSED -> {}
            }
        });
    }

    @Override
    public final void stop() {
        super.stop();
    }

    /**
     * Called when the animation is starting
     */
    protected void starting() {
        if (useCache) {
            oldCache = node.isCache();
            oldCacheHint = node.getCacheHint();
            node.setCache(true);
            node.setCacheHint(CacheHint.SPEED);
        }
    }

    /**
     * Called when the animation is stopping
     */
    protected void stopping() {
        if (useCache) {
            node.setCache(oldCache);
            node.setCacheHint(oldCacheHint);
        }
    }

    @Override
    public void pause() {
        if(timeline != null) timeline.pause();
        super.pause();
    }

    @Override
    public void play() {
        super.play();
    }

    @Override
    protected void interpolate(double d) {
        if(timeline != null) {
            timeline.playFrom(Duration.seconds(d));
            timeline.stop();
        }
    }

    /**
     * Called before starting the animation, in those cases where the animation will set the opacity to 0.0 right at the start. It must be called from the different transitions, in order to hide the node so it won't be visible during the initial delay, when it is added to the scene but the animation has not started yet.
     * */
    public final void hideNode() {
        if (node != null) {
            node.setOpacity(0.0);
        }
    }

    public boolean isRunning() {
        return getStatus() == Status.RUNNING;
    }

    protected boolean isOpacityInterpolated() {
        return opacityInterpolated;
    }
}