package vn.conyeu.javafx.controls.behavior;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.TwoLevelFocusBehavior;
import com.sun.javafx.scene.control.inputmap.InputMap;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.scene.control.Skin;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;
import vn.conyeu.javafx.controls.RangeSlider;
import vn.conyeu.javafx.controls.skin.RangeSliderSkin;
import vn.conyeu.javafx.controls.skin.RangeSliderSkin.FocusedChild;
import vn.conyeu.javafx.utils.FxUtils;

@Slf4j
public class RangeSliderBehavior extends BehaviorBase<RangeSlider> {
    private final InputMap<RangeSlider> sliderInputMap;
    private TwoLevelFocusBehavior tlFocus;
    private final RangeSlider slider;
    private final RangeSliderSkin skin;

    public RangeSliderBehavior(RangeSliderSkin skin) {
        super(skin.getSkinnable());
        this.slider = getNode();
        this.skin = skin;

        // create a map for slider-specific mappings (this reuses the default
        // InputMap installed on the control, if it is non-null, allowing us to pick up any user-specified mappings)
        sliderInputMap = createInputMap();

        // then slider-specific mappings for key input
        addDefaultMapping(sliderInputMap,
                new InputMap.KeyMapping(KeyCode.HOME, KeyEvent.KEY_RELEASED, e -> home()),
                new InputMap.KeyMapping(KeyCode.END, KeyEvent.KEY_RELEASED, e -> end())
        );

        // we split the rest of the mappings into vertical and horizontal slider
        // child input maps

        // -- horizontal
        InputMap<RangeSlider> horizontalMappings = new InputMap<>(slider);
        horizontalMappings.setInterceptor(e -> slider.getOrientation() != Orientation.HORIZONTAL);
        horizontalMappings.getMappings().addAll(
                // we use the rtl method to translate depending on the RTL state of the UI
                new InputMap.KeyMapping(KeyCode.LEFT, e -> rtl(this::incrementValue, this::decrementValue)),
                new InputMap.KeyMapping(KeyCode.KP_LEFT, e -> rtl(this::incrementValue, this::decrementValue)),
                new InputMap.KeyMapping(KeyCode.RIGHT, e -> rtl(this::decrementValue, this::incrementValue)),
                new InputMap.KeyMapping(KeyCode.KP_RIGHT, e -> rtl(this::decrementValue, this::incrementValue))
        );

        addDefaultChildMap(sliderInputMap, horizontalMappings);

        // -- vertical
        InputMap<RangeSlider> verticalMappings = new InputMap<>(slider);
        verticalMappings.setInterceptor(e -> slider.getOrientation() != Orientation.VERTICAL);
        verticalMappings.getMappings().addAll(
                new InputMap.KeyMapping(KeyCode.DOWN, e -> decrementValue()),
                new InputMap.KeyMapping(KeyCode.KP_DOWN, e -> decrementValue()),
                new InputMap.KeyMapping(KeyCode.UP, e -> incrementValue()),
                new InputMap.KeyMapping(KeyCode.KP_UP, e -> incrementValue())
        );
        addDefaultChildMap(sliderInputMap, verticalMappings);

        // Only add this if we're on an embedded platform that supports 5-button navigation
        if (FxUtils.isTwoLevelFocus()) {
            tlFocus = new TwoLevelFocusBehavior(slider); // needs to be last.
        }
    }

    /**
     * Invoked by the Slider {@link Skin} implementation whenever a mouse press
     * occurs on the "track" of the slider. This will cause the thumb to be
     * moved by some amount.
     *
     * @param position The mouse position on track with 0.0 being beginning of
     *                 track and 1.0 being the end
     */
    public void trackPress(MouseEvent e, double position) {

        if (!slider.isFocused()) slider.requestFocus();

        final double min = slider.getMin(), max = slider.getMax();
        final double pos = slider.isVertical() ? (1 - position) : position;
        final double newPosition = pos * (max - min) + min;

        // If the position is inferior to the current LowValue, this means
        // the user clicked on the track to move the low thumb. If not, then
        // it means the user wanted to move the high thumb.
        if (newPosition < slider.getValue()) slider.adjustLowValue(newPosition);
        else slider.adjustHighValue(newPosition);
    }

    public void trackRelease(MouseEvent e, double position) {
    }

    /**
     * @param position The mouse position on track with 0.0 being beginning of
     *                 track and 1.0 being the end
     */
    public void lowThumbPressed(MouseEvent e, double position) {
        if (!slider.isFocused()) slider.requestFocus();
        slider.setValueChanging(true);
    }

    /**
     * @param position The mouse position on track with 0.0 being beginning of
     *                 track and 1.0 being the end
     */
    public void lowThumbDragged(MouseEvent me, double position) {
        final double min = slider.getMin(), max = slider.getMax();
        slider.setValue(FxUtils.clamp(position * (max - min), min, max));
    }

    /**
     * When lowThumb is released valueChanging should be set to false.
     */
    public void lowThumbReleased(MouseEvent e) {
        slider.setValueChanging(false);

        // RT-15207 When snapToTicks is true, slider value calculated in drag
        // is then snapped to the nearest tick on mouse release.
        if (slider.isSnapToTicks()) {
            // RT-15207 When snapToTicks is true, slider value calculated in drag
            // is then snapped to the nearest tick on mouse release.
            slider.setValue(slider.snapValueToTicks(slider.getValue()));
        }

    }

    // when high thumb is released, highValueChanging is set to false.
    public void highThumbReleased(MouseEvent e) {
        slider.setHighValueChanging(false);
        if (slider.isSnapToTicks()) {
            slider.setHighValue(slider.snapValueToTicks(slider.getHighValue()));
        }
    }

    public void highThumbPressed(MouseEvent e, double position) {
        if (!slider.isFocused()) slider.requestFocus();
        slider.setHighValueChanging(true);
    }

    public void highThumbDragged(MouseEvent me, double position) {
        final double min = slider.getMin(), max = slider.getMax();
        final double value = position * (max - min) + min;
        slider.setHighValue(FxUtils.clamp(value, min, max));
    }

    public void moveRange(double position) {
        final double min = slider.getMin(), max = slider.getMax();
        final double total = slider.isVertical() ? slider.getHeight() : slider.getWidth();
        final double lowValue = FxUtils.clamp(min, slider.getValue() + position * (max - min) / total, max);
        final double highValue = FxUtils.clamp(min, slider.getHighValue() + position * (max - min) / total, max);
        if (lowValue > min && highValue < max) {
            slider.setValueChanging(true);
            slider.setHighValueChanging(true);
            slider.setValue(lowValue);
            slider.setHighValue(highValue);
        }
    }

    public void confirmRange() {
        slider.setValueChanging(false);
        if (slider.isSnapToTicks()) {
            slider.adjustLowValue(slider.getValue());
        }

        slider.setHighValueChanging(false);
        if (slider.isSnapToTicks()) {
            slider.adjustHighValue(slider.getHighValue());
        }
    }

    public void home() {
        slider.adjustHighValue(slider.getMin());
    }

    public void end() {
        RangeSlider slider = getNode();
        slider.adjustHighValue(slider.getMax());
    }

    public void decrementValue() {
        Callback<Void, FocusedChild> selectedValue = skin.getSelectedValue();

        if (selectedValue != null) {
            if (selectedValue.call(null) == FocusedChild.HIGH_THUMB) {
                if (slider.isSnapToTicks())
                    slider.adjustHighValue(slider.getHighValue() - computeIncrement());
                else
                    slider.decrementHighValue();
            } else {
                if (slider.isSnapToTicks())
                    slider.adjustLowValue(slider.getValue() - computeIncrement());
                else
                    slider.decrementLowValue();
            }
        }
    }

    public void incrementValue() {
        Callback<Void, FocusedChild> selectedValue = skin.getSelectedValue();
        if (selectedValue != null) {
            if (selectedValue.call(null) == FocusedChild.HIGH_THUMB) {
                if (slider.isSnapToTicks())
                    slider.adjustHighValue(slider.getHighValue() + computeIncrement());
                else
                    slider.incrementHighValue();
            } else {
                if (slider.isSnapToTicks())
                    slider.adjustLowValue(slider.getValue() + computeIncrement());
                else
                    slider.incrementLowValue();
            }
        }
    }

    public double computeIncrement() {
        double d;
        if (slider.getMinorTickCount() != 0)
            d = slider.getMajorTickUnit() / (double) (Math.max(slider.getMinorTickCount(), 0) + 1);
        else
            d = slider.getMajorTickUnit();
        if (slider.getBlockIncrement() > 0.0D && slider.getBlockIncrement() < d)
            return d;
        else
            return slider.getBlockIncrement();
    }

    public void rtl(Runnable rtlMethod, Runnable nonRtlMethod) {
        if (slider.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
            rtlMethod.run();
        } else {
            nonRtlMethod.run();
        }
    }


    @Override
    public InputMap<RangeSlider> getInputMap() {
        return sliderInputMap;
    }

    @Override
    public void dispose() {
        if (tlFocus != null) tlFocus.dispose();
        super.dispose();
    }

}