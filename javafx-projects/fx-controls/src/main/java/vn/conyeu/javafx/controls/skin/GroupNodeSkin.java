package vn.conyeu.javafx.controls.skin;

import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import vn.conyeu.javafx.controls.FlexBox;
import vn.conyeu.javafx.controls.GroupNode;
import vn.conyeu.javafx.controls.flex.FlexAlignItem;
import vn.conyeu.javafx.controls.flex.FlexWrap;
import vn.conyeu.javafx.utils.CssUtils;

public class GroupNodeSkin extends SimpleSkinBase<GroupNode> {
    private FlexBox container, centerGroup;
    private FlexBox prefixBox, suffixBox;
    private FlexBox leadingBox, trailingBox;
    private FlexBox inputBox;

    public GroupNodeSkin(GroupNode control) {
        super(control);
        initialize(control);
    }

    public void initializeUI(GroupNode c) {
        container = createFlexBox("container");
        centerGroup = createFlexBox("group");
        prefixBox = createFlexBox("prefix-box");
        suffixBox = createFlexBox("suffix-box");
        leadingBox = createFlexBox("leading-box");
        trailingBox = createFlexBox("trailing-box");
        inputBox = createFlexBox("input-box");

        //flex-order
        FlexBox.setFlexOrder(prefixBox, 1);
        FlexBox.setFlexOrder(centerGroup, 2);
        FlexBox.setFlexOrder(suffixBox, 3);

        FlexBox.setFlexOrder(leadingBox, 1);
        FlexBox.setFlexOrder(inputBox, 2);
        FlexBox.setFlexOrder(trailingBox, 3);

        //flex-grow
        FlexBox.setFlexGrow(centerGroup, 1);
        FlexBox.setFlexGrow(inputBox, 1);

        getChildren().setAll(container);
    }

    @Override
    public void initializeDataUI(GroupNode c) {
        prefixBox.getChildren().setAll(c.getPrefixNodes());
        suffixBox.getChildren().setAll(c.getSuffixNodes());
        leadingBox.getChildren().setAll(c.getLeadingNodes());
        trailingBox.getChildren().setAll(c.getTrailingNodes());
        inputBox.getChildren().setAll(c.getCenterNodes());
    }

    public void initializeListener(GroupNode c) {

        listChangeListener(container.getChildren(), e ->  {
            CssUtils.setPseudo(c, "has-prefix", container.getChildren().contains(prefixBox));
            CssUtils.setPseudo(c, "has-suffix", container.getChildren().contains(suffixBox));
            CssUtils.setPseudo(c, "has-center", container.getChildren().contains(centerGroup));
        });

        listChangeListener(centerGroup.getChildren(), e ->  {
            CssUtils.setPseudo(c, "has-leading", centerGroup.getChildren().contains(leadingBox));
            CssUtils.setPseudo(c, "has-trailing", centerGroup.getChildren().contains(trailingBox));
            CssUtils.setPseudo(c, "has-input", centerGroup.getChildren().contains(inputBox));
        });

        listChangeListener(prefixBox.getChildren(), e ->  container.addOrRemoveChild(prefixBox));
        listChangeListener(suffixBox.getChildren(), e ->  container.addOrRemoveChild(suffixBox));
        listChangeListener(centerGroup.getChildren(), e ->  container.addOrRemoveChild(centerGroup));

        listChangeListener(leadingBox.getChildren(), e ->  centerGroup.addOrRemoveChild(leadingBox));
        listChangeListener(trailingBox.getChildren(), e ->  centerGroup.addOrRemoveChild(trailingBox));
        listChangeListener(inputBox.getChildren(), e ->  centerGroup.addOrRemoveChild(inputBox));

        listChangeListener(c.prefixNodesProperty(), change -> applyListChange(prefixBox, change));
        listChangeListener(c.suffixNodesProperty(), change -> applyListChange(suffixBox, change));
        listChangeListener(c.leadingNodesProperty(), change -> applyListChange(leadingBox, change));
        listChangeListener(c.trailingNodesProperty(), change -> applyListChange(trailingBox, change));
        listChangeListener(c.centerNodesProperty(), change -> applyListChange(inputBox, change));
    }


    private void applyListChange(Pane pane, ListChangeListener.Change<Node> change) {
        while (change.next()) {
            if (change.wasRemoved()) pane.getChildren().removeAll(change.getRemoved());
            if (change.wasAdded()) pane.getChildren().addAll(change.getAddedSubList());
        }
    }

    private FlexBox createFlexBox(String styleClass) {
        FlexBox flex = new FlexBox();
        flex.getStyleClass().setAll(styleClass);
        flex.setAlignItem(FlexAlignItem.CENTER);
        flex.setFlexWrap(FlexWrap.NOWRAP);
        flex.setDefaultFlexShrink(1);
        return flex;
    }

    @Override
    protected void layoutChildren(double layoutX, double layoutY, double insideWidth, double insideHeight) {
        container.resizeRelocate(layoutX, layoutY, insideWidth, insideHeight);
    }

    @Override
    protected double computePrefWidth(double height, double top, double right, double bottom, double left) {
        return snapSizeX(container.prefWidth(height) + left + right);
    }

    @Override
    protected double computePrefHeight(double width, double top, double right, double bottom, double left) {
        return snapSizeY(container.prefHeight(width) + top + bottom);
    }
}