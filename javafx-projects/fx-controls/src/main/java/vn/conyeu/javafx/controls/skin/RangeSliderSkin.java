package vn.conyeu.javafx.controls.skin;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.AccessibleRole;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import vn.conyeu.javafx.bindings.BindingBuilder;
import vn.conyeu.javafx.controls.RangeSlider;
import vn.conyeu.javafx.controls.behavior.RangeSliderBehavior;
import vn.conyeu.javafx.utils.CssUtils;
import vn.conyeu.javafx.utils.NodeUtils;

import java.util.function.Function;

import static vn.conyeu.javafx.controls.skin.RangeSliderSkin.FocusedChild.LOW_THUMB;
import static vn.conyeu.javafx.controls.skin.RangeSliderSkin.FocusedChild.NONE;

public class RangeSliderSkin extends SimpleSkinBase<RangeSlider> {

    /**
     * Track if slider is vertical/horizontal and cause re layout
     */
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
    private Node lowThumb, highThumb;
    private StackPane rangeBar; // the bar between the two thumbs, can be dragged

    private Node lowThumbDefault, highThumbDefault;

    // temp fields for mouse drag handling
    private double preDragPos;          // used as a temp value for low and high thumbs
    private Point2D preDragThumbPoint;  // in skin coordinates
    private Callback<Void, FocusedChild> selectedValue;

    private FocusedChild currentFocus = LOW_THUMB;
    private RangeSliderBehavior behavior;

    //== event
    private final EventHandler<MouseEvent> trackPressHandler = this::onTrackPressed;
    private final EventHandler<MouseEvent> trackDraggedHandler = this::onTrackDragged;
    private final EventHandler<MouseEvent> trackReleasedHandler = this::onTrackReleased;

    private final EventHandler<MouseEvent> lowThumbPressHandler = this::onLowThumbPressed;
    private final EventHandler<MouseEvent> lowThumbDraggedHandler = this::onLowThumbDragged;
    private final EventHandler<MouseEvent> lowThumbReleasedHandler = this::onLowThumbReleased;

    private final EventHandler<MouseEvent> highThumbPressHandler = this::highThumbPressed;
    private final EventHandler<MouseEvent> highThumbDraggedHandler = this::highThumbDragged;
    private final EventHandler<MouseEvent> highThumbReleasedHandler = this::highThumbReleased;

    private final EventHandler<MouseEvent> rangeBarPressHandler = this::rangeBarPressed;
    private final EventHandler<MouseEvent> rangeBarDraggedHandler = this::rangeBarDragged;
    private final EventHandler<MouseEvent> rangeBarReleasedHandler = this::rangeBarReleased;

    public RangeSliderSkin(final RangeSlider rangeSlider) {
        super(rangeSlider);
        initialize(rangeSlider);
        behavior = new RangeSliderBehavior(this);
    }

    @Override
    public void initializeUI(RangeSlider rangeSlider) {
        track = new StackPane();
        track.setFocusTraversable(false);
        track.getStyleClass().setAll("track");

        lowThumb = new ThumbPane();
        lowThumb.getStyleClass().setAll("thumb","low-thumb");
        lowThumb.setAccessibleRole(AccessibleRole.THUMB);
        lowThumb.setFocusTraversable(true);

        highThumb = new ThumbPane();
        highThumb.setAccessibleRole(AccessibleRole.THUMB);
        highThumb.getStyleClass().setAll( "thumb","high-thumb");



        getChildren().setAll(track, lowThumb, highThumb);
    }

    @Override
    public void initializeListenerUI(RangeSlider control) {
        track.addEventHandler(MouseEvent.MOUSE_PRESSED, trackPressHandler);
        track.addEventHandler(MouseEvent.MOUSE_DRAGGED, trackDraggedHandler);
        track.addEventHandler(MouseEvent.MOUSE_RELEASED, trackReleasedHandler);

        lowThumb.addEventHandler(MouseEvent.MOUSE_PRESSED, lowThumbPressHandler);
        lowThumb.addEventHandler(MouseEvent.MOUSE_DRAGGED, lowThumbDraggedHandler);
        lowThumb.addEventHandler(MouseEvent.MOUSE_RELEASED, lowThumbReleasedHandler);

        highThumb.addEventHandler(MouseEvent.MOUSE_PRESSED, highThumbPressHandler);
        highThumb.addEventHandler(MouseEvent.MOUSE_DRAGGED, highThumbDraggedHandler);
        highThumb.addEventHandler(MouseEvent.MOUSE_RELEASED, highThumbReleasedHandler);

        registerChangeListener(lowThumb.focusedProperty(), e -> changeFocusedChild());
        registerChangeListener(highThumb.focusedProperty(), e -> changeFocusedChild());

    }

    @Override
    public void initializeDataUI(RangeSlider slider) {
        setSelectedValue(v -> currentFocus);
        setupTickLine();
        setupOrientation(slider);
        setupThumb(slider);
        setupRangeBar(slider);
    }

    @Override
    public void initializeListener(RangeSlider slider) {
        registerChangeListener(slider.valueProperty(), e -> setupLowValue(slider));
        registerChangeListener(slider.highValueProperty(), e -> setupHighValue(slider));
        registerChangeListener(slider.minProperty(), e -> setupMinValue(slider));
        registerChangeListener(slider.maxProperty(), e -> setupMaxValue(slider));
        registerChangeListener(slider.showTickMarksProperty(), e -> setupTickLine());
        registerChangeListener(slider.showTickLabelsProperty(), e -> setupTickLine());
        registerChangeListener(slider.focusedProperty(), e -> setupFocusSkin(slider));
        registerChangeListener(slider.orientationProperty(), e -> setupOrientation(slider));
        registerChangeListener(slider.visibleRangeBarProperty(), e -> setupRangeBar(slider));
        registerChangeListener(slider.thumbFactoryProperty(), e -> setupThumb(slider));
    }

    private void setupThumb(RangeSlider slider) {
        Function<RangeSlider, Node> factory = slider.getThumbFactory();
        if(factory == null) factory = sl -> {
            ThumbPane thumb = new ThumbPane();
            thumb.getStyleClass().setAll("thumb");
            thumb.setAccessibleRole(AccessibleRole.THUMB);
            thumb.setFocusTraversable(true);
            return thumb;
        };

        lowThumb = factory.apply(slider);
        NodeUtils.addStyleClass(lowThumb, "low-thumb");

        highThumb = factory.apply(slider);
        NodeUtils.addStyleClass(highThumb, "high-thumb");

        tryAddChildrenNode(lowThumb);
        tryAddChildrenNode(highThumb);

    }

    private void setupRangeBar(RangeSlider slider) {
        if(!slider.isVisibleRangeBar()) {
            getChildren().remove(rangeBar);
            removeEventHandlerRangeBar();
        }
        else {

            if(rangeBar == null) {
                rangeBar = new StackPane();
                rangeBar.setFocusTraversable(false);
                rangeBar.cursorProperty().bind(new ObjectBinding<>() {
                    {
                        bind(rangeBar.hoverProperty());
                    }

                    @Override
                    protected Cursor computeValue() {
                        return rangeBar.isHover() ? Cursor.HAND : Cursor.DEFAULT;
                    }
                });
                rangeBar.getStyleClass().setAll("range-bar");

                rangeBar.addEventHandler(MouseEvent.MOUSE_PRESSED, rangeBarPressHandler);
                rangeBar.addEventHandler(MouseEvent.MOUSE_DRAGGED, rangeBarDraggedHandler);
                rangeBar.addEventHandler(MouseEvent.MOUSE_RELEASED, rangeBarReleasedHandler);
                registerChangeListener(rangeBar.focusedProperty(), e -> changeFocusedChild());
            }

            tryAddChildrenNode(rangeBar);
        }
    }

    private void setupOrientation(RangeSlider slider) {
        final boolean isHorizontal = isHorizontal();
        CssUtils.setPseudoHorizontal(slider, isHorizontal);
        CssUtils.setPseudoVertical(slider, !isHorizontal);
    }

    private void setupFocusSkin(RangeSlider slider) {
        if (slider.isFocused()) {
           // lowThumb.setFocus(true);
        } else {
           // lowThumb.setFocus(false);
           // highThumb.setFocus(false);
            currentFocus = NONE;
        }
    }

    private void setupMaxValue(RangeSlider slider) {
        slider.requestLayout();
    }

    private void setupMinValue(RangeSlider slider) {
        slider.requestLayout();
    }

    private void setupHighValue(RangeSlider slider) {
        positionHighThumb();

        if(hasAddRangeBar()) {
            if (isHorizontal()) {
                rangeBar.resize(rangeEnd - rangeStart, rangeBar.getHeight());
            } else {
                rangeBar.resizeRelocate(rangeBar.getLayoutX(), rangeStart,
                        rangeBar.getWidth(), rangeEnd - rangeStart);
            }
        }
    }

    private void setupLowValue(RangeSlider slider) {
        positionLowThumb();
        if(hasAddRangeBar()) {
            if (isHorizontal()) {
                rangeBar.resizeRelocate(rangeStart, rangeBar.getLayoutY(),
                        rangeEnd - rangeStart, rangeBar.getHeight());
            } else {
                rangeBar.resize(rangeBar.getWidth(), rangeEnd - rangeStart);
            }
        }
    }




    private void changeFocusedChild() {
        if (lowThumb.isFocused()) currentFocus = FocusedChild.LOW_THUMB;
        else if (highThumb.isFocused()) currentFocus = FocusedChild.HIGH_THUMB;
        else if (hasAddRangeBar() && rangeBar.isFocused()) currentFocus = FocusedChild.RANGE_BAR;
    }

    /**
     * When ticks or labels are changing of visibility, we compute the new
     * visibility and add the necessary objets. After this method, we must be
     * sure to add the high Thumb and the rangeBar.
     */
    private void setupTickLine() {
        boolean ticksVisible = getSkinnable().isShowTickMarks();
        boolean labelsVisible = getSkinnable().isShowTickLabels();

        showTickMarks = (ticksVisible || labelsVisible);
        RangeSlider rangeSlider = getSkinnable();
        if (showTickMarks) {
            tickLine = createTickLine(rangeSlider);
            tryAddChildrenNode(tickLine);
        } else {
            getChildren().remove(tickLine);
            tickLine = null;

        }

        getSkinnable().requestLayout();
    }


    protected NumberAxis createTickLine(RangeSlider sl) {
        if (tickLine == null) {

            ObjectBinding<Side> sideBinding = BindingBuilder.<Side>objectBuilder()
                    .setMapper(() -> isHorizontal() ? Side.BOTTOM : Side.RIGHT)
                    .addSources(sl.orientationProperty()).get();

            tickLine = new NumberAxis();
            tickLine.setFocusTraversable(false);
            tickLine.setAutoRanging(false);
            tickLine.setAnimated(true);
            tickLine.sideProperty().bind(sideBinding);
            tickLine.upperBoundProperty().bind(sl.maxProperty());
            tickLine.lowerBoundProperty().bind(sl.minProperty());
            tickLine.tickUnitProperty().bind(sl.majorTickUnitProperty());
            tickLine.tickMarkVisibleProperty().bind(sl.showTickMarksProperty());
            tickLine.tickLabelsVisibleProperty().bind(sl.showTickLabelsProperty());
            tickLine.minorTickVisibleProperty().bind(sl.showTickMarksProperty());
            tickLine.minorTickCountProperty().bind(Bindings.max(0, sl.minorTickCountProperty()).add(1));
            //tickLine.setTickLabelFormatter(stringConverterWrapper);
        }
        return tickLine;
    }

    /**
     * Call when `track` on `MOUSE_PRESSED`
     */
    protected void onTrackPressed(MouseEvent me) {
        if (!lowThumb.isPressed() && !highThumb.isPressed()) {
            if (isHorizontal()) {
                behavior.trackPress(me, (me.getX() / trackLength));
            } else {
                behavior.trackPress(me, (me.getY() / trackLength));
            }
        }
    }

    /**
     * Call when `track` on `MOUSE_DRAGGED`
     */
    protected void onTrackDragged(MouseEvent me) {

    }

    /**
     * Call when `track` on `MOUSE_RELEASED`
     */
    protected void onTrackReleased(MouseEvent me) {
        // Nothing being done with the second param in sliderBehavior
        // So, passing a dummy value
        behavior.trackRelease(me, 0.0f);
    }

    /**
     * Call when `lowThumb` on `MOUSE_PRESSED`
     */
    protected void onLowThumbPressed(MouseEvent me) {
       // highThumb.setFocus(false);
      //  lowThumb.setFocus(true);
        behavior.lowThumbPressed(me, 0.0f);
        preDragThumbPoint = lowThumb.localToParent(me.getX(), me.getY());
        preDragPos = (getSkinnable().getValue() - getSkinnable().getMin()) /
                (getMaxMinusMinNoZero());
    }

    /**
     * Call when `lowThumb` on `MOUSE_DRAGGED`
     */
    protected void onLowThumbDragged(MouseEvent me) {
        Point2D cur = lowThumb.localToParent(me.getX(), me.getY());
        if (preDragThumbPoint == null) {
            preDragThumbPoint = cur;
        }
        double dragPos = (isHorizontal()) ?
                cur.getX() - preDragThumbPoint.getX() : -(cur.getY() - preDragThumbPoint.getY());
        behavior.lowThumbDragged(me, preDragPos + dragPos / trackLength);
    }

    /**
     * Call when `lowThumb` on `MOUSE_RELEASED`
     */
    protected void onLowThumbReleased(MouseEvent event) {
        behavior.lowThumbReleased(event);
    }


    /**
     * Call when `highThumb` on `MOUSE_PRESSED`
     */
    protected void highThumbPressed(MouseEvent e) {
        //lowThumb.setFocus(false);
        //highThumb.setFocus(true);
        behavior.highThumbPressed(e, 0.0D);
        preDragThumbPoint = highThumb.localToParent(e.getX(), e.getY());
        preDragPos = (getSkinnable().getHighValue() - getSkinnable().getMin()) / (getMaxMinusMinNoZero());
    }

    /**
     * Call when `highThumb` on `MOUSE_DRAGGED`
     */
    protected void highThumbDragged(MouseEvent e) {
        boolean orientation = getSkinnable().getOrientation() == Orientation.HORIZONTAL;
        double trackLength = orientation ? track.getWidth() : track.getHeight();

        Point2D point2d = highThumb.localToParent(e.getX(), e.getY());
        if (preDragThumbPoint == null) {
            preDragThumbPoint = point2d;
        }
        double d = getSkinnable().getOrientation() != Orientation.HORIZONTAL ? -(point2d.getY() - preDragThumbPoint.getY()) : point2d.getX() - preDragThumbPoint.getX();
        behavior.highThumbDragged(e, preDragPos + d / trackLength);
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
    protected void rangeBarPressed(MouseEvent e) {
        if(hasAddRangeBar() ) rangeBar.requestFocus();
        preDragPos = isHorizontal() ? e.getX() : -e.getY();
    }

    /**
     * Call when `rangeBar` on `MOUSE_DRAGGED`
     */
    protected void rangeBarDragged(MouseEvent e) {
        double delta = (isHorizontal() ? e.getX() : -e.getY()) - preDragPos;
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
        boolean horizontal = isHorizontal();
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
        RangeSlider slider = (RangeSlider) getSkinnable();
        boolean orientation = ((RangeSlider) getSkinnable()).getOrientation() == Orientation.HORIZONTAL;

        double thumbWidth = lowThumb.prefWidth(-1);
        double thumbHeight = lowThumb.prefHeight(-1);
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

    protected void layoutHorizontal(final double trackRadius, final double x, final double y, final double w, final double h) {
        double tickLineHeight = showTickMarks ? tickLine.prefHeight(-1) : 0;
        double trackHeight = snapSizeY(track.prefHeight(-1));
        double trackAreaHeight = Math.max(trackHeight, thumbHeight);
        double totalHeightNeeded = trackAreaHeight + (showTickMarks ? trackToTickGap + tickLineHeight : 0);
        double startY = y + ((h - totalHeightNeeded) / 2); // center slider in available height vertically
        double trackTop = startY + ((trackAreaHeight - trackHeight) / 2);

        trackLength = snapSizeX(w - thumbWidth);
        trackStart =snapPositionX( x + (thumbWidth / 2));
        lowThumbPos = startY + ((trackAreaHeight - thumbHeight) / 2);

        positionLowThumb();

        // layout track
        track.resizeRelocate(trackStart - trackRadius, trackTop, trackLength + trackRadius * 2, trackHeight);

        positionHighThumb();

        // layout range bar
        if(getChildren().contains(rangeBar)) {
            rangeBar.resizeRelocate(rangeStart, trackTop, rangeEnd - rangeStart, trackHeight);
        }

        // layout tick line
        if (showTickMarks) {
            tickLine.setLayoutX(trackStart);
            tickLine.setLayoutY(trackTop + trackHeight + trackToTickGap);
            tickLine.resize(trackLength, tickLineHeight);
            tickLine.requestAxisLayout();
        }

    }

    protected void layoutChildren(final double x, final double y, final double w, final double h) {
        // resize thumb to preferred size
        thumbWidth = lowThumb.prefWidth(-1);
        thumbHeight = lowThumb.prefHeight(-1);
        lowThumb.resize(thumbWidth, thumbHeight);

        // we are assuming the is common radius's for all corners on the track
        double trackRadius = track.getBackground() == null ? 0 : track.getBackground().getFills().size() > 0 ?
                track.getBackground().getFills().get(0).getRadii().getTopLeftHorizontalRadius() : 0;

        if (isHorizontal()) layoutHorizontal(trackRadius, x, y, w, h);
        else {
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
            if(hasAddRangeBar()) {
                rangeBar.resizeRelocate(trackLeft, rangeStart, trackWidth, rangeEnd - rangeStart);
            }

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
        }
    }

    private boolean hasAddRangeBar() {
        return getChildren().contains(rangeBar);
    }

    private double minTrackLength() {
        return 2 * lowThumb.prefWidth(-1);
    }

    @Override
    protected double computeMinWidth(double height, double top, double right, double bottom, double left) {
        final double width = isHorizontal() ? minTrackLength() + lowThumb.minWidth(-1) : lowThumb.prefHeight(-1);
        return snapSizeX(left + right + width);
    }

    @Override
    protected double computeMinHeight(double width, double top, double right, double bottom, double left) {
        final double height = lowThumb.prefHeight(-1) + (isHorizontal() ? 0 : minTrackLength());
        return snapSizeY(top + bottom + height);
    }

    @Override
    protected double computePrefWidth(double height, double top, double right, double bottom, double left) {
        if (isHorizontal()) return showTickMarks ? Math.max(140, tickLine.prefWidth(-1)) : 140;
        else {
            final double lowWidth = lowThumb.prefWidth(-1);
            final double trackWidth = track.prefWidth(-1);
            final double tickGap = showTickMarks ? trackToTickGap + tickLine.prefWidth(-1) : 0;
            return left + Math.max(lowWidth, trackWidth) + tickGap + right;
        }
    }

    @Override
    protected double computePrefHeight(double width, double top, double right, double bottom, double left) {
        if (!isHorizontal()) return showTickMarks ? Math.max(140, tickLine.prefHeight(-1)) : 140;
        else {
            final double lowHeight = lowThumb.prefHeight(-1);
            final double trackHeight = track.prefHeight(-1);
            final double tickGap = showTickMarks ? trackToTickGap + tickLine.prefHeight(-1) : 0;
            return top + Math.max(lowHeight, trackHeight) + tickGap + bottom;
        }
    }

    @Override
    protected double computeMaxWidth(double height, double top, double right, double bottom, double left) {
        return !isHorizontal() ? getSkinnable().prefWidth(-1) : Double.MAX_VALUE;
    }

    @Override
    protected double computeMaxHeight(double width, double top, double right, double bottom, double left) {
        return !isHorizontal() ? Double.MAX_VALUE : getSkinnable().prefHeight(width);
    }

    @Override
    public void dispose() {
        track.removeEventHandler(MouseEvent.MOUSE_PRESSED, trackPressHandler);
        track.removeEventHandler(MouseEvent.MOUSE_DRAGGED, trackDraggedHandler);
        track.removeEventHandler(MouseEvent.MOUSE_RELEASED, trackReleasedHandler);

        lowThumb.removeEventHandler(MouseEvent.MOUSE_PRESSED, lowThumbPressHandler);
        lowThumb.removeEventHandler(MouseEvent.MOUSE_DRAGGED, lowThumbDraggedHandler);
        lowThumb.removeEventHandler(MouseEvent.MOUSE_RELEASED, lowThumbReleasedHandler);

        highThumb.removeEventHandler(MouseEvent.MOUSE_PRESSED, highThumbPressHandler);
        highThumb.removeEventHandler(MouseEvent.MOUSE_DRAGGED, highThumbDraggedHandler);
        highThumb.removeEventHandler(MouseEvent.MOUSE_RELEASED, highThumbReleasedHandler);

        removeEventHandlerRangeBar();

        behavior.dispose();

        super.dispose();
    }

    private void removeEventHandlerRangeBar() {
        if(hasAddRangeBar()) {
            rangeBar.removeEventHandler(MouseEvent.MOUSE_PRESSED, rangeBarPressHandler);
            rangeBar.removeEventHandler(MouseEvent.MOUSE_DRAGGED, rangeBarDraggedHandler);
            rangeBar.removeEventHandler(MouseEvent.MOUSE_RELEASED, rangeBarReleasedHandler);
            unregisterChangeListeners(rangeBar.focusedProperty());
        }

    }
    private boolean isHorizontal() {
        RangeSlider slider = getSkinnable();
        return slider.getOrientation() == null || slider.getOrientation() == Orientation.HORIZONTAL;
    }


    public Callback<Void, FocusedChild> getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(Callback<Void, FocusedChild> c) {
        selectedValue = c;
    }

    private static class ThumbPane extends StackPane {

        public void setFocus(boolean value) {
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