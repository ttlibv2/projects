package vn.conyeu.javafx.controls.skin;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import javafx.util.StringConverter;
import vn.conyeu.javafx.bindings.BindingBuilder;
import vn.conyeu.javafx.controls.RangeSlider;
import vn.conyeu.javafx.controls.behavior.RangeSliderBehavior;

public class RangeSliderSkin extends SimpleSkinBase<RangeSlider> {
    private NumberAxis tickLine = null;
    private double trackToTickGap = 2;

    private boolean showTickMarks;
    private double thumbWidth;
    private double thumbHeight;


    private StackPane track;
    private double trackStart;
    private double trackLength;
    private double lowThumbPos;
    private double rangeEnd;
    private double rangeStart;
    private ThumbPane lowThumb;
    private ThumbPane highThumb;
    private StackPane rangeBar; // the bar between the two thumbs, can be dragged

    // temp fields for mouse drag handling
    private double preDragPos;          // used as a temp value for low and high thumbs
    private Point2D preDragThumbPoint;  // in skin coordinates
    private FocusedChild currentFocus = FocusedChild.LOW_THUMB;
    private Callback<Void, FocusedChild> selectedValue = v -> currentFocus;
    private boolean tooltipConsumeAutoHidingEvents;

    private RangeSliderBehavior behavior;
    private BooleanBinding showTickMarksBinding;
    private ObjectBinding<Side> sideBinding;
    private ObjectBinding<Cursor> rangeBarCursorBinding;

    //== event
    private final EventHandler<MouseEvent> trackPressHandler = this::onTrackPressed;
    private final EventHandler<MouseEvent> trackDraggedHandler = this::onTrackDragged;
    private final EventHandler<MouseEvent> trackReleasedHandler = this::onTrackReleased;

    private final EventHandler<MouseEvent> lowThumbPressHandler = this::onLowThumbPressed;
    private final EventHandler<MouseEvent> lowThumbDraggedHandler = this::onLowThumbDragged;
    private final EventHandler<MouseEvent> lowThumbReleasedHandler = this::onLowThumbReleased;
    private final EventHandler<MouseEvent> lowThumbEnteredHandler = this::lowThumbEntered;
    private final EventHandler<MouseEvent> lowThumbExitedHandler = this::lowThumbExited;

    private final EventHandler<MouseEvent> highThumbPressHandler = this::highThumbPressed;
    private final EventHandler<MouseEvent> highThumbDraggedHandler = this::highThumbDragged;
    private final EventHandler<MouseEvent> highThumbReleasedHandler = this::highThumbReleased;
    private final EventHandler<MouseEvent> highThumbEnteredHandler = this::highThumbEntered;
    private final EventHandler<MouseEvent> highThumbExitedHandler = this::highThumbExited;

    private final EventHandler<MouseEvent> rangeBarPressHandler = this::rangeBarPressed;
    private final EventHandler<MouseEvent> rangeBarDraggedHandler = this::rangeBarDragged;
    private final EventHandler<MouseEvent> rangeBarReleasedHandler = this::rangeBarReleased;


    StringConverter<Number> stringConverterWrapper = new StringConverter<>() {
        final Slider slider = getSkinnable();

        @Override
        public String toString(Number object) {
            return (object != null) ? slider.getLabelFormatter().toString(object.doubleValue()) : "";
        }

        @Override
        public Number fromString(String string) {
            return slider.getLabelFormatter().fromString(string);
        }
    };


    public RangeSliderSkin(RangeSlider slider) {
        super(slider);
        behavior = new RangeSliderBehavior(this);
        initializeAll(slider);
    }

    protected void initializeUI(RangeSlider sl) {
        showTickMarksBinding = BindingBuilder.booleanBuilder()
                .setMapper(() -> sl.isShowTickLabels() || sl.isShowTickLabels())
                .addSources(sl.showTickLabelsProperty(), sl.showTickMarksProperty())
                .get();

        sideBinding = BindingBuilder.<Side>objectBuilder()
                .setMapper(() -> isVertical() ? Side.RIGHT : Side.BOTTOM)
                .addSources(sl.orientationProperty()).get();

        track = new StackPane();
        track.setFocusTraversable(false);
        track.getStyleClass().setAll("track");

        lowThumb = new ThumbPane();
        lowThumb.getStyleClass().setAll("low-thumb");
        lowThumb.setFocusTraversable(true);

        highThumb = new ThumbPane();
        highThumb.getStyleClass().setAll("high-thumb");

        rangeBar = new StackPane();
        rangeBar.setFocusTraversable(false);
        rangeBar.getStyleClass().setAll("range-bar");
        rangeBar.cursorProperty().bind(BindingBuilder.<Cursor>objectBuilder()
                .setMapper(() -> rangeBar.isHover() ? Cursor.HAND : Cursor.DEFAULT)
                .addSources(rangeBar.hoverProperty()).get());

        getChildren().setAll(track, lowThumb, highThumb, rangeBar);
    }

    @Override
    protected void initializeDataUI(RangeSlider sl) {
        changeVisibleTickMark(sl);
    }

    protected void initializeListener(RangeSlider sl) {
        registerChangeListener(showTickMarksBinding, e -> changeVisibleTickMark(sl));
        registerChangeListener(sl.valueProperty(), e -> changeLowValue());
        registerChangeListener(sl.minProperty(), e -> changeMinValue());
        registerChangeListener(sl.maxProperty(), e -> changeMaxValue());
        registerChangeListener(sl.orientationProperty(), e -> changeOrientation());
        registerChangeListener(lowThumb.focusedProperty(), e -> changeFocusedChild());
        registerChangeListener(highThumb.focusedProperty(), e -> changeFocusedChild());
        registerChangeListener(rangeBar.focusedProperty(), e -> changeFocusedChild());
        registerChangeListener(sl.focusedProperty(), e -> {
            if(sl.isFocused())  lowThumb.setFakeFocused(true);
            else {
                lowThumb.setFakeFocused(false);
                highThumb.setFakeFocused(false);
                currentFocus = FocusedChild.NONE;
            }
        });

        track.addEventHandler(MouseEvent.MOUSE_PRESSED, trackPressHandler);
        track.addEventHandler(MouseEvent.MOUSE_DRAGGED, trackDraggedHandler);
        track.addEventHandler(MouseEvent.MOUSE_RELEASED, trackReleasedHandler);

        lowThumb.addEventHandler(MouseEvent.MOUSE_PRESSED, lowThumbPressHandler);
        lowThumb.addEventHandler(MouseEvent.MOUSE_DRAGGED, lowThumbDraggedHandler);
        lowThumb.addEventHandler(MouseEvent.MOUSE_RELEASED, lowThumbReleasedHandler);
        lowThumb.addEventHandler(MouseEvent.MOUSE_ENTERED, lowThumbEnteredHandler);
        lowThumb.addEventHandler(MouseEvent.MOUSE_EXITED, lowThumbExitedHandler);

        highThumb.addEventHandler(MouseEvent.MOUSE_PRESSED, highThumbPressHandler);
        highThumb.addEventHandler(MouseEvent.MOUSE_DRAGGED, highThumbDraggedHandler);
        highThumb.addEventHandler(MouseEvent.MOUSE_RELEASED, highThumbReleasedHandler);
        highThumb.addEventHandler(MouseEvent.MOUSE_ENTERED, highThumbEnteredHandler);
        highThumb.addEventHandler(MouseEvent.MOUSE_EXITED, highThumbExitedHandler);

        rangeBar.addEventHandler(MouseEvent.MOUSE_PRESSED, rangeBarPressHandler);
        rangeBar.addEventHandler(MouseEvent.MOUSE_DRAGGED, rangeBarDraggedHandler);
        rangeBar.addEventHandler(MouseEvent.MOUSE_RELEASED, rangeBarReleasedHandler);

    }

    private void changeFocusedChild() {
        if (lowThumb.isFocused()) currentFocus = FocusedChild.LOW_THUMB;
        else if(highThumb.isFocused()) currentFocus = FocusedChild.HIGH_THUMB;
        else if(rangeBar.isFocused())currentFocus = FocusedChild.RANGE_BAR;
    }

    private void changeOrientation() {
        getSkinnable().requestLayout();
    }

    private void changeMaxValue() {
        if (showTickMarks && tickLine != null) {
            tickLine.setUpperBound(getSkinnable().getMax());
        }
        getSkinnable().requestLayout();
    }

    private void changeMinValue() {
            if (showTickMarks && tickLine != null) {
                tickLine.setLowerBound(getSkinnable().getMin());
            }
            getSkinnable().requestLayout();
    }

    private void changeLowValue() {
        positionLowThumb();
        if (isVertical()) rangeBar.resize(rangeBar.getWidth(), rangeEnd - rangeStart);
        else rangeBar.resizeRelocate(rangeStart, rangeBar.getLayoutY(),
                rangeEnd - rangeStart, rangeBar.getHeight());
    }

    /**
     * Call when `track` on `MOUSE_PRESSED`
     */
    protected void onTrackPressed(MouseEvent me) {
        if (!lowThumb.isPressed() && !highThumb.isPressed()) {
            final double pos = isVertical() ? me.getY() : me.getX();
            behavior.trackPress(me, pos / trackLength);
        }
    }

    /**
     * Call when `track` on `MOUSE_DRAGGED`
     */
    protected void onTrackDragged(MouseEvent me) {
        if (!lowThumb.isPressed() && !highThumb.isPressed()) {
            final double pos = isVertical() ? me.getY() : me.getX();
            behavior.trackPress(me, pos / trackLength);
        }
    }

    /**
     * Call when `track` on `MOUSE_RELEASED`
     */
    protected void onTrackReleased(MouseEvent me) {
        behavior.trackRelease(me, 0.0f);
    }

    /**
     * Call when `lowThumb` on `MOUSE_PRESSED`
     */
    protected void onLowThumbPressed(MouseEvent me) {
        final RangeSlider slider = getSkinnable();

        highThumb.setFakeFocused(false);
        lowThumb.setFakeFocused(true);
        preDragThumbPoint = lowThumb.localToParent(me.getX(), me.getY());
        preDragPos = (slider.getValue() - slider.getMin()) / getMaxMinusMinNoZero();
        behavior.lowThumbPressed(me, 0.0f);
    }

    /**
     * Call when `lowThumb` on `MOUSE_DRAGGED`
     */
    protected void onLowThumbDragged(MouseEvent me) {
        Point2D cur = lowThumb.localToParent(me.getX(), me.getY());
        if (preDragThumbPoint == null) {
            preDragThumbPoint = cur;
        }

        double dragPos = !isVertical() ? cur.getX() - preDragThumbPoint.getX()
                : -(cur.getY() - preDragThumbPoint.getY());

        behavior.lowThumbDragged(me, preDragPos + dragPos / trackLength);
    }

    /**
     * Call when `lowThumb` on `MOUSE_RELEASED`
     */
    protected void onLowThumbReleased(MouseEvent event) {
        behavior.lowThumbReleased(event);
    }

    /**
     * Call when `lowThumb` on `{@link MouseEvent#MOUSE_EXITED}`
     */
    protected void lowThumbExited(MouseEvent event) {
        Tooltip t = getSkinnable().getTooltip();
        if (t != null && t.isAutoHide() && !t.consumeAutoHidingEventsProperty().isBound()) {
            t.setConsumeAutoHidingEvents(tooltipConsumeAutoHidingEvents);
        }
    }

    /**
     * Call `{@link MouseEvent#MOUSE_ENTERED}` for lowThumb
     */
    protected void lowThumbEntered(MouseEvent event) {
        Tooltip t = getSkinnable().getTooltip();
        if (t != null && t.isAutoHide() && !t.consumeAutoHidingEventsProperty().isBound()) {
            tooltipConsumeAutoHidingEvents = t.getConsumeAutoHidingEvents();
            // Temporarily disable consuming auto hiding events from tooltip.
            // This is done to receive mouse pressed event on thumb and
            // inturn to detect start of drag.
            t.setConsumeAutoHidingEvents(false);
        }
    }

    /**
     * Call when `highThumb` on `{@link MouseEvent#MOUSE_EXITED}`
     */
    protected void highThumbExited(MouseEvent event) {
        Tooltip t = getSkinnable().getTooltip();
        if (t != null && t.isAutoHide() && !t.consumeAutoHidingEventsProperty().isBound()) {
            t.setConsumeAutoHidingEvents(tooltipConsumeAutoHidingEvents);
        }
    }

    /**
     * Call `{@link MouseEvent#MOUSE_ENTERED}` for highThumb
     */
    protected void highThumbEntered(MouseEvent event) {
        Tooltip t = getSkinnable().getTooltip();
        if (t != null && t.isAutoHide() && !t.consumeAutoHidingEventsProperty().isBound()) {
            tooltipConsumeAutoHidingEvents = t.getConsumeAutoHidingEvents();
            // Temporarily disable consuming auto hiding events from tooltip.
            // This is done to receive mouse pressed event on thumb and
            // inturn to detect start of drag.
            t.setConsumeAutoHidingEvents(false);
        }
    }

    /**
     * Call when `highThumb` on `MOUSE_PRESSED`
     */
    protected void highThumbPressed(MouseEvent me) {
        lowThumb.setFakeFocused(false);
        highThumb.setFakeFocused(true);
        preDragThumbPoint = highThumb.localToParent(me.getX(), me.getY());
        preDragPos = (getSkinnable().getHighValue() - getSkinnable().getMin()) / (getMaxMinusMinNoZero());
        behavior.highThumbPressed(me, 0.0D);
    }

    /**
     * Call when `highThumb` on `MOUSE_DRAGGED`
     */
    protected void highThumbDragged(MouseEvent me) {
        double trackLength = !isVertical() ? track.getWidth() : track.getHeight();

        Point2D point2d = highThumb.localToParent(me.getX(), me.getY());
        if (preDragThumbPoint == null) {
            preDragThumbPoint = point2d;
        }
        double d = !isVertical() ? -(point2d.getY() - preDragThumbPoint.getY()) : point2d.getX() - preDragThumbPoint.getX();
        behavior.highThumbDragged(me, preDragPos + d / trackLength);
    }

    /**
     * Call when `highThumb` on `MOUSE_RELEASED`
     */
    protected void highThumbReleased(MouseEvent event) {
        behavior.highThumbReleased(event);
    }

    /**
     * Call when `rangeBar` on `MOUSE_PRESSED`
     */
    protected void rangeBarPressed(MouseEvent me) {
        rangeBar.requestFocus();
        preDragPos = !isVertical() ? me.getX() : -me.getY();
    }

    /**
     * Call when `rangeBar` on `MOUSE_DRAGGED`
     */
    protected void rangeBarDragged(MouseEvent me) {
        double delta = (isVertical() ? -me.getY() : me.getX()) - preDragPos;
        behavior.moveRange(delta);
    }

    /**
     * Call when `rangeBar` on `MOUSE_RELEASED`
     */
    protected void rangeBarReleased(MouseEvent event) {
        behavior.confirmRange();
    }

    /**
     * @return the difference between max and min, but if they have the same
     * value, 1 is returned instead of 0 because otherwise the division where it
     * can be used will return Nan.
     */
    private double getMaxMinusMinNoZero() {
        RangeSlider s = getSkinnable();
        return s.getMax() - s.getMin() == 0 ? 1 : s.getMax() - s.getMin();
    }

    /**
     * Called when ever either min, max or lowValue changes, so lowthumb's layoutX, Y is recomputed.
     */
    private void positionLowThumb() {
        RangeSlider s = getSkinnable();
        boolean horizontal = !isVertical();
        double lx = (horizontal) ? trackStart + (((trackLength * ((s.getValue() - s.getMin()) /
                (getMaxMinusMinNoZero()))) - thumbWidth / 2)) : lowThumbPos;
        double ly = (horizontal) ? lowThumbPos :
                getSkinnable().getInsets().getTop() + trackLength - (trackLength * ((s.getValue() - s.getMin()) /
                        (getMaxMinusMinNoZero()))); //  - thumbHeight/2
        lowThumb.setLayoutX(lx);
        lowThumb.setLayoutY(ly);
        if (horizontal) rangeStart = lx + thumbWidth;
        else rangeEnd = ly;
    }

    /**
     * Called when ever either min, max or highValue changes, so highthumb's layoutX, Y is recomputed.
     */
    private void positionHighThumb() {
        RangeSlider slider = getSkinnable();
        boolean orientation = !isVertical();

        double thumbWidth = lowThumb.getWidth();
        double thumbHeight = lowThumb.getHeight();
        highThumb.resize(thumbWidth, thumbHeight);

        double pad = 0;//track.impl_getBackgroundFills() == null || track.impl_getBackgroundFills().length <= 0 ? 0.0D : track.impl_getBackgroundFills()[0].getTopLeftCornerRadius();
        double trackStart = orientation ? track.getLayoutX() : track.getLayoutY();
        trackStart += pad;

        double trackLength = orientation ? track.getWidth() : track.getHeight();
        trackLength -= 2 * pad;

        double x = orientation ? trackStart + (trackLength * ((slider.getHighValue() - slider.getMin()) / (getMaxMinusMinNoZero())) - thumbWidth / 2D) : lowThumb.getLayoutX();
        double y = orientation ? lowThumb.getLayoutY() : (getSkinnable().getInsets().getTop() + trackLength) - trackLength * ((slider.getHighValue() - slider.getMin()) / (getMaxMinusMinNoZero()));
        highThumb.setLayoutX(x);
        highThumb.setLayoutY(y);
        if (orientation) rangeEnd = x;
        else rangeStart = y + thumbHeight;
    }

    @Override
    protected void layoutChildren(final double x, final double y,
                                  final double w, final double h) {
        // resize thumb to preferred size
        thumbWidth = lowThumb.prefWidth(-1);
        thumbHeight = lowThumb.prefHeight(-1);
        lowThumb.resize(thumbWidth, thumbHeight);
        // we are assuming the is common radius's for all corners on the track
        double trackRadius = track.getBackground() == null ? 0 : track.getBackground().getFills().size() > 0 ?
                track.getBackground().getFills().get(0).getRadii().getTopLeftHorizontalRadius() : 0;

        if (isVertical()) {
            double tickLineWidth = (showTickMarks) ? tickLine.prefWidth(-1) : 0;
            double trackWidth = track.prefWidth(-1);
            double trackAreaWidth = Math.max(trackWidth, thumbWidth);
            double totalWidthNeeded = trackAreaWidth + ((showTickMarks) ? trackToTickGap + tickLineWidth : 0);
            double startX = x + ((w - totalWidthNeeded) / 2); // center slider in available width horizontally
            trackLength = h - thumbHeight;
            trackStart = y + (thumbHeight / 2);
            double trackLeft = (int) (startX + ((trackAreaWidth - trackWidth) / 2));
            lowThumbPos = (int) (startX + ((trackAreaWidth - thumbWidth) / 2));

            positionLowThumb();
            // layout track
            track.resizeRelocate(trackLeft, trackStart - trackRadius, trackWidth, trackLength + trackRadius + trackRadius);
            positionHighThumb();
            // layout range bar
            rangeBar.resizeRelocate(trackLeft, rangeStart, trackWidth, rangeEnd - rangeStart);
            // layout tick line
            if (showTickMarks) {
                tickLine.setLayoutX(trackLeft + trackWidth + trackToTickGap);
                tickLine.setLayoutY(trackStart);
                tickLine.resize(tickLineWidth, trackLength);
                tickLine.requestAxisLayout();
            } else {
                if (tickLine != null) {
                    tickLine.resize(0, 0);
                    tickLine.requestAxisLayout();
                }
                tickLine = null;
            }
        } else {
            double tickLineHeight = (showTickMarks) ? tickLine.prefHeight(-1) : 0;
            double trackHeight = track.prefHeight(-1);
            double trackAreaHeight = Math.max(trackHeight, thumbHeight);
            double totalHeightNeeded = trackAreaHeight + ((showTickMarks) ? trackToTickGap + tickLineHeight : 0);
            double startY = y + ((h - totalHeightNeeded) / 2); // center slider in available height vertically
            trackLength = w - thumbWidth;
            trackStart = x + (thumbWidth / 2);
            double trackTop = (int) (startY + ((trackAreaHeight - trackHeight) / 2));
            lowThumbPos = (int) (startY + ((trackAreaHeight - thumbHeight) / 2));

            positionLowThumb();
            // layout track
            track.resizeRelocate(trackStart - trackRadius, trackTop, trackLength + trackRadius + trackRadius, trackHeight);
            positionHighThumb();
            // layout range bar
            rangeBar.resizeRelocate(rangeStart, trackTop, rangeEnd - rangeStart, trackHeight);
            // layout tick line
            if (showTickMarks) {
                tickLine.setLayoutX(trackStart);
                tickLine.setLayoutY(trackTop + trackHeight + trackToTickGap);
                tickLine.resize(trackLength, tickLineHeight);
                tickLine.requestAxisLayout();
            } else {
                if (tickLine != null) {
                    tickLine.resize(0, 0);
                    tickLine.requestAxisLayout();
                }
                tickLine = null;
            }
        }
    }

    private double minTrackLength() {
        return 2 * lowThumb.prefWidth(-1);
    }




    protected NumberAxis createTickLine(RangeSlider sl) {
        if (tickLine == null) {
            tickLine = new NumberAxis();
            tickLine.setFocusTraversable(false);
            tickLine.setAutoRanging(false);
            tickLine.animatedProperty().bind(sl.tickAnimatedProperty());
            tickLine.sideProperty().bind(sideBinding);
            tickLine.upperBoundProperty().bind(sl.maxProperty());
            tickLine.lowerBoundProperty().bind(sl.minProperty());
            tickLine.tickUnitProperty().bind(sl.majorTickUnitProperty());
            tickLine.tickMarkVisibleProperty().bind(sl.showTickMarksProperty());
            tickLine.tickLabelsVisibleProperty().bind(sl.showTickLabelsProperty());
            tickLine.minorTickVisibleProperty().bind(sl.showTickMarksProperty());
            tickLine.minorTickCountProperty().bind(Bindings.max(0, sl.minorTickCountProperty()).add(1));
            tickLine.setTickLabelFormatter(stringConverterWrapper);
        }
        return tickLine;
    }

    protected boolean isVertical() {
        return getSkinnable().getOrientation() == Orientation.VERTICAL;
    }


    @Override
    public void dispose() {
        track.removeEventHandler(MouseEvent.MOUSE_PRESSED, trackPressHandler);
        track.removeEventHandler(MouseEvent.MOUSE_DRAGGED, trackDraggedHandler);
        track.removeEventHandler(MouseEvent.MOUSE_RELEASED, trackReleasedHandler);

        lowThumb.removeEventHandler(MouseEvent.MOUSE_PRESSED, lowThumbPressHandler);
        lowThumb.removeEventHandler(MouseEvent.MOUSE_DRAGGED, lowThumbDraggedHandler);
        lowThumb.removeEventHandler(MouseEvent.MOUSE_RELEASED, lowThumbReleasedHandler);
        lowThumb.removeEventHandler(MouseEvent.MOUSE_ENTERED, lowThumbEnteredHandler);
        lowThumb.removeEventHandler(MouseEvent.MOUSE_EXITED, lowThumbExitedHandler);

        highThumb.removeEventHandler(MouseEvent.MOUSE_PRESSED, highThumbPressHandler);
        highThumb.removeEventHandler(MouseEvent.MOUSE_DRAGGED, highThumbDraggedHandler);
        highThumb.removeEventHandler(MouseEvent.MOUSE_RELEASED, highThumbReleasedHandler);
        highThumb.removeEventHandler(MouseEvent.MOUSE_ENTERED, highThumbEnteredHandler);
        highThumb.removeEventHandler(MouseEvent.MOUSE_EXITED, highThumbExitedHandler);

        rangeBar.removeEventHandler(MouseEvent.MOUSE_PRESSED, rangeBarPressHandler);
        rangeBar.removeEventHandler(MouseEvent.MOUSE_DRAGGED, rangeBarDraggedHandler);
        rangeBar.removeEventHandler(MouseEvent.MOUSE_RELEASED, rangeBarReleasedHandler);

        super.dispose();
    }

    protected static class ThumbPane extends StackPane {

        public void setFakeFocused(boolean value) {
            setFocused(value);
        }

    }

    public enum FocusedChild {
        LOW_THUMB,
        HIGH_THUMB,
        RANGE_BAR,
        NONE
    }

}