package vn.conyeu.javafx.animation.beans;

import javafx.util.Duration;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Supplier;

public final class KeyFrame {
    private final Duration duration;
    private final Set<KeyValue<?>> keyValues = new CopyOnWriteArraySet<>();
    private final Supplier<Boolean> animateCondition;

    public KeyFrame(Duration duration, KeyValue<?>... keyValues) {
        this(duration, null, Set.of(keyValues));
    }

    public KeyFrame(Duration duration, Supplier<Boolean> animateCondition, KeyValue<?>... keyValues) {
       this(duration, animateCondition, Set.of(keyValues));
    }

    public KeyFrame(Duration duration, Supplier<Boolean> animateCondition, Set<KeyValue<?>> keyValues) {
        this.duration = duration;
        this.animateCondition = animateCondition;
        for (KeyValue<?> keyValue : keyValues) {
            if (keyValue != null) this.keyValues.add(keyValue);
        }
    }

    public final Duration getDuration() {
        return duration;
    }

    public final Set<KeyValue<?>> getValues() {
        return keyValues;
    }

    public Supplier<Boolean> getAnimateCondition() {
        return animateCondition;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Duration duration;
        private final Set<KeyValue<?>> keyValues = new CopyOnWriteArraySet<>();
        private Supplier<Boolean> animateCondition = null;

        private Builder() {
        }

        public Builder duration(Duration duration) {
            this.duration = duration;
            return this;
        }

        public Builder keyValues(KeyValue<?>... keyValues) {
            for (final KeyValue<?> keyValue : keyValues) {
                if (keyValue != null) {
                    this.keyValues.add(keyValue);
                }
            }
            return this;
        }

        public Builder animateCondition(Supplier<Boolean> animateCondition) {
            this.animateCondition = animateCondition;
            return this;
        }

        public KeyFrame build() {
           return new KeyFrame(duration,
                   animateCondition, keyValues);
        }
    }
}