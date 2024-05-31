package vn.conyeu.javafx.controls;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.css.*;
import javafx.scene.control.Skin;
import vn.conyeu.javafx.controls.skin.ScrollBarSkin;
import vn.conyeu.javafx.styleable.CssFactory;

import java.util.List;

/**
 * Implementation of a scroll bar following material design guidelines in JavaFX.
 * <p>
 * In addition to an appealing style, {@code MFXScrollBar} offers many new features compared
 * to the boring standard JavaFX' scroll bar, such as:
 * <p> - The ability to show/hide the increase/decrease buttons through a property
 * <p> - The ability to specify the gap between the buttons and the thumb
 * <p> - Inbuilt smooth scroll for the thumb and the track
 * <p> All of the above feature (and also every other not mentioned here) can also be changed trough CSS.
 * <p></p>
 * {@code MFXScrollBar} also offers 4 new PseudoClasses for CSS:
 * <p> - ":dragging": active when the thumb is being dragged
 * <p> - ":horizontal": active when the scroll bar's orientation is HORIZONTAL
 * <p> - ":vertical": active when the scroll bar's orientation is VERTICAL
 * <p> - ":focus-within": active when any of the internal components of the scroll bar is focused
 * <p></p>
 * {@code MFXScrollBar} is also the first control to use the new behavior API, so you can easily change
 * the behavior of the control by creating your own behavior and set it trough {@code #behaviorProperty()}.
 * <p></p>
 * <b>NOTE:</b> there is an important difference between this and the standard JavaFX' scroll bar.
 * Both allow to set a minimum and a maximum value, <b>BUT</b>, for {@code MFXScrollBar} these values
 * are capped to 0.0 (min) and 1.0 (max).
 * <p>
 * The idea here is to make the scroll bar work on "percentage values". 0.0 the thumb is all the way up, 1.0 the thumb
 * is all the way down, as simple as that. It would make little to no sense to make a scroll bar work on pixels
 * value, since the scroll bar alone is a pretty useless control. Once you combine it with other controls (for example
 * in a scroll pane) it's your job to tweak the scrolling. In such cases you typically want to scroll by a certain amount
 * of pixels which means that the {@code #unitIncrementProperty()} and {@code #trackIncrementProperty()} must be determined
 * in percentage values.
 * <p>
 * A simple example: if I have a content of 12000px and I want to scroll 20px (for unit increment) and 80px (for track increment),
 * in a viewport of 1000px, we must compute both the increments as follows:
 * <p> ScrollableAmount = ContentLength - ViewportLength = 11000px;
 * <p> UnitIncrement = 20px / ScrollableAmount = 0.001818182;
 * <p> TrackIncrement = 80px / ScrollableAmount = 0.007272727;
 */
public class ScrollBar extends javafx.scene.control.ScrollBar {
    private static final PseudoClass DRAGGING_PS = PseudoClass.getPseudoClass("dragging");

    public ScrollBar() {
    }

    //@formatter:off

    /**
     * This property tracks the position of the thumb along the track.
     */
    private DoubleProperty thumbPos;
    public final double getThumbPos() {return thumbPosProperty().get();}
    public final void setThumbPos(double value) {thumbPosProperty().set(value);}
    public final DoubleProperty thumbPosProperty() {
        if(thumbPos == null) {
            thumbPos = new SimpleDoubleProperty(this, "thumbPos");
        }
        return thumbPos;
    }

    /**
     * This property tracks the length of the scroll bar's track.
     */
    private DoubleProperty trackLength;
    public final double getTrackLength() {return trackLengthProperty().get();}
    public final void setTrackLength(double value) {trackLengthProperty().set(value);}
    public final DoubleProperty trackLengthProperty() {
        if(trackLength == null) {
            trackLength = new SimpleDoubleProperty(this, "trackLength");
        }
        return trackLength;
    }

    /**
     * Specifies whether the scroll bar's thumb is being dragged.
     */
    private BooleanProperty dragging;
    public final boolean isDragging() {return draggingProperty().get();}
    public final void setDragging(boolean value) {draggingProperty().set(value);}
    public final BooleanProperty draggingProperty() {
        if(dragging == null) {
            dragging = new SimpleBooleanProperty(this, "dragging"){
                @Override
                protected void invalidated() {
                    pseudoClassStateChanged(DRAGGING_PS, get());
                }
            };
        }
        return dragging;
    }

    /**
     * Specifies whether the increase/decrease buttons are visible.
     */
    private StyleableBooleanProperty buttonsVisible;
    public final boolean isButtonsVisible() {return buttonsVisibleProperty().get();}
    public final void setButtonsVisible(boolean value) {buttonsVisibleProperty().set(value);}
    public final StyleableBooleanProperty buttonsVisibleProperty() {
        if(buttonsVisible == null) {
            buttonsVisible = FACTORY.createBoolean(this, "buttonsVisible", "-fx-buttons-visible");
        }
        return buttonsVisible;
    }

    /**
     * Specifies the gap between the increase/decrease buttons and the thumb.
     */
    private StyleableDoubleProperty buttonsGap;
    public final void setButtonsGap(double value) {buttonsGapProperty().set(value);}
    public final double getButtonsGap() {return buttonsGapProperty().get();}
    public final StyleableDoubleProperty buttonsGapProperty() {
        if(buttonsGap == null) {
            buttonsGap = FACTORY.createDouble(this, "buttonsGap", "-fx-buttons-gap");
        }
        return buttonsGap;
    }

    /**
     * Specifies the amount added/subtracted to the {@link #valueProperty()} used by the
     * scroll bar's track.
     */
    private StyleableDoubleProperty trackIncrement;
    public final void setTrackIncrement(double value) {trackIncrementProperty().set(value);}
    public final double getTrackIncrement() {return trackIncrementProperty().get();}
    public final StyleableDoubleProperty trackIncrementProperty() {
        if(trackIncrement == null) {
            trackIncrement = FACTORY.createDouble(this, "trackIncrement", "-fx-track-increment");
        }
        return trackIncrement;
    }

    /**
     * Specifies whether the scrolling should be smooth, done by animations.
     */
    private StyleableBooleanProperty smoothScroll;
    public final boolean isSmoothScroll() {return smoothScrollProperty().get();}
    public final void setSmoothScroll(boolean value) {smoothScrollProperty().set(value);}
    public final StyleableBooleanProperty smoothScrollProperty() {
        if(smoothScroll == null) {
            smoothScroll = FACTORY.createBoolean(this, "smoothScroll", "-fx-smooth-scroll");
        }
        return smoothScroll;
    }

    /**
     * Specifies if the scrolling made by the track should be smooth, done by animations.
     * <p></p>
     * The default behavior considers this feature an addition to the {@link #smoothScrollProperty()},
     * meaning that for this to work the aforementioned feature must be enabled too.
     */
    private StyleableBooleanProperty trackSmoothScroll;
    public final boolean isTrackSmoothScroll() {return trackSmoothScrollProperty().get();}
    public final void setTrackSmoothScroll(boolean value) {trackSmoothScrollProperty().set(value);}
    public final StyleableBooleanProperty trackSmoothScrollProperty() {
        if(trackSmoothScroll == null) {
            trackSmoothScroll = FACTORY.createBoolean(this, "trackSmoothScroll", "-fx-track-smooth-scroll");
        }
        return trackSmoothScroll;
    }

    /** incrementIcon */
    private StyleableStringProperty incrementIcon;
    public final String getIncrementIcon() {return incrementIconProperty().get();}
    public final void setIncrementIcon(String value) {incrementIconProperty().set(value);}
    public final StyleableStringProperty incrementIconProperty() {
        if(incrementIcon == null) {
            incrementIcon = FACTORY.createString(this, "incrementIcon", "-fx-increment-icon");
        }
        return incrementIcon;
    }

    /** decrementIcon */
    private StyleableStringProperty decrementIcon;
    public final String getDecrementIcon() {return decrementIconProperty().get();}
    public final void setDecrementIcon(String value) {decrementIconProperty().set(value);}
    public final StyleableStringProperty decrementIconProperty() {
        if(decrementIcon == null) {
            decrementIcon = FACTORY.createString(this, "decrementIcon", "-fx-decrement-icon");
        }
        return decrementIcon;
    }



    //@formatter:on

    protected Skin<?> createDefaultSkin() {
        return new ScrollBarSkin(this);
    }

    /**
     * {@inheritDoc}
     */
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    /**
     * Gets the {@code CssMetaData} associated with this class, which may include the {@code CssMetaData} of its superclasses.
     *
     * @return the {@code CssMetaData}
     */
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return FACTORY.getCssMetaData();
    }

    /**
     * Methods for creating instances of StyleableProperty
     */
    static final CssFactory<ScrollBar> FACTORY = new CssFactory<>(javafx.scene.control.ScrollBar.getClassCssMetaData()) {
        @Override
        protected void registerCss() {
            FACTORY.createBooleanCss("-fx-buttons-visible", ScrollBar::buttonsVisibleProperty, false);
            FACTORY.createSizeCss("-fx-buttons-gap", ScrollBar::buttonsGapProperty, 3);
            FACTORY.createSizeCss("-fx-track-increment", ScrollBar::trackIncrementProperty, .1);
            FACTORY.createBooleanCss("-fx-smooth-scroll", ScrollBar::smoothScrollProperty, false);
            FACTORY.createBooleanCss("-fx-track-smooth-scroll", ScrollBar::trackSmoothScrollProperty, false);
            FACTORY.createStringCss("-fx-increment-icon", ScrollBar::incrementIconProperty);
            FACTORY.createStringCss("-fx-decrement-icon", ScrollBar::decrementIconProperty);


        }
    };

}