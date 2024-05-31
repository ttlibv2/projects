package vn.conyeu.javafx.animation.utils;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.beans.value.WritableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

/**
 * Builder class for keyframes and key values.
 */
public class KeyFrames {

    /**
     * Returns a new KeyFrame with the given duration and action.
     */
    public static KeyFrame of(Duration duration, EventHandler<ActionEvent> action) {
        return new KeyFrame(duration, action);
    }

    /**
     * Calls {@link #of(Duration, EventHandler)} by converting the given millis value
     * with {@link Duration#millis(double)}.
     */
    public static KeyFrame of(double millis, EventHandler<ActionEvent> action) {
        return of(Duration.millis(millis), action);
    }

    /**
     * Returns a new KeyFrame with the given duration and keyvalues.
     */
    public static KeyFrame of(Duration duration, KeyValue... keyValues) {
        return new KeyFrame(duration, keyValues);
    }

    /**
     * Calls {@link #of(Duration, KeyValue[])} by converting the given millis value
     * with {@link Duration#millis(double)}.
     */
    public static KeyFrame of(double millis, KeyValue... keyValues) {
        return of(Duration.millis(millis), keyValues);
    }

    /**
     * Returns a new KeyFrame with the given duration and builds a new KeyValue for it
     * with the given writable property and endValue.
     */
    public static <T> KeyFrame of(Duration duration, WritableValue<T> writableValue, T endValue) {
        return of(duration, new KeyValue(writableValue, endValue));
    }

    /**
     * Calls {@link #of(Duration, WritableValue, Object)} by converting the given millis value
     * with {@link Duration#millis(double)}.
     */
    public static <T> KeyFrame of(double millis, WritableValue<T> writableValue, T endValue) {
        return of(Duration.millis(millis), writableValue, endValue);
    }

    /**
     * Returns a new KeyFrame with the given duration and builds a new KeyValue for it
     * with the given writable property, endValue and interpolator.
     */
    public static <T> KeyFrame of(Duration duration, WritableValue<T> writableValue, T endValue, Interpolator interpolator) {
        return of(duration, new KeyValue(writableValue, endValue, interpolator));
    }

    /**
     * Calls {@link #of(Duration, WritableValue, Object, Interpolator)} by converting the given millis value
     * with {@link Duration#millis(double)}.
     */
    public static <T> KeyFrame of(double millis, WritableValue<T> writableValue, T endValue, Interpolator interpolator) {
        return of(Duration.millis(millis), writableValue, endValue, interpolator);
    }

    /**
     * Returns a new KeyFrame with the given duration and builds a new KeyValue for it
     * with the given writable property, endValue and interpolator.
     */
    public static <T> KeyFrame of(Duration duration, WritableValue<T> writableValue, T endValue, Interpolators interpolator) {
        return of(duration, new KeyValue(writableValue, endValue, interpolator.toInterpolator()));
    }

    /**
     * Calls {@link #of(Duration, WritableValue, Object, Interpolators)} by converting the given millis value
     * with {@link Duration#millis(double)}.
     */
    public static <T> KeyFrame of(double millis, WritableValue<T> writableValue, T endValue, Interpolators interpolator) {
        return of(Duration.millis(millis), writableValue, endValue, interpolator.toInterpolator());
    }
}