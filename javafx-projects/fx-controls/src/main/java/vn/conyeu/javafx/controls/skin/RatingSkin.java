package vn.conyeu.javafx.controls.skin;

import javafx.beans.binding.ObjectBinding;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import vn.conyeu.javafx.bindings.BindingBuilder;
import vn.conyeu.javafx.controls.FlexBox;
import vn.conyeu.javafx.controls.Rating;
import vn.conyeu.javafx.controls.flex.FlexAlignItem;
import vn.conyeu.javafx.controls.flex.FlexDirection;
import vn.conyeu.javafx.icons.font.FontIcon;
import vn.conyeu.javafx.utils.CssUtils;
import vn.conyeu.javafx.utils.FxUtils;


public class RatingSkin extends SimpleSkinBase<Rating> {
    private static final String STRONG_STYLE = "strong";

    private final EventHandler<MouseEvent> mouseMoveHandler = event -> {
        computeRatingFromMouseEvent(event);
        updateForegroundClipRect();
        if (getSkinnable().isUpdateOnHover()) {
            updateRatingForNode();
        }
    };

    private final EventHandler<MouseEvent> mouseClickHandler = event -> {
        final boolean isHoverUpdate = getSkinnable().isUpdateOnHover();
        if (!isHoverUpdate) updateRatingForNode();
    };

    private final EventHandler<MouseEvent> mouseExitedHandler = evt -> {
        handlerMouseExistEvent();
    };


    // the container for the traditional rating control. If updateOnHover and
    // partialClipping are disabled, this will show a combination of strong
    // and non-strong graphics, depending on the current rating value
    private FlexBox bgContainer;

    // the container for the strong graphics which may be partially clipped.
    // Note that this only exists if updateOnHover or partialClipping is enabled.
    private FlexBox strongContainer;

    private final Rectangle foregroundClipRect = new Rectangle();

    private double newRating;

    public RatingSkin(Rating control) {
        super(control);
        initialize(control);
    }

    public void initializeUI(Rating c) {
        ObjectBinding<FlexDirection> directionBinding = BindingBuilder.<FlexDirection>objectBuilder()
                .setMapper(() -> c.isVertical() ? FlexDirection.COLUMN : FlexDirection.ROW)
                .addSources(c.verticalProperty()).get();

        bgContainer = new FlexBox();
        bgContainer.setAlignItem(FlexAlignItem.CENTER);
        bgContainer.hgapProperty().bind(c.spacingProperty());
        bgContainer.vgapProperty().bind(c.spacingProperty());
        bgContainer.directionProperty().bind(directionBinding);
        bgContainer.getStyleClass().setAll("container", "background");

        strongContainer = new FlexBox();
        strongContainer.setMouseTransparent(true);
        strongContainer.getStyleClass().setAll("container", "foreground");
        strongContainer.hgapProperty().bind(c.spacingProperty());
        strongContainer.vgapProperty().bind(c.spacingProperty());
        bgContainer.setAlignItem(FlexAlignItem.CENTER);
        strongContainer.directionProperty().bind(directionBinding);
        strongContainer.setClip(foregroundClipRect);

        getChildren().setAll(bgContainer, strongContainer);

        setupRatingMax(c);
    }

    public void initializeListener(Rating c) {
        registerChangeListener(c.maxProperty(), e -> setupRatingMax(c));
        registerChangeListener(c.boundsInLocalProperty(), e -> updateForegroundClipRect());
        registerChangeListener(c.ratingProperty(), e -> changeRatingValue());
        registerChangeListener(c.readonlyProperty(), e -> changeReadonly());
        registerChangeListener(c.iconSizeProperty(), e -> changeStarSize());
        registerChangeListener(c.strongColorProperty(), e -> changeStrongColor());
        registerChangeListener(c.iconColorProperty(), e -> changeIconColor());
        registerChangeListener(c.verticalRevertProperty(), e -> updateForegroundClipRect());
        listener.registerMapChangeListener(c.tooltipsProperty(), change -> setupTooltip(c));

        c.addEventHandler(MouseEvent.MOUSE_EXITED, mouseExitedHandler);
    }

    private void setupTooltip(Rating control) {
        int pos = 0;
        for (Node node : bgContainer.getChildren()) {
            String tipText = control.getTooltips().get("rating_" + pos++);
            if (tipText == null) Tooltip.uninstall(node, null);
            else {
                Tooltip tooltip = new Tooltip(tipText);
                Tooltip.install(node, tooltip);
            }
        }
    }

    private void setupRatingMax(Rating control) {
        newRating = -1;

        bgContainer.getChildren().clear();
        strongContainer.getChildren().clear();

        for (int index = 0; index < control.getMax(); index++) {
            Node starNode = createStarNode(index, "star");
            Node foregroundNode = createStarNode(index, STRONG_STYLE);
            foregroundNode.getStyleClass().add(STRONG_STYLE);
            foregroundNode.setMouseTransparent(true);

            bgContainer.getChildren().add(starNode);
            strongContainer.getChildren().add(foregroundNode);
        }

        setupTooltip(control);
        changeRatingValue();

    }

    private Node createStarNode(int index, String type) {

        Callback<Rating, Node> callback = getSkinnable().getStarFactory();
        if (callback == null) callback = r -> {
            FontIcon icon = new FontIcon(r.getRatingIcon(), r.getIconSize());
            icon.setIconColor(STRONG_STYLE.equals(type) ? r.getStrongColor() : r.getIconColor());
            return icon;
        };

        Node node = callback.call(getSkinnable());
        CssUtils.setPseudoReadonly(node, getSkinnable().isReadonly());
        node.getStyleClass().addAll("star", "star-" + index);
        node.addEventHandler(MouseEvent.MOUSE_MOVED, mouseMoveHandler);
        node.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickHandler);

        return node;
    }

    private void changeRatingValue() {
        final double rating = getSkinnable().getRating();
        if (newRating != rating) {
            newRating = clampRating(rating);
            updateForegroundClipRect();
        }
    }

    private void changeStarSize() {
        final double size = getSkinnable().getIconSize();
        if (bgContainer != null) for (Node node : bgContainer.getChildren()) {
            if (node instanceof FontIcon icon) icon.setIconSize(size);
        }

        if (strongContainer != null) for (Node node : strongContainer.getChildren()) {
            if (node instanceof FontIcon icon) icon.setIconSize(size);
        }
    }

    private void changeStrongColor() {
        final Color color = getSkinnable().getStrongColor();
        if (strongContainer != null) for (Node node : strongContainer.getChildren()) {
            if (node instanceof FontIcon icon) icon.setIconColor(color);
        }
    }

    private void changeIconColor() {
        final Color color = getSkinnable().getIconColor();
        if (bgContainer != null) for (Node node : bgContainer.getChildren()) {
            if (node instanceof FontIcon icon) icon.setIconColor(color);
        }
    }

    private void changeReadonly() {
        final boolean readonly = getSkinnable().isReadonly();

        for (Node node : bgContainer.getChildren()) {
            CssUtils.setPseudoReadonly(node, readonly);
        }

        for (Node node : strongContainer.getChildren()) {
            CssUtils.setPseudoReadonly(node, readonly);
        }
    }

    private void updateRatingForNode() {
        getSkinnable().setRating(newRating);
    }

    private void handlerMouseExistEvent() {
        final double nodeRate = getSkinnable().getRating();
        if (newRating != nodeRate) {
            changeRatingValue();
        }
    }

    // Calculate the rating based on a mouse position (in Scene coordinates).
    // If we support partial ratings, the value is calculated directly.
    // Otherwise, the ceil of the value is computed.
    private void computeRatingFromMouseEvent(MouseEvent event) {
        if (!getSkinnable().isReadonly()) {
            final Rating control = getSkinnable();
            final double sceneX = event.getSceneX(), sceneY = event.getSceneY();
            final Point2D b = bgContainer.sceneToLocal(new Point2D(sceneX, sceneY));
            final double x = b.getX(), y = b.getY();
            final double width = getContentWidth(), height = getContentHeight();
            final boolean verticalRevert = control.isVerticalRevert();

            final double max = control.getMax();
            final double spacing = control.getSpacing();

            double rating = 0;


            if (isVertical()) {
                final double v = verticalRevert ? y : height - y;
                rating = (v / height) * max;
            } else {
                rating = (x / width) * max;
            }

            if (!control.isAllowHalf()) {
                rating = (int) Math.ceil(rating);
            }

            if (newRating != rating)
                newRating = clampRating(rating);
        }
    }

    private void updateForegroundClipRect() {
        final Rating c = getSkinnable();
        final double cx = snappedLeftInset(), cy = snappedTopInset();
        final double h = getContentHeight(), w = getContentWidth();
        final boolean verticalRevert = c.isVerticalRevert();

        if (isVertical()) {
            final double y = h * newRating / c.getMax();
            foregroundClipRect.relocate(cx, verticalRevert ? cy : h - y);
            foregroundClipRect.setWidth(c.getWidth());
            foregroundClipRect.setHeight(y);
        } //
        else {
            final double x = w * newRating / c.getMax();
            foregroundClipRect.setWidth(x);
            foregroundClipRect.setHeight(c.getHeight());
        }

    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        bgContainer.resizeRelocate(x, y, w, h);
        strongContainer.resizeRelocate(x, y, w, h);
    }

    @Override
    protected double computePrefWidth(double height, double top, double right, double bottom, double left) {
        return snapSpaceX(bgContainer.prefWidth(height)) + left + right;
    }

    @Override
    protected double computePrefHeight(double width, double top, double right, double bottom, double left) {
        return snapSpaceY(bgContainer.prefHeight(width)) + top + bottom;
    }

    public void dispose() {
        getSkinnable().removeEventHandler(MouseEvent.MOUSE_EXITED, mouseExitedHandler);

        if (bgContainer != null) {
            for (Node node : bgContainer.getChildren()) {
                node.removeEventHandler(MouseEvent.MOUSE_MOVED, mouseMoveHandler);
                node.removeEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickHandler);
            }

            bgContainer.getChildren().clear();
            bgContainer = null;
        }

        if (strongContainer != null) {
            for (Node node : strongContainer.getChildren()) {
                node.removeEventHandler(MouseEvent.MOUSE_MOVED, mouseMoveHandler);
                node.removeEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickHandler);
            }

            strongContainer.getChildren().clear();
            strongContainer = null;
        }

        super.dispose();
    }

    double clampRating(double value) {
        return FxUtils.clamp(0, value, getSkinnable().getMax());
    }

    boolean isVertical() {
        return getSkinnable().isVertical();
    }

    double getContentWidth() {
        return getSkinnable().getWidth() - (snappedLeftInset() + snappedRightInset());
    }

    double getContentHeight() {
        return getSkinnable().getHeight() - (snappedTopInset() + snappedBottomInset());
    }
}