package vn.conyeu.javafx.controls.skin;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.css.PseudoClass;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import lombok.extern.slf4j.Slf4j;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.javafx.bindings.BindingBuilder;
import vn.conyeu.javafx.controls.Badge;
import vn.conyeu.javafx.controls.Badge.BadgeType;
import vn.conyeu.javafx.controls.enums.Status;
import vn.conyeu.javafx.utils.Colors;
import vn.conyeu.javafx.utils.CssUtils;
import vn.conyeu.javafx.utils.NodeUtils;
import vn.conyeu.javafx.utils.StyleUtils;

@Slf4j
public class BadgeSkin extends SimpleSkinBase<Badge> {
    static final PseudoClass OVERFLOW_PS = PseudoClass.getPseudoClass("overflow-count");

    private Label label, dotText;
    private StackPane contentPane;


    private StringBinding labelBinding;
    private BooleanBinding overflowBinding;
    private BooleanBinding dotTextBinding;

    public BadgeSkin(Badge control) {
        super(control);
        initialize(control);
    }

    @Override
    public void initializeUI(Badge control) {
        labelBinding = BindingBuilder.stringBuilder()
                .addSources(control.typeProperty(), control.countProperty())
                .addSources(control.overflowCountProperty(), control.textProperty())
                .setMapper(() -> calcCountText(control)).get();

        overflowBinding = BindingBuilder.booleanBuilder()
                .setMapper(() -> control.getType() == BadgeType.COUNT && control.getOverflowCount() > 0 && control.getCount() > control.getOverflowCount() )
                .addSources(control.typeProperty(), control.countProperty(), control.overflowCountProperty()).get();

        dotTextBinding = BindingBuilder.booleanBuilder()
                .setMapper(() -> control.getType() == BadgeType.DOT && Objects.notBlank(control.getText()) && control.getContent() == null)
                .addSources(control.typeProperty(), control.textProperty(), control.contentProperty()).get();

        label = new Label();
        label.getStyleClass().setAll("badge-label");
        label.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        label.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        label.textProperty().bind(labelBinding);

        getChildren().setAll(label);

        setupContentPane(control);

    }

    @Override
    public void initializeDataUI(Badge control) {
        applyColor(null, control.getColor());
        applyBadgeType(null, control.getType());
        applyBadgeStatus(null, control.getStatus());
        applyDotText(control);
        applyOverflowPs(control);
        applyDotSize(control);
    }

    @Override
    public void initializeListener(Badge control) {
        changeListener(control.colorProperty(), (obs, ov, nv)-> applyColor(ov, nv));
        changeListener(control.typeProperty(), (obs, ov, nv) -> applyBadgeType(ov, nv));
        changeListener(control.statusProperty(), (obs, ov, nv) -> applyBadgeStatus(ov, nv));
        changeListener(dotTextBinding, (obs, ov, nv) -> applyDotText(control));
        changeListener(overflowBinding, (obs, ov, nv) -> applyOverflowPs(control));

        registerChangeListener(control.dotSizeProperty(), e -> applyDotSize(control));
        registerChangeListener(control.contentProperty(), e -> setupContentPane(control));
        registerChangeListener(control.positionProperty(), e -> control.requestLayout());
    }

    private void setupContentPane(Badge control) {
        Node contentNode = control.getContent();
        if(contentNode == null) {
            if(contentPane != null) {
                getChildren().remove(contentPane);
                contentPane.getChildren().clear();
                contentPane = null;
            }
        }
        else {
            if(contentPane == null) {
                contentPane = new StackPane();
                contentPane.getStyleClass().add("content");
                contentPane.setAlignment(Pos.CENTER);
                contentPane.getChildren().add(control.getContent());
                getChildren().add(contentPane);
                label.toFront();
            }

            contentPane.getChildren().setAll(contentNode);
        }
    }

    private void applyBadgeStatus(Status old, Status status) {
        CssUtils.setPseudo(getSkinnable(), "badge-status-", old, status);
    }

    private void applyDotSize(Badge control) {
        StyleUtils.of(control).addTag("-badge-dot-size", control.getDotSize()).apply();
    }

    private void applyDotText(Badge control) {
        if(!dotTextBinding.get()) {
            if(dotText != null) {
                getChildren().remove(dotText);
                dotText.textProperty().unbind();
                dotText = null;
            }
        }
        else {
            if(dotText  == null) {
                dotText = new Label();
                dotText.getStyleClass().setAll("dot-text");
                dotText.textProperty().bind(control.textProperty());
                getChildren().add(dotText);
            }
        }

    }

    protected void applyOverflowPs(Badge control) {
        CssUtils.setPseudo(getSkinnable(), OVERFLOW_PS, overflowBinding.get());
    }

    protected void applyColor(Paint old, Paint color) {
        StyleUtils.of(getSkinnable()).addTag("-badge-color", color).apply();
        NodeUtils.removeStyles(getSkinnable(), Colors.getColorName(old));
        NodeUtils.addStyleClass(getSkinnable(), Colors.getColorName(color));
    }

    protected void applyBadgeType(BadgeType old, BadgeType type) {
        CssUtils.setPseudo(label, "", old, type);
    }

    private String calcCountText(Badge control) {
        return switch (control.getType()) {
            case DOT -> "";
            case TEXT -> control.getText();
            case COUNT -> {
                int count = Math.max(0, control.getCount());
                int overlay = Math.max(0, control.getOverflowCount());
                boolean useCount = overlay <=0 || count <= overlay;
                yield String.valueOf(useCount ? count : overlay + "+");
            }

        };
    }

    @Override
    protected void layoutChildren(double layoutX, double layoutY, double insideWidth, double insideHeight) {
        super.layoutChildren(layoutX, layoutY, insideWidth, insideHeight);



        if(dotText != null && getChildren().contains(dotText)) {
            final double x = layoutX + label.getBoundsInParent().getMaxX() + 5;
            layoutInArea(dotText, x, layoutY, dotText.prefWidth(-1), insideHeight, -1, HPos.LEFT, VPos.CENTER);
        }

        if(contentPane != null && getChildren().contains(contentPane)) {
            final double nodeWidth = snapSizeX(contentPane.prefWidth(-1));
            final double nodeHeight = snapSizeY(contentPane.prefHeight(-1));
            final double countWidth = snapSizeX(label.prefWidth(-1));
            final double countHeight = snapSizeY(label.prefHeight(-1));

            final HPos hPos = getSkinnable().getPosition().getHpos();
            final VPos vPos = getSkinnable().getPosition().getVpos();

            final double nodeX = (HPos.LEFT == hPos ? countWidth / 2 : 0) + layoutX;

            final double nodeY = layoutY + switch (vPos) {
                case CENTER, BASELINE -> 0;
                case TOP -> countHeight / 2;
                case BOTTOM -> 0;//nodeHeight - countHeight / 2;
            };

            contentPane.resizeRelocate(nodeX, nodeY, nodeWidth, nodeHeight);

            final double offsetX = getSkinnable().getOffsetX(), offsetY = getSkinnable().getOffsetY();
            final double countX = offsetX >= 0 ? offsetX : computeLayoutYForCount(vPos, nodeHeight, countHeight) + layoutY;
            final  double countY = offsetY >= 0 ? offsetY : computeLayoutXForCount(hPos, nodeWidth, countWidth) + layoutX;

            log.warn("{}::{}", countX, countY);
            label.resizeRelocate(countX, countY, countWidth, countHeight);
        }
    }


//    @Override
//    protected double computePrefWidth(double h, double t, double r, double b, double l) {
//        final double countWidth = isVisibleCount() ? countStack.prefWidth(-1) : 0;
//        if (!getSkinnable().isStandalone()) {
//            if (visibleStatusLabelBinding.get()) {
//                final double labelStatus = statusLabel.prefWidth(-1);
//                return labelStatus + countWidth + l + r;
//            } //
//            else if (contentStack != null) {
//                final double nodeWidth = contentStack.prefWidth(-1);
//                final boolean isPosCenter = getSkinnable().getPosition().getHpos() == HPos.CENTER;
//                return (isPosCenter ? 0 : countWidth / 2) + nodeWidth + l + r;
//            }
//        }
//        return countWidth + l + r;
//    }


        @Override
    protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return computePrefWidth(height, topInset, rightInset, bottomInset, leftInset);
    }

    @Override
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    private double computeLayoutXForCount(HPos hpos, double nodeWidth, double countWidth) {
        return switch (hpos) {
            case LEFT -> 0;
            case CENTER -> (nodeWidth - countWidth) / 2;
            case RIGHT -> nodeWidth - countWidth / 2;
        };
    }

    private double computeLayoutYForCount(VPos vPos, double nodeHeight, double countHeight) {
        return switch (vPos) {
            case TOP -> 0;
            case CENTER, BASELINE -> (nodeHeight - countHeight) / 2;
            case BOTTOM -> nodeHeight - countHeight / 2;
        };
    }

    @Override
    public void dispose() {
        label.textProperty().unbind();
        labelBinding = null;
        super.dispose();
    }
}