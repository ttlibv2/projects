package vn.conyeu.javafx.controls.skin;

import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import lombok.extern.slf4j.Slf4j;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.javafx.controls.FlexBox;
import vn.conyeu.javafx.controls.IconWrapper;
import vn.conyeu.javafx.controls.InputGroup;
import vn.conyeu.javafx.controls.flex.FlexAlignItem;
import vn.conyeu.javafx.controls.flex.FlexDirection;
import vn.conyeu.javafx.controls.flex.FlexJustify;
import vn.conyeu.javafx.controls.flex.FlexWrap;
import vn.conyeu.javafx.icons.core.SimpleIcon;
import vn.conyeu.javafx.utils.CssUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class InputGroupSkin<S extends InputGroup<N>, N extends TextInputControl> extends SimpleSkinBase<S> {
    final Map<Node, Node> allNodes = new HashMap<>();

    private FlexBox container;
    private FlexBox centerBox;
    private HBox leftBox, rightBox;
    // private StackPane inputBox;

    private HBox leadingBox, trailingBox;
    private TextInputControl textInput;

    public InputGroupSkin(S control) {
        super(control);
        initialize(control);
    }

    public void initializeUI(S control) {

        // create centerBox
        //inputBox = new StackPane();
        //inputBox.getStyleClass().setAll("input-wrapper");
        //inputBox.setAlignment(Pos.CENTER_LEFT);

        leadingBox = new HBox();
        leadingBox.getStyleClass().setAll("leading-box");
        leadingBox.setAlignment(Pos.CENTER_LEFT);
        leadingBox.spacingProperty().bind(control.leadingSpacingProperty());

        trailingBox = new HBox();
        trailingBox.getStyleClass().setAll("trailing-box");
        trailingBox.setAlignment(Pos.CENTER_RIGHT);
        trailingBox.spacingProperty().bind(control.trailingSpacingProperty());

        //-- left + right

        leftBox = new HBox();
        leftBox.getStyleClass().setAll("left-box");
        leftBox.setAlignment(Pos.CENTER_LEFT);
        leftBox.spacingProperty().bind(control.leftSpacingProperty());

        centerBox = createFlexBox();
        centerBox.getStyleClass().setAll("center-box");
        centerBox.setAlignItem(FlexAlignItem.STRETCH);
        centerBox.hgapProperty().bind(control.inputSpacingProperty());


        rightBox = new HBox();
        rightBox.getStyleClass().setAll("right-box");
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        rightBox.spacingProperty().bind(control.rightSpacingProperty());

        // order for suffix + prefix
        FlexBox.setFlexOrder(leftBox, 1);
        FlexBox.setFlexOrder(centerBox, 2);
        FlexBox.setFlexOrder(rightBox, 3);


        // order for input nodes
        FlexBox.setFlexOrder(leadingBox, 1);
        //   FlexBox.setFlexOrder(inputBox, 2);
        FlexBox.setFlexOrder(trailingBox, 3);

        FlexBox.setFlexGrow(centerBox, 1);
        //  FlexBox.setFlexGrow(inputBox, 1);

        // setup container
        container = createFlexBox(centerBox);
        container.setAlignItem(FlexAlignItem.STRETCH);
        getChildren().setAll(container);
    }



    @Override
    public void initializeDataUI(S control) {
        setupInputNode(control);
        for (Node node : control.getLeftNodes()) addWrapperIcon(leftBox, node);
        for (Node node : control.getRightNodes()) addWrapperIcon(rightBox, node);
        for (Node node : control.getLeadingNodes()) addWrapperIcon(leadingBox, node);
        for (Node node : control.getTrailingNodes()) addWrapperIcon(trailingBox, node);

    }

    @Override
    public void initializeListener(S control) {
        registerChangeListener(control.inputProperty(), e -> setupInputNode(control));

        registerListChangeListener(leftBox.getChildren(), change -> {
            int result = container.addOrRemoveChild(leftBox);
            CssUtils.setPseudo(control, "has-left", result != -1);
        });

        registerListChangeListener(rightBox.getChildren(), e -> {
            int result = container.addOrRemoveChild(rightBox);
            CssUtils.setPseudo(control, "has-right", result != -1);
        });

        registerListChangeListener(leadingBox.getChildren(), e -> {
            int result = centerBox.addOrRemoveChild(leadingBox);
            CssUtils.setPseudo(control, "has-leading", result != -1);
        });

        registerListChangeListener(trailingBox.getChildren(), e -> {
            int result = centerBox.addOrRemoveChild(trailingBox);
            CssUtils.setPseudo(control, "has-trailing", result != -1);
        });

        registerListChangeListener(control.leftNodesProperty(), change -> {
            listNodeChange(leftBox, change);
        });

        registerListChangeListener(control.rightNodesProperty(), change -> {
            listNodeChange(rightBox, change);
        });

        registerListChangeListener(control.leadingNodesProperty(), change -> {
            listNodeChange(leadingBox, change);
        });

        registerListChangeListener(control.trailingNodesProperty(), change -> {
            listNodeChange(trailingBox, change);
        });

    }

    private FlexBox createFlexBox(Node... children) {
        FlexBox flex = new FlexBox(0, 0, children);
        flex.getStyleClass().clear();
        flex.setDirection(FlexDirection.ROW);
        flex.setFlexWrap(FlexWrap.NOWRAP);
        flex.setFlexJustify(FlexJustify.FLEX_START);
        flex.setAlignItem(FlexAlignItem.CENTER);
        flex.setDefaultFlexShrink(1);
        return flex;
    }

    private void addWrapperIcon(Pane parent, Node childNode) {

        if (allNodes.containsKey(childNode)) {
            throw Objects.newIllegal("The parent `%s` exist child `%s`", parent, childNode.getStyleClass());
        }

        Node wraper = allNodes.computeIfAbsent(childNode, n -> childNode instanceof SimpleIcon ? new IconWrapper(childNode) : childNode);
        if (wraper instanceof IconWrapper) wraper.setCursor(Cursor.HAND);
        parent.getChildren().add(wraper);
    }

    private void setupInputNode(InputGroup<N> c) {

        if (textInput != null) {
            listener.unregisterChangeListeners(textInput.focusedProperty());
        }

        textInput = getSkinnable().getInput();
        if (textInput != null) {

            listener.registerChangeListener(textInput.focusedProperty(), (obs, ov, focus) -> {
                centerBox.handleFocus(focus);
            });

            if(textInput instanceof TextField field) field.setPrefColumnCount(5);
            else if(textInput instanceof TextArea area) area.setPrefColumnCount(5);

            FlexBox.setFlexOrder(textInput, 2);
            FlexBox.setFlexGrow(textInput, 1);
            textInput.getStyleClass().setAll("input");
            textInput.setMinWidth(c.getMinWidthInput());

            centerBox.getChildren().add(textInput);

            //throw new IllegalArgumentException("The input must be not null");
        }

    }

    private void listNodeChange(Pane pane, ListChangeListener.Change change) {
        while (change.next()) {

            if (change.wasRemoved()) {
                List<Node> nodes = change.getRemoved();
                for (Node node : nodes) {
                    Node wrapper = allNodes.containsKey(node) ? allNodes.remove(node) : node;
                    pane.getChildren().remove(wrapper);
                }
            }

            if (change.wasAdded()) {
                List<Node> nodes = change.getAddedSubList();
                for (Node node : nodes) {
                    addWrapperIcon(pane, node);
                }

            }
        }
    }

    @Override
    protected void layoutChildren(double layoutX, double layoutY, double insideWidth, double insideHeight) {
        container.resizeRelocate(layoutX, layoutY, insideWidth, insideHeight);
    }

    @Override
    protected double computePrefHeight(double width, double top, double right, double bottom, double left) {
        return snapSizeY(container.prefHeight(width) + top + bottom);
    }

    @Override
    protected double computePrefWidth(double height, double top, double right, double bottom, double left) {
        return snapSizeX(container.prefWidth(height) + left + right);
    }

    @Override
    protected double computeMinWidth(double height, double top, double right, double bottom, double left) {
        return snapSizeX(container.minWidth(height) + left + right);
    }

    @Override
    public void dispose() {
        allNodes.clear();
        super.dispose();
    }
}