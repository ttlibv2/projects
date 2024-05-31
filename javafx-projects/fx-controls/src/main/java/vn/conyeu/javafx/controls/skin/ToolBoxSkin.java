package vn.conyeu.javafx.controls.skin;

import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import lombok.extern.slf4j.Slf4j;
import vn.conyeu.javafx.controls.FlexBox;
import vn.conyeu.javafx.controls.IconWrapper;
import vn.conyeu.javafx.controls.ToolBox;
import vn.conyeu.javafx.controls.flex.FlexAlignItem;
import vn.conyeu.javafx.controls.flex.FlexWrap;

@Slf4j
public class ToolBoxSkin extends SimpleSkinBase<ToolBox> {
    private FlexBox container;
    private HBox leadingBox;
    private HBox trailingBox;
    private Label label;
    private IconWrapper graphicWrapper;
    private IconWrapper closeWrapper;

    public ToolBoxSkin(ToolBox control) {
        super(control);
        initialize(control);
    }

    @Override
    public void initializeUI(ToolBox c) {
        container = new FlexBox();
        container.getStyleClass().setAll("container");
        container.setFlexWrap(FlexWrap.NOWRAP);
        container.setAlignItem(FlexAlignItem.CENTER);

        leadingBox = new HBox(5);
        leadingBox.setAlignment(Pos.CENTER_LEFT);
        leadingBox.getStyleClass().setAll("leading-box");
        leadingBox.spacingProperty().bind(c.leadingSpacingProperty());

        trailingBox = new HBox(5);
        trailingBox.setAlignment(Pos.CENTER_RIGHT);
        trailingBox.getStyleClass().setAll("trailing-box");
        trailingBox.spacingProperty().bind(c.trailingSpacingProperty());

        label = new Label();
        label.textProperty().bind(c.labelProperty());

        closeWrapper = new IconWrapper();
        closeWrapper.getStyleClass().addAll("close-wrapper");
        closeWrapper.setCursor(Cursor.HAND);
        //closeWrapper.getRippleGenerator().setClipSupplier(()-> Ripple.ClipType.CIRCLE.createFactory().offsetW(10).offsetH(10).build(closeWrapper));
        closeWrapper.enableRippleGenerator(true);

        graphicWrapper = new IconWrapper();
        graphicWrapper.getStyleClass().addAll("graphic-rapper");
        graphicWrapper.enableRippleGenerator(true);

        FlexBox.setFlexOrder(graphicWrapper, 1);
        FlexBox.setFlexOrder(leadingBox, 2);
        FlexBox.setFlexOrder(label, 3);
        FlexBox.setFlexOrder(trailingBox, 4);
        FlexBox.setFlexOrder(closeWrapper, 5);

        FlexBox.setFlexGrow(label, 1);

        Bindings.bindContentBidirectional(leadingBox.getChildren(), c.leadingNodesProperty());
        Bindings.bindContentBidirectional(trailingBox.getChildren(), c.trailingNodesProperty());

        getChildren().setAll(container);
        tryAddChild(label);

        //--
        setupGraphicIcon(c);
        setupCloseNode(c);
        setupLeadingNodes(c);
        setupTrailingNodes(c);
    }


    @Override
    public void initializeListener(ToolBox c) {
        registerChangeListener(c.labelProperty(), e -> {
        });
        registerChangeListener(c.closeNodeProperty(), e -> setupCloseNode(c));
        registerChangeListener(c.visibleCloseProperty(), e -> setupCloseNode(c));
        registerChangeListener(c.graphicProperty(), e -> setupGraphicIcon(c));
        registerListChangeListener(leadingBox.getChildren(), e -> {
            if (isEmptyNode(leadingBox)) removeChild(leadingBox);
            else tryAddChild(leadingBox);
        });

        registerListChangeListener(trailingBox.getChildren(), e -> {
            if (isEmptyNode(trailingBox)) removeChild(trailingBox);
            else tryAddChild(trailingBox);
        });

    }

    private void setupLeadingNodes(ToolBox c) {
        if (isEmptyNode(leadingBox)) removeChild(leadingBox);
        else tryAddChild(leadingBox);
    }

    private void setupTrailingNodes(ToolBox c) {
        if (isEmptyNode(trailingBox)) removeChild(trailingBox);
        else tryAddChild(trailingBox);
    }

    private void setupCloseNode(ToolBox c) {
        Node node = c.getCloseNode();
        if (node == null || !c.isVisibleClose()) removeChild(closeWrapper);
        else {
            closeWrapper.setIcon(node);
            tryAddChild(closeWrapper);
        }
    }

    private void setupGraphicIcon(ToolBox c) {
        Node graphic = c.getGraphic();
        if (graphic == null) removeChild(graphicWrapper);
        else {
            graphicWrapper.setIcon(graphic);
            tryAddChild(graphicWrapper);
        }
    }


    void tryAddChild(Node child) {
        if (!container.getChildren().contains(child)) {
            container.getChildren().add(child);
        }
    }

    void removeChild(Node... children) {
        container.getChildren().removeAll(children);
    }

    boolean isEmptyNode(Pane region) {
        return region.getChildren().isEmpty();
    }


    protected void layoutChildren(double x, double y, double w, double h) {
        container.resizeRelocate(x, y, w, h);
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return snapSizeX(container.prefWidth(height)) + leftInset + rightInset;
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        final double height = container.prefHeight(width) + topInset + bottomInset;
        return snapSizeY(height) + topInset + bottomInset;
    }

}