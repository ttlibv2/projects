package vn.conyeu.javafx.controls.skin;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import lombok.extern.slf4j.Slf4j;
import vn.conyeu.javafx.bindings.BindingBuilder;
import vn.conyeu.javafx.controls.Badge;
import vn.conyeu.javafx.controls.FlexBox;
import vn.conyeu.javafx.controls.MeterGroup;
import vn.conyeu.javafx.controls.MeterGroup.LabelPos;
import vn.conyeu.javafx.controls.flex.*;
import vn.conyeu.javafx.controls.simple.MeterItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Slf4j
public class MeterGroupSkin extends SimpleSkinBase<MeterGroup> {
    private final Map<MeterItem, Rectangle> meterBars = new HashMap<>();
    private final Map<MeterItem, Node> meterLabelNodes = new HashMap<>();

    private FlexBox groupLabelItem, container;
    private Group groupLine;
    private Rectangle track;

    ObjectBinding<FlexDirection> groupLabelDirectionBinding;

    public MeterGroupSkin(MeterGroup control) {
        super(control);
        initialize(control);
    }

    public void initializeUI(MeterGroup mg) {
        groupLabelDirectionBinding = FlexBox.createDirectionBinding(false, mg.labelOrientationProperty());

        groupLine = new Group();
        groupLine.getStyleClass().setAll("line-group");

        groupLabelItem = new FlexBox(10);
        groupLabelItem.getStyleClass().setAll("label-group");
        groupLabelItem.setFlexWrap(FlexWrap.WRAP);
        groupLabelItem.directionProperty().bind(groupLabelDirectionBinding);

        container = new FlexBox(groupLabelItem, groupLine);
        container.getStyleClass().setAll("container");
        container.setAlignContent(FlexAlignContent.FLEX_START);
        container.setFlexWrap(FlexWrap.NOWRAP);
        container.setDirection(FlexDirection.COLUMN);
        container.setDefaultFlexGrow(0);

        getChildren().setAll(container);

        // setup track
        track = buildRectangle("track");
        track.getStyleClass().setAll("track");

        groupLine.getChildren().add(track);


    }

    @Override
    public void initializeDataUI(MeterGroup control) {
        addMeterItems(control.getItems());
        applyLabelPos(control);
        applyOrientation(control);

    }

    public void initializeListener(MeterGroup control) {
        listChangeListener(control.itemsProperty(), change -> {
            while (change.next()) {
                if (change.wasRemoved()) removeMeterItems(change.getRemoved());
                if (change.wasAdded()) addMeterItems(change.getAddedSubList());
            }
        });
        registerChangeListener(control.minProperty(), e -> applyMin(control));
        registerChangeListener(control.maxProperty(), e -> applyMax(control));
        registerChangeListener(control.labelPosProperty(), e -> applyLabelPos(control));
        registerChangeListener(control.orientationProperty(), e -> applyOrientation(control));
    }

    private void applyOrientation(MeterGroup control) {
        Orientation orientation = control.getOrientation();

        track.widthProperty().unbind();
        track.heightProperty().unbind();

        final Insets insets = control.getInsets();

        if(orientation == Orientation.VERTICAL) {

            track.setWidth(8);
            container.setDirection(FlexDirection.ROW);
            track.heightProperty().bind(control.prefHeightProperty()
                    .subtract(insets.getTop())
                    .subtract(insets.getBottom())
            );
        }
        else {
            track.setHeight(8);
            container.setDirection(FlexDirection.COLUMN);
            track.widthProperty().bind(control.prefWidthProperty()
                    .subtract(insets.getLeft()).subtract(insets.getRight()));
        }

    }

    private void removeMeterItems(List<MeterItem> items) {
        for(MeterItem item:items) {
            Node labelNode = meterLabelNodes.remove(item);
            if(labelNode instanceof MeterItem.MeterItemNode mn) mn.dispose();

            Node barNode = meterBars.remove(item);
            groupLine.getChildren().remove(barNode);
        }

    }

    private void addMeterItems(List<MeterItem> items) {
        for (MeterItem item : items) {

            // create meter label
            Region node = createMeterLabelNode(item);
            groupLabelItem.getChildren().add(node);
            meterLabelNodes.put(item, node);

            // create meter bar
            //Rectangle bar = buildRectangle("bar");
            //bar.strokeProperty().bind(item.colorProperty());
            //bar.widthProperty().bind(container.widthProperty().multiply(computePercent(item.getValue())).divide(100) );

            //groupLine.getChildren().add(bar);
            //meterBars.put(item, bar);

        }
    }

    private void applyMin(MeterGroup control) {}

    private void applyMax(MeterGroup control) {}

    private void applyLabelPos(MeterGroup control) {
        LabelPos labelPos = control.getLabelPos();
        FlexBox.setFlexOrder(groupLabelItem, labelPos == LabelPos.END ? 2 : 1);
        FlexBox.setFlexOrder(groupLine, labelPos == LabelPos.END ? 1 : 2);
    }

    protected Region createMeterLabelNode(MeterItem item) {
        Function<MeterItem, Region> factory = getSkinnable().getLabelFactory();
        if (factory == null) factory = MeterItemNode::new;
        return factory.apply(item);
    }

    /**
     * Responsible for building the track and the bars for the slider.
     */
    protected Rectangle buildRectangle(String styleClass) {
        Rectangle rectangle = new Rectangle();
        rectangle.getStyleClass().setAll(styleClass);
        rectangle.setStroke(Color.valueOf("red"));
        rectangle.setStrokeLineCap(StrokeLineCap.ROUND);
        rectangle.setStrokeLineJoin(StrokeLineJoin.ROUND);
        rectangle.setStrokeType(StrokeType.INSIDE);
        rectangle.setStrokeWidth(0);
        rectangle.setHeight(6);
        return rectangle;
    }

    protected double computePercent(double meter) {
        final MeterGroup sk = getSkinnable();
        final double min = sk.getMin(), max = sk.getMax();
        double percentOfItem = ((meter - min) / (max - min)) * 100;
        return Math.round(Math.max(0, Math.min(100, percentOfItem)));
    }

    @Override
    protected void layoutChildren(double layoutX, double layoutY, double insideWidth, double insideHeight) {
        double barX = 0;
        for(Node node:groupLine.getChildren()) {
            if(node.getStyleClass().contains("bar") && node instanceof Rectangle rec) {
                rec.setLayoutX(barX);
                barX += rec.getWidth();
            }
        }

        container.resizeRelocate(layoutX, layoutY, insideWidth, insideHeight);
    }

    protected class MeterItemNode extends FlexBox implements MeterItem.MeterItemNode {
        private StringBinding percentTextBinding;
        private ObjectBinding<Node> labelGraphicBinding;

        private final MeterItem meterItem;
        private Badge badge;
        private Label label;
        private Label percent;

        protected MeterItemNode(MeterItem meterItem) {
            super(5);
            this.meterItem = meterItem;
            setDirection(FlexDirection.ROW);
            setFlexWrap(FlexWrap.NOWRAP);
            setAlignItem(FlexAlignItem.CENTER);
            setAlignContent(FlexAlignContent.FLEX_START);
            initializeUI();initialzeEvent();
        }

        private void initializeUI() {
            percentTextBinding = BindingBuilder.stringBuilder()
                    .setMapper(() -> "(" + computePercent(meterItem.getValue()) + "%)")
                    .addSources(meterItem.valueProperty()).get();

            labelGraphicBinding = BindingBuilder.<Node>objectBuilder()
                    .setMapper(() -> resolveIcon(meterItem.getIcon(), meterItem.getIconLiteral()))
                    .addSources(meterItem.iconProperty(), meterItem.iconLiteralProperty()).get();


            label = new Label();
            label.textProperty().bind(meterItem.labelProperty());
            label.graphicProperty().bind(labelGraphicBinding);

            percent = new Label();
            percent.textProperty().bind(percentTextBinding);

            idProperty().bind(meterItem.idProperty());
            getStyleClass().addAll(meterItem.getStyleClass());

            FlexBox.setFlexOrder(label, 2);
            FlexBox.setFlexOrder(percent, 3);

            getChildren().setAll(label, percent);

            setupBadgeDot();

        }

        private void initialzeEvent() {
            listChangeListener(meterItem.getStyleClass(), change -> {
                while (change.next()) {
                    if (change.wasRemoved()) getStyleClass().removeAll(change.getRemoved());
                    if (change.wasAdded()) getStyleClass().addAll(change.getAddedSubList());
                }
            });

            registerChangeListener(meterItem.visibleDotProperty(), e -> setupBadgeDot());
        }

        private void setupBadgeDot() {
            if (!meterItem.isVisibleDot()) getChildren().remove(badge);
            else {

                if (badge == null) {
                    badge = new Badge();
                    badge.getStyleClass().add("badge-dot");
                    badge.colorProperty().bind(meterItem.colorProperty());
                    badge.setType(Badge.BadgeType.DOT);
                    FlexBox.setFlexOrder(badge, 1);
                }

                tryAddChildrenNode(getChildren(), badge);
            }
        }

        public void dispose() {
            badge.colorProperty().unbind();
            label.textProperty().unbind();
            label.graphicProperty().unbind();
            percent.textProperty().unbind();
            idProperty().unbind();

            unListChangeListeners(meterItem.getStyleClass());
            unregisterChangeListeners(meterItem.visibleDotProperty());

            getChildren().clear();

            percentTextBinding = null;
            labelGraphicBinding = null;
            badge = null;
            label = null;
            percent = null;
        }

    }

    protected class LineBar extends Group {
        private Rectangle track;

        public LineBar() {
            track = buildRectangle("track");
        }




    }
}