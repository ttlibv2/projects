package vn.conyeu.javafx.utils;


import javafx.animation.*;
import javafx.animation.Animation.Status;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.util.Duration;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility class for JavaFX's ScrollPanes.
 */
public class ScrollUtils {
    private static final Interpolator INTERPOLATOR_V1 = Interpolator.SPLINE(0.25, 0.1, 0.25, 1);

    public enum ScrollDirection {
        UP(-1), RIGHT(1), DOWN(1), LEFT(-1);

        final int intDirection;

        ScrollDirection(int intDirection) {
            this.intDirection = intDirection;
        }

        public int intDirection() {
            return intDirection;
        }
    }

    private ScrollUtils() {
    }

    /**
     * Determines if the given ScrollEvent comes from a trackpad.
     * <p></p>
     * Although this method works in most cases, it is not very accurate.
     * Since in JavaFX there's no way to tell if a ScrollEvent comes from a trackpad or a mouse
     * we use this trick: I noticed that a mouse scroll has a delta of 32 (don't know if it changes depending on the device or OS)
     * and trackpad scrolls have a way smaller delta. So depending on the scroll direction we check if the delta is lesser than 10
     * (trackpad event) or greater(mouse event).
     *
     * @see ScrollEvent#getDeltaX()
     * @see ScrollEvent#getDeltaY()
     */
    public static boolean isTrackPad(ScrollEvent event, ScrollDirection scrollDirection) {
        return switch (scrollDirection) {
            case UP, DOWN -> Math.abs(event.getDeltaY()) < 10;
            case LEFT, RIGHT -> Math.abs(event.getDeltaX()) < 10;
            default -> false;
        };
    }

    /**
     * Determines the scroll direction of the given ScrollEvent.
     * <p></p>
     * Although this method works fine, it is not very accurate.
     * In JavaFX there's no concept of scroll direction, if you try to scroll with a trackpad
     * you'll notice that you can scroll in both directions at the same time, both deltaX and deltaY won't be 0.
     * <p></p>
     * For this method to work we assume that this behavior is not possible.
     * <p></p>
     * If deltaY is 0 we return LEFT or RIGHT depending on deltaX (respectively if lesser or greater than 0).
     * <p>
     * Else we return DOWN or UP depending on deltaY (respectively if lesser or greater than 0).
     *
     * @see ScrollEvent#getDeltaX()
     * @see ScrollEvent#getDeltaY()
     */
    public static ScrollDirection determineScrollDirection(ScrollEvent event) {
        double deltaX = event.getDeltaX();
        double deltaY = event.getDeltaY();

        if (deltaY == 0.0) {
            return deltaX < 0 ? ScrollDirection.LEFT : ScrollDirection.RIGHT;
        } else {
            return deltaY < 0 ? ScrollDirection.DOWN : ScrollDirection.UP;
        }
    }

    /**
     * Adds a smooth scrolling effect to the given scroll pane,
     * calls {@link #addSmoothScrolling(ScrollPane, double)} with a
     * default speed value of 1.
     */
    public static void addSmoothScrolling(ScrollPane scrollPane) {
        addSmoothScrolling(scrollPane, 1);
    }

    /**
     * Adds a smooth scrolling effect to the given scroll pane with the given scroll speed.
     * Calls {@link #addSmoothScrolling(ScrollPane, double, double)}
     * with a default trackPadAdjustment of 7.
     */
    public static void addSmoothScrolling(ScrollPane scrollPane, double speed) {
        addSmoothScrolling(scrollPane, speed, 7);
    }

    /**
     * Adds a smooth scrolling effect to the given scroll pane with the given
     * scroll speed and the given trackPadAdjustment.
     * <p></p>
     * The trackPadAdjustment is a value used to slow down the scrolling if a trackpad is used.
     * This is kind of a workaround and it's not perfect, but at least it's way better than before.
     * The default value is 7, tested up to 10, further values can cause scrolling misbehavior.
     */
    public static void addSmoothScrolling(ScrollPane scrollPane, double speed, double trackPadAdjustment) {
        smoothScroll(scrollPane, speed, trackPadAdjustment);
    }

    private static void smoothScroll(ScrollPane scrollPane, double speed, double trackPadAdjustment) {
        final double[] frictions = {0.99, 0.1, 0.05, 0.04, 0.03, 0.02, 0.01, 0.04, 0.01, 0.008, 0.008, 0.008, 0.008, 0.0006, 0.0005, 0.00003, 0.00001};
        final double[] derivatives = new double[frictions.length];
        AtomicReference<Double> atomicSpeed = new AtomicReference<>(speed);

        Timeline timeline = new Timeline();
        AtomicReference<ScrollDirection> scrollDirection = new AtomicReference<>();
        final EventHandler<MouseEvent> mouseHandler = event -> timeline.stop();
        final EventHandler<ScrollEvent> scrollHandler = event -> {
            if (event.getEventType() == ScrollEvent.SCROLL) {
                scrollDirection.set(determineScrollDirection(event));
                if (isTrackPad(event, scrollDirection.get())) {
                    atomicSpeed.set(speed / trackPadAdjustment);
                } else {
                    atomicSpeed.set(speed);
                }
                derivatives[0] += scrollDirection.get().intDirection * atomicSpeed.get();
                if (timeline.getStatus() == Status.STOPPED) {
                    timeline.play();
                }
                event.consume();
            }
        };

        if (scrollPane.getContent().getParent() != null) {
            scrollPane.getContent().getParent().addEventHandler(MouseEvent.MOUSE_PRESSED, mouseHandler);
            scrollPane.getContent().getParent().addEventHandler(ScrollEvent.ANY, scrollHandler);
        }

        scrollPane.getContent().parentProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                oldValue.removeEventHandler(MouseEvent.MOUSE_PRESSED, mouseHandler);
                oldValue.removeEventHandler(ScrollEvent.ANY, scrollHandler);
            }
            if (newValue != null) {
                newValue.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseHandler);
                newValue.addEventHandler(ScrollEvent.ANY, scrollHandler);
            }
        });

        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(3), event -> {

            for (int i = 0; i < derivatives.length; i++) {
                derivatives[i] *= frictions[i];
            }

            for (int i = 1; i < derivatives.length; i++) {
                derivatives[i] += derivatives[i - 1];
            }

            double dy = derivatives[derivatives.length - 1];
            Function<Bounds, Double> sizeFunction = (scrollDirection.get() == ScrollDirection.UP || scrollDirection.get() == ScrollDirection.DOWN) ? Bounds::getHeight : Bounds::getWidth;
            double size = sizeFunction.apply(scrollPane.getContent().getLayoutBounds());
            double value;
            switch (scrollDirection.get()) {
                case LEFT, RIGHT -> {
                    value = Math.min(Math.max(scrollPane.hvalueProperty().get() + dy / size, 0), 1);
                    scrollPane.hvalueProperty().set(value);
                }
                case UP, DOWN -> {
                    value = Math.min(Math.max(scrollPane.vvalueProperty().get() + dy / size, 0), 1);
                    scrollPane.vvalueProperty().set(value);
                }
            }

            if (Math.abs(dy) < 0.001) {
                timeline.stop();
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    /**
     * Adds a fade in and out effect to the given scroll pane's scroll bars,
     * calls {@link #animateScrollBars(ScrollPane, double)} with a
     * default fadeSpeedMillis value of 500.
     */
    public static void animateScrollBars(ScrollPane scrollPane) {
        animateScrollBars(scrollPane, 500);
    }

    /**
     * Adds a fade in and out effect to the given scroll pane's scroll bars,
     * calls {@link #animateScrollBars(ScrollPane, double, double)} with a
     * default fadeSpeedMillis value of 500 and a default hideAfterMillis value of 800.
     */
    public static void animateScrollBars(ScrollPane scrollPane, double fadeSpeedMillis) {
        animateScrollBars(scrollPane, fadeSpeedMillis, 800);
    }

    /**
     * Adds a fade in and out effect to the given scroll pane's scroll bars
     * with the given fadeSpeedMillis and hideAfterMillis values.
     *
     * @see NodeUtils#waitForSkin(Control, Runnable, boolean, boolean)
     */
    public static void animateScrollBars(ScrollPane scrollPane, double fadeSpeedMillis, double hideAfterMillis) {
        NodeUtils.waitForSkin(scrollPane, () -> {

            Set<ScrollBar> scrollBars = scrollPane.lookupAll(".scroll-bar").stream()
                    .filter(node -> node instanceof ScrollBar)
                    .map(node -> (ScrollBar) node)
                    .collect(Collectors.toSet());

            scrollBars.forEach(scrollBar -> {
                scrollBar.setOpacity(0.0);
                scrollPane.hoverProperty().addListener((observable, oldValue, newValue) -> {
                    if (scrollBar.isVisible()) {
                        if (newValue) {
                            Timeline t = new Timeline(
                                    new KeyFrame(Duration.ZERO, new KeyValue(scrollBar.opacityProperty(), 0, INTERPOLATOR_V1)),
                                    new KeyFrame(Duration.millis(fadeSpeedMillis), new KeyValue(scrollBar.opacityProperty(), 1.0, INTERPOLATOR_V1))
                            );
                            t.play();
                        } //
                        else {
                            Timeline t = new Timeline(
                                    new KeyFrame(Duration.ZERO, new KeyValue(scrollBar.opacityProperty(), 1.0, INTERPOLATOR_V1)),
                                    new KeyFrame(Duration.millis(fadeSpeedMillis), new KeyValue(scrollBar.opacityProperty(), 0.0, INTERPOLATOR_V1))
                            );
                            PauseTransition pt = new PauseTransition(Duration.millis(hideAfterMillis));
                            pt.setOnFinished(e -> t.play());
                            pt.play();
                        }
                    }


                });
            });
        }, true, true);
    }
}