package vn.conyeu.javafx.controls.skin;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;
import vn.conyeu.javafx.bindings.BindingBuilder;
import vn.conyeu.javafx.controls.IconWrapper;
import vn.conyeu.javafx.controls.Tag;
import vn.conyeu.javafx.icons.font.FontIcon;
import vn.conyeu.javafx.utils.Colors;
import vn.conyeu.javafx.utils.Converters;
import vn.conyeu.javafx.utils.CssUtils;
import vn.conyeu.javafx.utils.StyleUtils;

import java.util.Optional;

@Slf4j
public class TagSkin<T> extends SimpleSkinBase<Tag<T>> {
    static final PseudoClass PS_HAS_CUSTOM_COLOR = PseudoClass.getPseudoClass("has-custom-color");
    static final PseudoClass PS_HAS_COLOR_NAME = PseudoClass.getPseudoClass("has-color-name");

    private final EventHandler<MouseEvent> onPressedHandler = e -> getSkinnable().fireClickEvent();
    private final EventHandler<MouseEvent> onCloseHandler = e -> {
        if (getSkinnable().isCloseMode()) {
            getSkinnable().fireCloseEvent();
            e.consume();
        }
    };

    private StringBinding labelBinding;
    private BooleanBinding visibleCloseBinding;
    private IconWrapper closeWrapper;
    private Label label;

    //---
    private String curColorNameCls = null;

    public TagSkin(Tag<T> control) {
        super(control);
        initialize(control);
    }

    @Override
    public void initializeUI(Tag<T> c) {
        labelBinding = BindingBuilder.stringBuilder()
                .setMapper(() -> resovleConverter().toString(c.getValue()))
                .addSources(c.valueProperty(), c.converterProperty()).get();

        visibleCloseBinding = BindingBuilder.booleanBuilder()
                .setMapper(() -> c.isVisibleClose() && c.isCloseMode())
                .addSources(c.visibleCloseProperty(), c.modeProperty()).get();

        label = new Label(labelBinding.get());
        label.getStyleClass().add("tag-label");
        label.setMaxWidth(Double.MAX_VALUE);
        label.textFillProperty().bind(c.textFillProperty());
        label.graphicProperty().bind(c.graphicProperty());

        closeWrapper = new IconWrapper()
                .enableRippleGenerator(true)
                .addStyleClasses("close-wrapper")
                .cursor(Cursor.HAND);

        closeWrapper.setPickOnBounds(true);

        getChildren().setAll(label);
        resovleCloseIcon(c);
        updateStatusOrColor(c);

    }


    @Override
    public void initializeListener(Tag<T> c) {
        getSkinnable().addEventHandler(MouseEvent.MOUSE_PRESSED, onPressedHandler);
        closeWrapper.addEventHandler(MouseEvent.MOUSE_PRESSED, onCloseHandler);
        registerChangeListener(labelBinding, e -> label.setText(labelBinding.get()));
        registerChangeListener(c.colorProperty(), e -> updateStatusOrColor(c));
        registerChangeListener(c.statusProperty(), e -> updateStatusOrColor(c));

        registerChangeListener(visibleCloseBinding, e -> resovleCloseIcon(c));
        registerChangeListener(c.closeIconProperty(), e -> resovleCloseIcon(c));
        registerChangeListener(c.closeFactoryProperty(), e -> resovleCloseIcon(c));
        registerChangeListener(c.closeGapProperty(), e -> c.requestLayout());
    }


    private void updateStatusOrColor(Tag<T> c) {

        // if status has set -> only apply status
        if (c.getStatus() != null) clearColor(c);
        else if (c.getColor() == null) clearColor(c);

        // apply tag color
        else {

            if (curColorNameCls != null) {
                c.getStyleClass().remove(curColorNameCls);
            }

            String colorName = Colors.getColorName(c.getColor());

            // :has-color-name
            if (colorName != null) {
                curColorNameCls = "color-" + colorName;
                c.getStyleClass().add(curColorNameCls);
                CssUtils.setPseudo(c, PS_HAS_COLOR_NAME, true);
            }
            // :has-custom-color
            else {
                CssUtils.setPseudo(c, PS_HAS_CUSTOM_COLOR, true);
                updateCustomColor(c.getColor());
            }

        }

    }

    private void updateCustomColor(Paint color) {
        StyleUtils.of(getSkinnable())
                .addTag("-tag-bg-color", color)
                .addTag("-tag-border-color", "transparent")
                .apply();
    }

    private void clearColor(Tag<T> c) {
        updateCustomColor(null);
        c.getStyleClass().remove(curColorNameCls);
        CssUtils.setPseudo(c, PS_HAS_CUSTOM_COLOR, false);
        CssUtils.setPseudo(c, PS_HAS_COLOR_NAME, false);
    }

    protected void layoutChildren(double layoutX, double layoutY, double insideWidth, double insideHeight) {
        final double closeGap = snapSizeX(getSkinnable().getCloseGap());
        final double closeWidth = closeWrapper.prefWidth(insideHeight);
        final double labelWidth = insideWidth - (isVisibleClose() ? closeWidth + closeGap : 0);

        layoutInArea(label, layoutX, layoutY, labelWidth, insideHeight,
                label.getBaselineOffset(), Insets.EMPTY,
                true, false, HPos.LEFT, VPos.CENTER);

        if (isVisibleClose() && getChildren().contains(closeWrapper)) {
            final double closeX = label.getBoundsInParent().getMaxX() + closeGap;
            layoutInArea(closeWrapper, closeX, layoutY, closeWidth, insideHeight,
                    closeWrapper.getBaselineOffset(), Insets.EMPTY,
                    false, false, HPos.RIGHT, VPos.CENTER);
        }
    }

    @Override
    protected double computePrefWidth(double height, double top, double right, double bottom, double left) {
        final double closeWidth = isVisibleClose() ? closeWrapper.prefWidth(height) + getSkinnable().getCloseGap() : 0;
        return snapSizeX(label.prefWidth(height) + closeWidth) + left + right;
    }

    @Override
    protected double computePrefHeight(double width, double top, double right, double bottom, double left) {
        final double labelHeight = label.prefHeight(width);
        final double closeHeight = isVisibleClose() ? closeWrapper.prefHeight(width) : 0;
        return snapSizeY(Math.max(labelHeight, closeHeight)) + top + bottom;
    }

    protected StringConverter<T> resovleConverter() {
        StringConverter<T> converter = getSkinnable().getConverter();
        return converter == null ? Converters.defaultConverter() : converter;
    }

    protected void resovleCloseIcon(Tag<T> c) {
        if (!visibleCloseBinding.get()) {
            getChildren().remove(closeWrapper);
        } else {
            tryAddChildrenNode(closeWrapper);
            Callback<String, Node> factory = c.getCloseFactory();
            if (factory == null) factory = FontIcon::new;

            String closeStr = Optional.ofNullable(c.getCloseIcon()).orElse("far-xmark");
            closeWrapper.setIcon(factory.call(closeStr));
        }
    }

    private boolean isVisibleClose() {
        return visibleCloseBinding.get();
    }

    public void dispose() {
        getSkinnable().removeEventHandler(MouseEvent.MOUSE_PRESSED, onPressedHandler);
        closeWrapper.removeEventHandler(MouseEvent.MOUSE_PRESSED, onCloseHandler);

        visibleCloseBinding = null;
        labelBinding = null;
        super.dispose();
    }

}