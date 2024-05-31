package vn.conyeu.javafx.animation.beans;

import javafx.beans.value.WritableValue;
import javafx.scene.Node;
import javafx.util.Duration;

import java.util.*;
import java.util.function.Supplier;

/**
 * Custom AnimationTimer that can be created the same way as a timeline,
 * however it doesn't behave the same yet. it only animates in one direction,
 * it doesn't support animation 0 -> 1 -> 0.5
 */
public class AnimationTimer extends javafx.animation.AnimationTimer {
    private final HashMap<KeyFrame, AnimationHandler> mutableFrames = new HashMap<>();
    private final Set<AnimationHandler> animationHandlers = new HashSet<>();
    private final List<CacheMemento> caches = new ArrayList<>();
    private long startTime = -1;
    private boolean running = false;
    private double totalElapsedMilliseconds;

    public AnimationTimer(KeyFrame... keyFrames) {
        for (KeyFrame keyFrame : keyFrames) {
            addHandler(keyFrame);
        }
    }

    public void addKeyFrame(KeyFrame keyFrame) {
        checkRunning();
        AnimationHandler handler = addHandler(keyFrame);
        if (handler != null) mutableFrames.put(keyFrame, handler);
    }

    public void removeKeyFrame(KeyFrame keyFrame) {
        checkRunning();
        AnimationHandler handler = mutableFrames.get(keyFrame);
        animationHandlers.remove(handler);
    }

    @Override
    public void start() {
        super.start();
        running = true;
        startTime = -1;
        animationHandlers.forEach(AnimationHandler::init);
        caches.forEach(CacheMemento::cache);
    }

    @Override
    public void handle(long now) {
        startTime = startTime == -1 ? now : startTime;
        totalElapsedMilliseconds = (now - startTime) / 1000000.0;

        boolean stop = true;

        for (AnimationHandler handler : animationHandlers) {
            handler.animate(totalElapsedMilliseconds);
            if (!handler.finished) stop = false;
        }

        if (stop) {
            this.stop();
        }

    }

    /**
     * this method will pause the timer and reverse the animation if the timer already
     * started otherwise it will start the animation.
     */
    public void reverseAndContinue() {
        if (isRunning()) {
            super.stop();
            for (AnimationHandler handler : animationHandlers) {
                handler.reverse(totalElapsedMilliseconds);
            }
            startTime = -1;
            super.start();
        } else {
            start();
        }
    }

    @Override
    public void stop() {
        super.stop();
        running = false;
        animationHandlers.forEach(AnimationHandler::clear);
        caches.forEach(CacheMemento::restore);
        if (onFinished != null) onFinished.run();
    }

    public void applyEndValues() {
        if (isRunning()) {
            super.stop();
        }

        animationHandlers.forEach(AnimationHandler::applyEndValues);
        startTime = -1;
    }

    public boolean isRunning() {
        return running;
    }

    private Runnable onFinished = null;

    public void setOnFinished(Runnable onFinished) {
        this.onFinished = onFinished;
    }

    public void setCacheNodes(Node... nodesToCache) {
        caches.clear();
        if (nodesToCache != null) {
            for (Node node : nodesToCache) {
                caches.add(new CacheMemento(node));
            }
        }
    }

    public void dispose() {
        caches.clear();
        animationHandlers.forEach(AnimationHandler::dispose);
        animationHandlers.clear();
    }


    private void checkRunning() {
        if (isRunning()) {
            String msg = "Can't update animation timer while running";
            throw new IllegalArgumentException(msg);
        }
    }

    private AnimationHandler addHandler(KeyFrame keyFrame) {
        final Duration duration = keyFrame.getDuration();
        final Set<KeyValue<?>> keyValuesSet = keyFrame.getValues();
        return !keyValuesSet.isEmpty() ? addHandler(duration, keyFrame) : null;
    }

    private AnimationHandler addHandler(Duration duration, KeyFrame keyFrame) {
        AnimationHandler handler = new AnimationHandler(duration, keyFrame);
        animationHandlers.add(handler);
        return handler;
    }

    static class AnimationHandler {
        private final double duration;
        private final Set<KeyValue<?>> keyValues;
        private final Supplier<Boolean> animationCondition;
        private boolean finished = false;
        private double currentDuration;

        private final HashMap<WritableValue<?>, Object> initialValuesMap = new HashMap<>();
        private final HashMap<WritableValue<?>, Object> endValuesMap = new HashMap<>();

        AnimationHandler(Duration duration, KeyFrame keyFrame) {
            this.duration = duration.toMillis();
            this.currentDuration = duration.toMillis();
            this.keyValues = keyFrame.getValues();
            this.animationCondition = keyFrame.getAnimateCondition();
        }


        private boolean isFinished() {
            return animationCondition != null && !animationCondition.get();
        }

        public void init() {
            finished = isFinished();
            for (KeyValue keyValue : keyValues) {
                if (keyValue.getTarget() != null) {
                    if (!initialValuesMap.containsKey(keyValue.getTarget())) {
                        initialValuesMap.put(keyValue.getTarget(), keyValue.getTarget().getValue());
                    }
                    if (!endValuesMap.containsKey(keyValue.getTarget())) {
                        endValuesMap.put(keyValue.getTarget(), keyValue.getEndValue());
                    }
                }
            }
        }

        void reverse(double now) {
            finished = animationCondition != null && !animationCondition.get();
            currentDuration = duration - (currentDuration - now);
            // update initial values
            for (KeyValue keyValue : keyValues) {
                final WritableValue target = keyValue.getTarget();
                if (target != null) {
                    initialValuesMap.put(target, target.getValue());
                    endValuesMap.put(target, keyValue.getEndValue());
                }
            }
        }

        // now in milliseconds
        public void animate(double now) {
            // if animate condition for the key frame is not met then do nothing
            if (!finished) {
                if (now <= currentDuration) {
                    for (KeyValue keyValue : keyValues) {
                        if (keyValue.isValid()) {
                            final WritableValue target = keyValue.getTarget();
                            final Object endValue = endValuesMap.get(target);

                            if (endValue != null && target != null && !target.getValue().equals(endValue)) {
                                Object startValue = initialValuesMap.get(target);
                                double fraction = now / currentDuration;
                                target.setValue(keyValue.getInterpolator().interpolate(startValue, endValue, fraction));
                            }
                        }
                    }
                } //
                else {
                    finished = true;
                    for (KeyValue keyValue : keyValues) {
                        if (keyValue.isValid()) {
                            final WritableValue target = keyValue.getTarget();
                            if (target != null) {
                                final Object endValue = keyValue.getEndValue();
                                if (endValue != null) target.setValue(endValue);

                            }
                        }
                    }
                    currentDuration = duration;

                }
            }


        }

        public void applyEndValues() {
            for (KeyValue keyValue : keyValues) {
                if (keyValue.isValid()) {
                    final WritableValue target = keyValue.getTarget();
                    if (target != null) {
                        final Object endValue = keyValue.getEndValue();
                        if (endValue != null && !target.getValue().equals(endValue)) {
                            target.setValue(endValue);
                        }
                    }
                }
            }
        }

        public void clear() {
            initialValuesMap.clear();
            endValuesMap.clear();
        }

        void dispose() {
            clear();
            keyValues.clear();
        }
    }
}