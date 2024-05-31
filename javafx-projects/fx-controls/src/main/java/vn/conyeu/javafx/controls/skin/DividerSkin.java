package vn.conyeu.javafx.controls.skin;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.javafx.bindings.BindingBuilder;
import vn.conyeu.javafx.controls.Divider;
import vn.conyeu.javafx.controls.FlexBox;
import vn.conyeu.javafx.controls.flex.FlexAlignContent;
import vn.conyeu.javafx.controls.flex.FlexAlignItem;
import vn.conyeu.javafx.controls.flex.FlexWrap;
import vn.conyeu.javafx.utils.CssUtils;
import vn.conyeu.javafx.utils.StyleUtils;

import java.util.Optional;
import java.util.function.Consumer;

public class DividerSkin extends SimpleSkinBase<Divider> {
    private FlexBox container;
    private Region firstLine, lastLine;
    private Label label;

    private ObjectBinding<Node> graphicBinding;
    private BooleanBinding visibleLabel;

    public DividerSkin(Divider control) {
        super(control);
        initialize(control);
    }

    public void initializeUI(Divider c) {

        graphicBinding = BindingBuilder.<Node>objectBuilder()
                .addSources(c.graphicProperty(), c.graphicLiteralProperty())
                .setMapper(() -> {
                    Node graphic = c.getGraphic();
                    String literal = c.getGraphicLiteral();
                    return graphic != null ? graphic : literal != null ? resolveIcon(literal) : null;
                }).get();

        visibleLabel = BindingBuilder.booleanBuilder()
                .setMapper(() -> (graphicBinding.get() != null || Objects.notBlank(c.getLabel())) && c.isVisibleLabel() )
                .addSources(graphicBinding, c.labelProperty(), c.visibleLabelProperty()).get();

        container = new FlexBox();
        container.setFlexWrap(FlexWrap.NOWRAP);
        container.setAlignItem(FlexAlignItem.CENTER);
        container.setAlignContent(FlexAlignContent.CENTER);
        container.setDefaultFlexShrink(1);
        container.setDefaultFlexGrow(1);

        firstLine = new Region();
        firstLine.getStyleClass().addAll("line", "first-line");

        lastLine = new Region();
        lastLine.getStyleClass().addAll("line", "last-line");

        label = new Label(c.getLabel());
        label.textProperty().bind(c.labelProperty());
        label.graphicProperty().bind(graphicBinding);
        label.graphicTextGapProperty().bind(c.graphicGapProperty());

        FlexBox.setFlexOrder(firstLine, 1);
        FlexBox.setFlexOrder(label, 2);
        FlexBox.setFlexOrder(lastLine, 3);
        FlexBox.setFlexGrow(label, 0);

        container.getChildren().setAll(firstLine, label, lastLine);
        getChildren().setAll(container);

    }

    @Override
    public void initializeDataUI(Divider control) {
        changeVisibleLabel();
        changeVertical(control);
        changeLabelGap(control);
        changeLinePercent(control);
        changeStyleForLine(control);
        changeLineColor(control, 0);
    }

    @Override
    public void initializeListener(Divider c) {
        registerChangeListener(c.labelGapProperty(), e -> changeLabelGap(c));
        registerChangeListener(c.labelPosProperty(), e -> changeLinePercent(c));
        registerChangeListener(c.linePercentProperty(), e -> changeLinePercent(c));
        registerChangeListener(c.verticalProperty(), e -> changeVertical(c));
        registerChangeListener(c.lineStyleProperty(), e -> changeStyleForLine(c));
        registerChangeListener(c.lineWeightProperty(), e -> changeStyleForLine(c));
        registerChangeListener(c.lineColorProperty(), e -> changeLineColor(c, 0));
        registerChangeListener(c.leftLineColorProperty(), e -> changeLineColor(c,1));
        registerChangeListener(c.rightLineColorProperty(), e -> changeLineColor(c,2));

        registerChangeListener(visibleLabel, e -> changeVisibleLabel());
    }

    private void changeLineColor(Divider c, int pos) {
        final Paint color = c.getLineColor();

        if(pos == 0 || pos == 1) {
            final Paint fc = Optional.ofNullable(c.getLeftLineColor()).orElse(color);
            StyleUtils.of(c).addTag("-first-line-color", fc).apply();
        }

        if(pos == 0 || pos == 2) {
            final Paint lc = Optional.ofNullable(c.getRightLineColor()).orElse(color);
            StyleUtils.of(c).addTag("-last-line-color", lc).apply();
        }
    }

    private boolean isVisibleLabel() {
        return visibleLabel.get();
    }

    private void changeStyleForLine(Divider c) {

        Consumer<Node> consumer = node -> StyleUtils.of(node)
                .addTag("-fx-border-style", c.getLineStyle())
                .addTag("-fx-border-width", c.getLineWeight())
                .apply();

        consumer.accept(firstLine);
        consumer.accept(lastLine);
    }

    private void changeVisibleLabel() {
        container.addOrRemoveChild(label, isVisibleLabel());
        changeLinePercent(getSkinnable());
    }

    private void changeVertical(Divider c) {
        CssUtils.setPseudo(c, "vertical", c.isVertical());
        CssUtils.setPseudo(c, "horizontal", !c.isVertical());
        container.setVertical(c.isVertical());
    }

    private void changeLabelGap(Divider c) {
        final double gap = c.getLabelGap();
        final boolean isVer = c.isVertical();

        final Insets margin = gap <= 0
                ? Insets.EMPTY : c.isVertical()
                ? new Insets(gap, 0, gap, 0)
                : new Insets(0, gap, 0, gap);

        FlexBox.setMargin(label, margin);
    }

    private void changeLinePercent(Divider divider) {
        final Pos labelPos = getSkinnable().getLabelPos();
        final HPos labelHpos = labelPos.getHpos();
        final VPos labelVPos = labelPos.getVpos();

        if(labelHpos == HPos.CENTER && labelVPos == VPos.CENTER) {
            updateLineGrow(50, 50);
            return;
        }

        int position = divider.isVertical() ?
                switch (labelVPos) {
                    case TOP, BASELINE -> 1;
                    case CENTER -> 2;
                    case BOTTOM -> 3;
                }
                :
                switch (labelHpos) {
                    case LEFT -> 1;
                    case CENTER -> 2;
                    case RIGHT -> 3;
                };


        int percent = getSkinnable().getLinePercent();

        // label is postion { first, last }
        if (percent == 0 || percent >= 100) {
            switch (position) {
                case 1 -> updateLineGrow(null, 100);
                case 2 -> updateLineGrow(50, 50);
                case 3 -> updateLineGrow(100, null);

            }
        }//
        else {
            final int firstP = percent < 0 ? 30 : percent;
            final int lastP = 100 - firstP;

            switch (position) {
                case 1 -> updateLineGrow(firstP, lastP);
                case 2 -> updateLineGrow(50, 50);
                case 3 -> updateLineGrow(lastP, firstP);
            }
        }

        // getSkinnable().requestLayout();
    }


    /**
     * Update flex-grow for line {firstLine,lastLine}
     * <li>if <b>@grow1</b> is null then <b>firstLine</b> has remove</li>
     * <li>if <b>@grow2</b> is null then <b>lastLine</b> has remove</li>
     *
     * @see #changeLinePercent(Divider)
     */
    private void updateLineGrow(Integer grow1, Integer grow2) {
        FlexBox.setFlexGrow(firstLine, grow1);
        FlexBox.setFlexGrow(lastLine, grow2);
        container.addOrRemoveChild(firstLine, grow1 != null);

        boolean isAdd = isVisibleLabel() && grow2 != null;
        container.addOrRemoveChild(lastLine, isAdd );
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
        return snapSizeY(container.prefHeight(width) + top +bottom);
    }
}