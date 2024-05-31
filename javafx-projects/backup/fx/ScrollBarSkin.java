package vn.conyeu.javafx.controls.skin;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.css.PseudoClass;
import javafx.geometry.*;
import javafx.scene.layout.Region;
import vn.conyeu.javafx.beans.DoubleRange;
import vn.conyeu.javafx.beans.PositionBean;
import vn.conyeu.javafx.bindings.BindingBuilder;
import vn.conyeu.javafx.collections.When;
import vn.conyeu.javafx.controls.IconWrapper;
import vn.conyeu.javafx.controls.ScrollBar;
import vn.conyeu.javafx.utils.FxUtils;
import vn.conyeu.javafx.utils.Layouts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScrollBarSkin extends SimpleSkinBase<ScrollBar> {
    private static final double DEFAULT_LENGTH = 100.0;
    private static final PseudoClass VERTICAL_PS = PseudoClass.getPseudoClass("vertical");
    private static final PseudoClass HORIZONTAL_PS = PseudoClass.getPseudoClass("horizontal");
    private static final PseudoClass FOCUS_WITHIN_PS = PseudoClass.getPseudoClass("focus-within");
    protected List<When<?>> whens = new ArrayList<>();

    private Region track, thumb;
    private IconWrapper decIcon, incIcon;
    private double trackLength, thumbLength;
    private boolean vamAdjust, barPressed;

    private DoubleBinding thumbPosBinding;
    private DoubleBinding trackLengthBinding;
    private BooleanBinding visibleSkinBinding;

    public ScrollBarSkin(ScrollBar control) {
        super(control);
        initializeAll(control);
    }

    protected void initializeUI(ScrollBar c) {

        track = new Region();
        track.getStyleClass().add("track");

        thumb = new Region();
        thumb.getStyleClass().add("thumb");

        decIcon = new IconWrapper()
                .addStyleClasses("decrement-icon")
                .enableRippleGenerator(true);

        incIcon = new IconWrapper()
                .addStyleClasses("increment-icon")
                .enableRippleGenerator(true);

        thumbPosBinding = BindingBuilder.doubleBuilder()
                .setMapper(() -> valToPos(c.getValue()))
                .addSources(thumb.widthProperty(), thumb.heightProperty())
                .addSources(c.valueProperty()).get();

        trackLengthBinding = BindingBuilder.doubleBuilder()
                .setMapper(() -> isVertical() ? track.getHeight() : track.getWidth())
                .addSources(track.widthProperty(), track.heightProperty())
                .addSources(c.orientationProperty()).get();

        visibleSkinBinding = BindingBuilder.booleanBuilder()
                .setMapper(() -> c.getVisibleAmount() < 1.0)
                .addSources(c.visibleAmountProperty()).get();

        c.thumbPosProperty().bind(thumbPosBinding);
        c.trackLengthProperty().bind(trackLengthBinding);
        c.visibleProperty().bind(visibleSkinBinding);
    }

    protected boolean isVertical() {
        return getSkinnable().getOrientation() == Orientation.VERTICAL;
    }

    @Override
    protected void initializeDataUI(ScrollBar c) {
        setupOrientation();
        updateChildren();
        //c.setValue(c.getValue()); // Adjusts the value if min and max are not 0.0/1.0
        //initBehavior();
    }

    protected void initializeListener(ScrollBar sb) {
        registerInvalidationListener(sb.minProperty(), e -> reLayout());
        registerInvalidationListener(sb.maxProperty(), e -> reLayout());
        registerInvalidationListener(sb.visibleAmountProperty(), e -> reLayout());
        registerInvalidationListener(sb.buttonsGapProperty(), e -> reLayout());
        registerInvalidationListener(sb.buttonsVisibleProperty(), e -> {
            updateChildren();
            thumbPosBinding.invalidate();
            sb.requestLayout();
        });
        registerInvalidationListener(sb.orientationProperty(), e -> {
            setupOrientation();
            sb.requestLayout();
        });

        registerChangeListener(decIcon.focusedProperty(), e -> {
            pseudoClassStateChanged(FOCUS_WITHIN_PS, decIcon.isFocused());
        });

        registerChangeListener(incIcon.focusedProperty(), e -> {
            pseudoClassStateChanged(FOCUS_WITHIN_PS, incIcon.isFocused());
        });

        registerChangeListener(track.layoutBoundsProperty(), e-> {
            Bounds bounds = track.getLayoutBounds();
            double width = bounds.getWidth(), height = bounds.getHeight();
            if(width > 0 && height > 0) thumbPosBinding.invalidate();
        });
    }

    private void reLayout() {
        if (vamAdjust) vamAdjust = false;
        else getSkinnable().requestLayout();
    }

    /**
     * This is responsible for updating the children list according to the {@link ScrollBar#buttonsVisibleProperty()}.
     */
    private void updateChildren() {
        boolean buttonsVisible = getSkinnable().isButtonsVisible();
        if (buttonsVisible) getChildren().setAll(track, thumb, decIcon, incIcon);
        else getChildren().setAll(track, thumb);
    }

    /**
     * This is responsible for updating the ":vertical" and ":horizontal" PseudoClass states,
     * as well as create the right binding for the thumb position every time the scroll bar's orientation
     * change.
     */
    private void setupOrientation() {

        if (isVertical()) {
            pseudoClassStateChanged(HORIZONTAL_PS, false);
            pseudoClassStateChanged(VERTICAL_PS, true);
            thumb.translateXProperty().unbind();
            thumb.translateYProperty().bind(thumbPosBinding);
        } //
        else {
            pseudoClassStateChanged(VERTICAL_PS, false);
            pseudoClassStateChanged(HORIZONTAL_PS, true);
            thumb.translateYProperty().unbind();
            thumb.translateXProperty().bind(thumbPosBinding);
        }
    }

    /**
     * This core method is responsible for converting the {@link ScrollBar#valueProperty()} to the coordinate
     * (x or y depending on the orientation) at which the thumb must be positioned.
     */
    private double valToPos(double val) {
        ScrollBar sb = getSkinnable();
        double min = sb.getMin();
        double max = sb.getMax();
        assert val >= min && val <= max;

        return FxUtils.mapOneRangeToAnother(val,
                DoubleRange.of(min, max),
                DoubleRange.of(0.0, trackLength - thumbLength)
        );
    }

    /**
     * Responsible for computing the scroll bar's length (width for VERTICAL, height for HORIZONTAL).
     */
    private double getLength() {
        ScrollBar sb = getSkinnable();
        double length, padding;
        if (isVertical()) {
            padding = snappedLeftInset() + snappedRightInset();
            length = Layouts.boundWidth(thumb) + padding;
        } else {
            padding = snappedTopInset() + snappedBottomInset();
            length = Layouts.boundHeight(thumb) + padding;
        }

        if (sb.isButtonsVisible())
            length = Math.max(length, Math.max(decIcon.getSize(), incIcon.getSize()) + padding);
        return length;
    }

    /**
     * @return the minimum allowed track length, 2 * {@link #getLength()}
     */
    private double minTrackLength() {
        return getLength() * 2.0;
    }

    /**
     * @return the minimum allowed thumb length, 1.5 * {@link #getLength()}
     */
    private double minThumbLength() {
        return getLength() * 1.5;
    }

    /**
     * @return the visible portion as {@link ScrollBar} / (max - min)
     */
    private double visiblePortion() {
        ScrollBar sb = getSkinnable();
        return sb.getVisibleAmount() / (sb.getMax() - sb.getMin());
    }

    @Override
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double buttonsGap = getSkinnable().isButtonsVisible() ? getSkinnable().getButtonsGap() : 0.0;
        return isVertical()? getLength() : leftInset + decIcon.getSize() + minTrackLength() + incIcon.getSize() + rightInset + (buttonsGap * 2);
    }

    @Override
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        double buttonsGap = getSkinnable().isButtonsVisible() ? getSkinnable().getButtonsGap() : 0.0;
        return isVertical() ? topInset + decIcon.getSize() + minTrackLength() + incIcon.getSize() + bottomInset + (buttonsGap * 2) : getLength();
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double buttonsGap = getSkinnable().isButtonsVisible() ? getSkinnable().getButtonsGap() : 0.0;
        return isVertical() ? getLength() : leftInset + DEFAULT_LENGTH + rightInset + (buttonsGap * 2);
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        double buttonsGap = getSkinnable().isButtonsVisible() ? getSkinnable().getButtonsGap() : 0.0;
        return isVertical() ? topInset + DEFAULT_LENGTH + bottomInset + (buttonsGap * 2) : getLength();
    }

    @Override
    protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return isVertical() ? getSkinnable().prefWidth(-1) : Double.MAX_VALUE;
    }

    @Override
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return isVertical() ? Double.MAX_VALUE :  getSkinnable().prefHeight(-1);
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        ScrollBar sb = getSkinnable();

        boolean buttonsVisible = sb.isButtonsVisible();
        double buttonsGap = buttonsVisible ? sb.getButtonsGap() : 0.0;
        boolean isVer = isVertical();

        double decSize = buttonsVisible ? decIcon.getSize() + buttonsGap : 0.0;
        double incSize = buttonsVisible ? incIcon.getSize() + buttonsGap : 0.0;

        DoubleProperty visibleAmountProperty = sb.visibleAmountProperty();

        if (buttonsVisible) {
            HPos decHPos = isVer ? HPos.CENTER : HPos.LEFT;
            HPos incHPos = isVer ? HPos.CENTER : HPos.RIGHT;
            VPos decVPos = isVer ? VPos.TOP : VPos.CENTER;
            VPos incVPos = isVer ? VPos.BOTTOM : VPos.CENTER;
            layoutInArea(decIcon, x, y, w, h, 0, decHPos, decVPos);
            layoutInArea(incIcon, x, y, w, h, 0, incHPos, incVPos);
        }

        double visiblePortion = visiblePortion();
        if (isVer) {
            trackLength = snapSizeY(h - (decSize + incSize));
            layoutInArea(track, x, y + decSize, w, trackLength, 0, HPos.CENTER, VPos.TOP);

            thumbLength = snapSizeY(FxUtils.clamp(trackLength * visiblePortion, minThumbLength(), trackLength));
            double visibleAmount = (thumbLength * (sb.getMax() - sb.getMin())) / trackLength;

            if (sb.getVisibleAmount() != visibleAmount && !visibleAmountProperty.isBound()) {
                vamAdjust = true;
                visibleAmountProperty.set(visibleAmount);
            }

            PositionBean position = Layouts.computePosition(
                    sb, thumb,
                    x, y + decSize, w, thumbLength, 0,
                    Insets.EMPTY, HPos.CENTER, VPos.TOP,
                    false, true
            );
            double thumbW = Layouts.boundWidth(thumb);
            thumb.resizeRelocate(position.getX(), position.getY(), thumbW, thumbLength);
        } //
        else {
            trackLength = snapSizeX(w - (decSize + incSize));
            layoutInArea(track, x + decSize, y, trackLength, h, 0, HPos.LEFT, VPos.CENTER);

            thumbLength = snapSizeX(FxUtils.clamp(trackLength * visiblePortion, minThumbLength(), trackLength));
            double visibleAmount = (thumbLength * (sb.getMax() - sb.getMin())) / trackLength;
            if (sb.getVisibleAmount() != visibleAmount && !visibleAmountProperty.isBound()) {
                vamAdjust = true;
                visibleAmountProperty.set(visibleAmount);
            }

            PositionBean position = Layouts.computePosition(
                    sb, thumb,
                    x + decSize, y, thumbLength, h, 0,
                    Insets.EMPTY, HPos.LEFT, VPos.CENTER,
                    false, true
            );
            double thumbH = Layouts.boundWidth(thumb);
            thumb.resizeRelocate(position.getX(), position.getY(), thumbLength, thumbH);
        }
    }

    @Override
    public void dispose() {
       // MFXScrollBarBehavior behavior = getSkinnable().getBehavior();
        //if (behavior != null) behavior.dispose();

        thumb.translateXProperty().unbind();
        thumb.translateYProperty().unbind();
        thumbPosBinding = null;
        super.dispose();
    }
}