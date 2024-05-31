package vn.conyeu.javafx.controls.skin;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.Axis.TickMark;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.TextBoundsType;
import javafx.util.Duration;
import vn.conyeu.javafx.animation.ripple.RippleGenerator;
import vn.conyeu.javafx.animation.utils.Animations;
import vn.conyeu.javafx.animation.utils.Interpolators;
import vn.conyeu.javafx.animation.utils.KeyFrames;
import vn.conyeu.javafx.beans.DoubleRange;
import vn.conyeu.javafx.beans.NumberRange;
import vn.conyeu.javafx.beans.PositionBean;
import vn.conyeu.javafx.bindings.BindingBuilder;
import vn.conyeu.javafx.icons.font.FontIcon;
import vn.conyeu.javafx.utils.FxUtils;
import vn.conyeu.javafx.utils.NodeUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SliderSkin extends javafx.scene.control.skin.SliderSkin {

    public SliderSkin(Slider control) {
        super(control);
    }
}