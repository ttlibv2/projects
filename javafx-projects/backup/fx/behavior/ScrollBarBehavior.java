package vn.conyeu.javafx.controls.skin.behavior;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.inputmap.InputMap;
import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.geometry.Orientation;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import vn.conyeu.javafx.animation.utils.Animations;
import vn.conyeu.javafx.animation.utils.KeyFrames;
import vn.conyeu.javafx.beans.DoubleRange;
import vn.conyeu.javafx.controls.ScrollBar;
import vn.conyeu.javafx.utils.FxUtils;
import vn.conyeu.javafx.utils.ScrollUtils.ScrollDirection;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Concrete implementation of {@code BehaviorBase}.
 * <p></p>
 * This offers all the methods to manage scrolling and smooth scrolling, track press/release,
 * buttons press, thumb press/drag/release
 * <p></p>
 */
public class ScrollBarBehavior extends BehaviorBase<ScrollBar> {

    @Override
    public InputMap<ScrollBar> getInputMap() {
        return null;
    }
}