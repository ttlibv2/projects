package vn.conyeu.javafx.utils;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Screen;
import javafx.stage.Window;
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.commons.utils.Objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public final class NodeUtils {

    public static double computeXOffset(double width, double contentWidth, HPos hpos) {
        return switch (hpos) {
            case LEFT -> 0;
            case CENTER -> (width - contentWidth) / 2;
            case RIGHT -> width - contentWidth;
        };
    }

    public static double computeYOffset(double height, double contentHeight, VPos vpos) {
        return switch (vpos) {
            case BASELINE, TOP -> 0;
            case CENTER -> (height - contentHeight) / 2;
            case BOTTOM -> height - contentHeight;
        };
    }


    public static void removeStyleClass(Node node, String... styleClass) {
        node.getStyleClass().removeAll(styleClass);
    }

    public static void removeStyleClass(Node node, List<String> styleClass) {
        node.getStyleClass().removeAll(styleClass);
    }

    public static void addStyleClass(Node node, String styleClass) {
        if (node != null && Objects.notBlank(styleClass)) {
            if (!node.getStyleClass().contains(styleClass)) {
                node.getStyleClass().add(styleClass);
            }
        }
    }

    public static void addStyleClass(Node node, String... styleClass) {
        for (String style : styleClass) addStyleClass(node, style);
    }

    public static void updateStyleClass(Node node, String styleClass, boolean isAddTo) {
        if (isAddTo) addStyleClass(node, styleClass);
        else node.getStyleClass().remove(styleClass);
    }

    public static void updateStyleClass(Node node, String prefix, Enum oldEnum, Enum newEnum) {
        List<String> styleClasses = node.getStyleClass();
        if (oldEnum != null) styleClasses.remove(prefix + enumToStyleClass(oldEnum));
        if (newEnum != null) addStyleClass(node, prefix + enumToStyleClass(newEnum));
    }

    private static String enumToStyleClass(Enum enumVal) {
        Asserts.notNull(enumVal, "@Enum");
        return enumVal.name().replaceAll("_", "-").toLowerCase();
    }

    /**
     * @param child the child node of an anchor pane
     * @param value the offset from the right of the anchor pane
     */
    public static void setAnchorLeftRight(Node child, double value) {
        AnchorPane.setLeftAnchor(child, value);
        AnchorPane.setRightAnchor(child, value);
    }

    /**
     * @param child the child node of an anchor pane
     * @param value the offset from the right of the anchor pane
     */
    public static void setAnchorTopBottom(Node child, double value) {
        AnchorPane.setTopAnchor(child, value);
        AnchorPane.setBottomAnchor(child, value);
    }

    /**
     * @param child the child node of an anchor pane
     * @param insets the offset from the right of the anchor pane
     */
    public static void setAnchor(Node child, Insets insets) {
        AnchorPane.setLeftAnchor(child, insets.getLeft());
        AnchorPane.setRightAnchor(child, insets.getRight());
        AnchorPane.setTopAnchor(child, insets.getTop());
        AnchorPane.setBottomAnchor(child, insets.getBottom());
    }

    /**
     * @param child the child node of an anchor pane
     * @param value the offset from the right of the anchor pane
     */
    public static void setAnchor(Node child, double value) {
        AnchorPane.setLeftAnchor(child, value);
        AnchorPane.setRightAnchor(child, value);
        AnchorPane.setTopAnchor(child, value);
        AnchorPane.setBottomAnchor(child, value);
    }

    public static Background createBg(Paint color) {
        return color == null ? Background.EMPTY
                :new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY));
    }

    /**
     * Update center node for {@link BorderPane}.
     * If {@code node#children} is empty -> remove
     */
    public static void setCenterBorderPane(BorderPane container, Pane node) {
        if (node.getChildren().isEmpty()) container.setCenter(null);
        else if (!node.getChildren().contains(node)) container.setCenter(node);
    }

    public static <E, C> void listChangeListener(ListChangeListener.Change change, ObservableList<C> list, Callback<E, C> childCallback) {
        while (change.next()) {

            if (change.wasRemoved()) {
                List<E> items = change.getRemoved();
                for(E item:items) {
                    C child = childCallback.call(item);
                    if(child != null) list.remove(child);
                }
            }

            if (change.wasAdded()) {
                List<E> items = change.getAddedSubList();
                for(E item:items) {
                    C child = childCallback.call(item);
                    if(child != null) list.add(child);
                }
            }
        }
    }

    public static void changeListForHBox(HBox hbox, ListChangeListener.Change<? extends Node> change) {
        Callback<Node, Node> callback = node -> node;
        listChangeListener(change, hbox.getChildren(), callback);
    }



    public static HBox createHBox(Pos alignment, boolean bindVisible, String... styleClass) {
        HBox hbox = new HBox();
        hbox.setAlignment(alignment);

        if (styleClass != null && styleClass.length > 0) {
            hbox.getStyleClass().setAll(styleClass);
        }

        if (bindVisible) {
            bindVisible(hbox, Bindings.isNotEmpty(hbox.getChildren()));
        }

        return hbox;
    }

    /**
     * @deprecated use {@link #createHBox(Pos, boolean, String...)}
     */
    public static HBox createHBox(String styleClass, Pos alignment, boolean bindVisible) {
        return createHBox(alignment, bindVisible, styleClass);
    }

    public static StackPane createStackPane(Pos alignment, boolean bindVisible, String... styleClass) {
        StackPane hbox = new StackPane();
        hbox.setAlignment(alignment);

        if (styleClass != null && styleClass.length > 0) {
            hbox.getStyleClass().setAll(styleClass);
        }

        if (bindVisible) {
            bindVisible(hbox, Bindings.isNotEmpty(hbox.getChildren()));
        }

        return hbox;
    }

    /**
     * @deprecated use {@link #createStackPane(Pos, boolean, String...)}
     */
    public static StackPane createStackPane(String styleClass, Pos alignment, boolean bindVisible) {
        return createStackPane(alignment, bindVisible, styleClass);
    }

    private static void bindVisible(Node node, BooleanBinding bindVisible) {
        node.visibleProperty().bind(bindVisible);
        node.managedProperty().bind(node.visibleProperty());
    }

    public static void pseudoClass(Node node, PseudoClass psClass, Pane child) {
        node.pseudoClassStateChanged(psClass, child.getChildren().isEmpty());
    }

    private static String convertCssObjectToString(Object object) {
        if (object == null) return null;
        else if (object instanceof String str) return str;
        else if (object.getClass().isEnum()) return ((Enum) object).name();
        else if (object.getClass().isPrimitive()) return object.toString();
        else if (object instanceof Number n) return n.toString();
        else if (object instanceof Paint p) return Colors.toCss(p);
        else throw new UnsupportedOperationException(object.getClass().getName());
    }

    public static void removeStyles(Node node, String... styles) {
        Map<String, Object> nodeStyles = new HashMap<>();
        parseStyle(nodeStyles, node.getStyle());
        for (String style : styles) nodeStyles.remove(style);
    }

    public static void updateStyle(Node node, Object... styles) {
        updateStyle(node, List.of(styles));
    }

    public static void updateStyle(Node node, List<Object> styles) {
        if (styles.size() % 2 != 0) throw Objects.newIllegal("@styles invalid format value");
        Map<String, String> lines = new HashMap<>();
        for (int i = 0; i < styles.size(); i += 2) {
            String fxName = styles.get(i).toString().trim();
            String fxVal = convertCssObjectToString(styles.get(i + 1));
            lines.put(fxName, fxVal);
        }
        updateStyle(node, lines);
    }

    public static void updateStyle(Node node, Map<String, String> style) {
        Asserts.notNull(node, "@Node must be not null");
        if (Objects.notEmpty(style)) {
            Map<String, Object> lines = new HashMap<>();
            if (Objects.notBlank(node.getStyle())) {
                String nodeStyle = node.getStyle();
                parseStyle(lines, nodeStyle);
            }

            // override style node
            for (Map.Entry<String, String> entry : style.entrySet()) {
                if (Objects.isBlank(entry.getValue())) lines.remove(entry.getKey());
                else lines.put(entry.getKey(), entry.getValue());
            }

            node.setStyle(lines.keySet()
                    .stream().map(s -> s + ":" + lines.get(s))
                    .collect(Collectors.joining(";")));

            if (log.isDebugEnabled()) {
                log.debug("update style for node -> {}", node.getStyle());
            }
        }
    }

    public static void parseStyle(Map<String, Object> cache, String style) {
        if (Objects.notBlank(style)) {
            String[] parts = style.split(";");
            for (String part : parts) {
                String[] list = part.split(":");
                if (list.length == 2) {
                    cache.put(list[0].trim(), list[1].trim());
                }
            }
        }
    }

    /**
     * Changes the background color of a {@code Region} to the desired one.
     *
     * @param nodeToUpdate The region to change the background color to
     * @param fill         The desired color
     */
    public static void updateBackground(Region nodeToUpdate, Paint fill) {
        updateBackground(nodeToUpdate, nodeToUpdate.getBackground(), fill);
    }

    public static void updateBackground(Region nodeToUpdate, Background newBackground) {
        updateBackground(nodeToUpdate, newBackground, Color.BLACK);
    }

    public static void updateBackground(Region nodeToUpdate, Background newBackground, Paint fill) {
        if (newBackground != null && !newBackground.getFills().isEmpty()) {
            final BackgroundFill[] fills = new BackgroundFill[newBackground.getFills().size()];
            for (int i = 0; i < newBackground.getFills().size(); i++) {
                BackgroundFill bf = newBackground.getFills().get(i);
                fills[i] = new BackgroundFill(fill, bf.getRadii(), bf.getInsets());
            }
            nodeToUpdate.setBackground(new Background(fills));
        }
    }

    /**
     * Changes the background color of a {@code Region} to the desired one and lets specify the background insets.
     *
     * @param region           The region to change the background color to
     * @param fill             The desired color
     * @param backgroundInsets The background insets to use
     */
    public static void updateBackground(Region region, Paint fill, Insets backgroundInsets) {
        final Background background = region.getBackground();
        if (background == null || background.getFills().isEmpty()) {
            return;
        }

        final List<BackgroundFill> fills = new ArrayList<>();
        for (BackgroundFill bf : background.getFills()) {
            fills.add(new BackgroundFill(fill, bf.getRadii(), backgroundInsets));
        }

        region.setBackground(new Background(fills.toArray(BackgroundFill[]::new)));
    }

    public static void updateBackground(Region region, Paint fill, CornerRadii cornerRadii, Insets backgroundInsets) {
        final Background background = region.getBackground();
        if (background == null || background.getFills().isEmpty()) {
            return;
        }

        final List<BackgroundFill> fills = new ArrayList<>();
        for (int index = 0; index < background.getFills().size(); index++) {
            fills.add(new BackgroundFill(fill, cornerRadii, backgroundInsets));
        }

        region.setBackground(new Background(fills.toArray(BackgroundFill[]::new)));
    }

    /**
     * Sets the background of the given region to the given color.
     */
    public static void setBackground(Region region, Paint fill) {
        setBackground(region, fill, CornerRadii.EMPTY, Insets.EMPTY);
    }

    /**
     * Sets the background of the given region to the given color, with the given radius.
     */
    public static void setBackground(Region region, Paint fill, CornerRadii radius) {
        setBackground(region, fill, radius, Insets.EMPTY);
    }

    /**
     * Sets the background of the given region to the given color, with the given radius and insets.
     */
    public static void setBackground(Region region, Paint fill, CornerRadii radius, Insets insets) {
        region.setBackground(new Background(new BackgroundFill(fill, radius, insets)));
    }

    /**
     * Tries to parse tje given Region's corner radius.
     * <p>
     * To be more precise it tries to parse both the background and the
     * border radius. The background radius is prioritized over the border one
     * but in case the background is null or empty then the border one is used.
     * <p>
     * In case of both null or empty returns {@link  CornerRadii#EMPTY}.
     */
    public static CornerRadii parseCornerRadius(Region region) {
        CornerRadii backRadius = CornerRadii.EMPTY;
        CornerRadii bordRadius = CornerRadii.EMPTY;

        Background background = region.getBackground();
        if (background != null && !background.isEmpty()) {
            backRadius = background.getFills().get(0).getRadii();
        }

        Border border = region.getBorder();
        if (border != null && !border.isEmpty()) {
            bordRadius = border.getStrokes().get(0).getRadii();
        }

        return !backRadius.equals(CornerRadii.EMPTY) ? backRadius : bordRadius;
    }

    /**
     * Convenience method for adding the desired value to the region's prefHeight
     */
    public static void addPrefHeight(Region region, double value) {
        double prefH = region.getPrefHeight();
        region.setPrefHeight(prefH + value);
    }

    /**
     * Convenience method for programmatically fire a dummy MOUSE_PRESSED event on the desired node.
     */
    public static void fireDummyEvent(Node node) {
        Event.fireEvent(node, new MouseEvent(MouseEvent.MOUSE_PRESSED,
                0, 0, 0, 0, MouseButton.PRIMARY, 1,
                false, false, false, false, true, false, false, false, false, false, null));
    }

    /**
     * Checks if the given alignment is set to RIGHT(any).
     */
    public static boolean isRightAlignment(Pos alignment) {
        return alignment == Pos.BASELINE_RIGHT || alignment == Pos.BOTTOM_RIGHT ||
                alignment == Pos.CENTER_RIGHT || alignment == Pos.TOP_RIGHT;
    }

    /**
     * Recursively gets all nodes that are descendants of the given root.
     */
    public static ArrayList<Node> getAllNodes(Parent root) {
        ArrayList<Node> nodes = new ArrayList<>();
        addAllDescendents(root, nodes);
        return nodes;
    }

    private static void addAllDescendents(Parent parent, ArrayList<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            if (node instanceof Parent p)
                addAllDescendents(p, nodes);
        }
    }

    /**
     * Convenience method to execute a given action after that the given control
     * has been laid out and its skin is not null anymore.
     * <p></p>
     * If the skin is not null when called, the action is executed immediately.
     * <p>
     * The listener is added only if the skin is null or the addListenerIfNotNull parameter is true.
     *
     * @param control              the control to check for skin initialization
     * @param action               the action to perform when the skin is not null
     * @param addListenerIfNotNull to specify if the listener should be added anyway even if the scene is not null
     * @param isOneShot            to specify if the listener added to the skin property
     *                             should be removed after it is not null anymore
     */
    public static void waitForSkin(Control control, Runnable action, boolean addListenerIfNotNull, boolean isOneShot) {
        if (control.getSkin() != null) {
            action.run();
        }

        if (control.getSkin() == null || addListenerIfNotNull) {
            control.skinProperty().addListener(new ChangeListener<>() {
                @Override
                public void changed(ObservableValue<? extends Skin<?>> observable, Skin<?> oldValue, Skin<?> newValue) {
                    if (newValue != null) {
                        action.run();
                        if (isOneShot) {
                            control.skinProperty().removeListener(this);
                        }
                    }
                }
            });
        }
    }

    /**
     * Convenience method to execute a given action after that the given node
     * has been laid out and its scene is not null anymore.
     * <p></p>
     * If the scene is not null when called, the action is executed immediately.
     * <p>
     * The listener is added only if the scene is null or the addListenerIfNotNull parameter is true.
     *
     * @param node                 the node to check for scene initialization
     * @param action               the action to perform when the scene is not null
     * @param addListenerIfNotNull to specify if the listener should be added anyway even if the scene is not null
     * @param isOneShot            to specify if the listener added to the scene property
     *                             should be removed after it is not null anymore
     */
    public static void waitForScene(Node node, Runnable action, boolean addListenerIfNotNull, boolean isOneShot) {
        if (node.getScene() != null) {
            action.run();
        }

        if (node.getScene() == null || addListenerIfNotNull) {
            node.sceneProperty().addListener(new ChangeListener<>() {
                @Override
                public void changed(ObservableValue<? extends Scene> observable, Scene oldValue, Scene newValue) {
                    if (newValue != null) {
                        action.run();
                        if (isOneShot) node.sceneProperty().removeListener(this);
                    }
                }
            });
        }
    }

    /**
     * Checks if the given {@link PseudoClass} is currently active on the given {@link Control}.
     */
    public static boolean isPseudoClassActive(Control control, PseudoClass pseudoClass) {
        return control.getPseudoClassStates().contains(pseudoClass);
    }

    /**
     * Attempts to get the {@link Screen} instance on which
     * the given {@link Node} is shown.
     * If the screen is not found for any reason, returns null.
     */
    public static Screen getScreenFor(Node node) {
        Bounds nodeBounds = node.localToScreen(node.getLayoutBounds());
        Rectangle2D boundsToRect = new Rectangle2D(nodeBounds.getMinX(), nodeBounds.getMinY(), nodeBounds.getWidth(), nodeBounds.getHeight());
        return Screen.getScreens().stream()
                .filter(screen -> screen.getBounds().contains(boundsToRect))
                .findFirst()
                .orElse(null);
    }

    public static void addToChildren(SkinBase skinBase, int index, Node node) {
        if (!skinBase.getChildren().contains(node)) {
            skinBase.getChildren().add(node);
        }
    }

    public static void updateChildren(String position, BorderPane container, Pane pane) {
        Node nodeSet = pane.getChildren().isEmpty() ? null : pane;
        switch (position) {
            case "left":
                container.setLeft(nodeSet);
            case "right":
                container.setRight(nodeSet);
            case "center":
                container.setCenter(nodeSet);
            case "top":
                container.setTop(nodeSet);
            case "bottom":
                container.setBottom(nodeSet);
        }
    }

    public static void removeStyleClassStartsWith(Node node, String styleClass) {
        List<String> styleList = node.getStyleClass();
        styleList.removeIf(styleCls -> styleCls.startsWith(styleClass));
    }

    public static void setConstraint(Node node, Object key, Object value) {
        Map<Object, Object> props = node.getProperties();

        if (value == null) props.remove(key);
        else props.put(key, value);

        if (node.getParent() != null) {
            node.getParent().requestLayout();
        }
    }

    public static <T> T getConstraint(Node node, Object key) {
        return getConstraint(node, key, null);
    }

    public static <T> T getConstraint(Node node, Object key, T defaultValue){
        T object = node.hasProperties() ? (T) node.getProperties().get(key) : null;
        return object == null ? defaultValue : object;
    }

    public static boolean equalsValueConstraint(Node node, String constraintKey, Object other) {
        return Objects.equals(getConstraint(node, constraintKey), other);
    }

    public static double computeChildAreaWidth(Region parent, Node child, Insets margin) {
        return computeChildAreaWidth(parent, child, -1, margin, -1, false);
    }

    public static double computeChildAreaWidth(Region parent, Node child, double baselineComplement, Insets margin, double height, boolean fillHeight) {
        final boolean snap = parent.isSnapToPixel();
        double left = margin != null ? snapSpaceX(parent, margin.getLeft(), snap) : 0;
        double right = margin != null ? snapSpaceX(parent, margin.getRight(), snap) : 0;

        double alt = -1;
        if (height != -1 && child.isResizable() && child.getContentBias() == Orientation.VERTICAL) { // width depends on height
            double top = margin != null ? snapSpaceY(parent, margin.getTop(), snap) : 0;
            double bottom = margin != null ? snapSpaceY(parent, margin.getBottom(), snap) : 0;
            double bo = child.getBaselineOffset();
            final double contentHeight = bo == BASELINE_OFFSET_SAME_AS_HEIGHT && baselineComplement != -1 ?
                    height - top - bottom - baselineComplement :
                    height - top - bottom;

            if (fillHeight) {
                alt = parent.snapSizeY(boundedSize(
                        child.minHeight(-1), contentHeight,
                        child.maxHeight(-1)));
            } else {
                alt = parent.snapSizeY(boundedSize(
                        child.minHeight(-1),
                        child.prefHeight(-1),
                        Math.min(child.maxHeight(-1), contentHeight)));
            }
        }

        double width = boundedSize(child.minWidth(alt), child.prefWidth(alt), child.maxWidth(alt));
        return left + parent.snapSizeX(width) + right;
    }

    public static double computeChildPrefAreaWidth(Region parent, Node child, Insets margin) {
        return computeChildPrefAreaWidth(parent, child, -1, margin, -1, false);
    }

    public static double computeChildPrefAreaWidth(Region parent, Node child,
                                                   double baselineComplement, Insets margin, double height, boolean fillHeight) {
        final boolean snap = parent.isSnapToPixel();
        double left = margin != null ? snapSpaceX(parent, margin.getLeft(), snap) : 0;
        double right = margin != null ? snapSpaceX(parent, margin.getRight(), snap) : 0;
        double alt = -1;
        if (height != -1 && child.isResizable() && child.getContentBias() == Orientation.VERTICAL) { // width depends on height
            double top = margin != null ? snapSpaceY(parent, margin.getTop(), snap) : 0;
            double bottom = margin != null ? snapSpaceY(parent, margin.getBottom(), snap) : 0;
            double bo = child.getBaselineOffset();
            final double contentHeight = bo == BASELINE_OFFSET_SAME_AS_HEIGHT && baselineComplement != -1 ?
                    height - top - bottom - baselineComplement :
                    height - top - bottom;
            if (fillHeight) {
                alt = parent.snapSizeY(boundedSize(
                        child.minHeight(-1), contentHeight,
                        child.maxHeight(-1)));
            } else {
                alt = parent.snapSizeY(boundedSize(
                        child.minHeight(-1),
                        child.prefHeight(-1),
                        Math.min(child.maxHeight(-1), contentHeight)));
            }
        }
        return left + parent.snapSizeX(boundedSize(child.minWidth(alt), child.prefWidth(alt), child.maxWidth(alt))) + right;
    }


    public static double computeChildPrefAreaHeight(Region parent, Node child, Insets margin) {
        return computeChildPrefAreaHeight(parent, child, -1, margin, -1);
    }

    public static double computeChildPrefAreaHeight(Region parent, Node child, double prefBaselineComplement, Insets margin, double width) {
        final boolean snap = parent.isSnapToPixel();
        double top = margin != null ? snapSpaceY(parent, margin.getTop(), snap) : 0;
        double bottom = margin != null ? snapSpaceY(parent, margin.getBottom(), snap) : 0;

        double alt = -1;
        if (child.isResizable() && child.getContentBias() == Orientation.HORIZONTAL) { // height depends on width
            double left = margin != null ? snapSpaceX(parent, margin.getLeft(), snap) : 0;
            double right = margin != null ? snapSpaceX(parent, margin.getRight(), snap) : 0;
            alt = parent.snapSizeX(boundedSize(
                    child.minWidth(-1), width != -1 ? width - left - right
                            : child.prefWidth(-1), child.maxWidth(-1)));
        }

        if (prefBaselineComplement != -1) {
            double baseline = child.getBaselineOffset();
            if (child.isResizable() && baseline == BASELINE_OFFSET_SAME_AS_HEIGHT) {
                // When baseline is same as height, the preferred height of the node will be above the baseline, so we need to add
                // the preferred complement to it
                return top + parent.snapSizeY(boundedSize(child.minHeight(alt), child.prefHeight(alt), child.maxHeight(alt))) + bottom
                        + prefBaselineComplement;
            } else {
                // For all other Nodes, it's just their baseline and the complement.
                // Note that the complement already contain the Node's preferred (or fixed) height
                return top + baseline + prefBaselineComplement + bottom;
            }
        } else {
            return top + parent.snapSizeY(boundedSize(child.minHeight(alt), child.prefHeight(alt), child.maxHeight(alt))) + bottom;
        }
    }

    public static double computeMaxPrefAreaHeight(Region parent, List<Node> children, Callback<Node, Insets> margins, VPos valignment) {
        return getMaxAreaHeight(parent, children, margins, null, valignment, false);
    }

    public static double computeMaxPrefAreaHeight(Region parent, List<Node> children, Callback<Node, Insets> margins, double width, VPos valignment) {
        return getMaxAreaHeight(parent, children, margins, new double[]{width}, valignment, false);
    }

    public static double computeMaxPrefAreaHeight(Region parent, List<Node> children, Callback<Node, Insets> childMargins, double childWidths[], VPos valignment) {
        return getMaxAreaHeight(parent, children, childMargins, childWidths, valignment, false);
    }

    public static double computeMaxMinAreaHeight(Region parent, List<Node> children, Callback<Node, Insets> margins, VPos valignment) {
        return getMaxAreaHeight(parent, children, margins, null, valignment, true);
    }


    /* utility method for computing the max of children's min or pref heights, taking into account baseline alignment */
    private static double getMaxAreaHeight(Region parent, List<Node> children, Callback<Node, Insets> childMargins, double childWidths[], VPos valignment, boolean minimum) {
        final double singleChildWidth = childWidths == null ? -1 : childWidths.length == 1 ? childWidths[0] : Double.NaN;
        if (valignment == VPos.BASELINE) {
            double maxAbove = 0;
            double maxBelow = 0;
            for (int i = 0, maxPos = children.size(); i < maxPos; i++) {
                final Node child = children.get(i);
                final double childWidth = Double.isNaN(singleChildWidth) ? childWidths[i] : singleChildWidth;
                Insets margin = childMargins.call(child);
                final double top = margin != null ? parent.snapSpaceY(margin.getTop()) : 0;
                final double bottom = margin != null ? parent.snapSpaceY(margin.getBottom()) : 0;
                final double baseline = child.getBaselineOffset();

                final double childHeight = minimum ?
                        parent.snapSizeY(child.minHeight(childWidth)) : parent.snapSizeY(child.prefHeight(childWidth));
                if (baseline == BASELINE_OFFSET_SAME_AS_HEIGHT) {
                    maxAbove = Math.max(maxAbove, childHeight + top);
                } else {
                    maxAbove = Math.max(maxAbove, baseline + top);
                    maxBelow = Math.max(maxBelow,
                            parent.snapSpaceY(minimum ? parent.snapSizeY(child.minHeight(childWidth)) :
                                    parent.snapSizeY(child.prefHeight(childWidth))) -
                                    baseline + bottom);
                }
            }
            return maxAbove + maxBelow; //remind(aim): ceil this value?
        } //
        else {
            double max = 0;
            for (int i = 0, maxPos = children.size(); i < maxPos; i++) {
                final Node child = children.get(i);
                Insets margin = childMargins.call(child);
                final double childWidth = Double.isNaN(singleChildWidth) ? childWidths[i] : singleChildWidth;
                max = Math.max(max, minimum ?
                        computeChildMinAreaHeight(parent, child, -1, margin, childWidth) :
                        computeChildPrefAreaHeight(parent, child, -1, margin, childWidth));
            }
            return max;
        }
    }

    public static  double computeChildMinAreaWidth(Region parent, Node child, Insets margin) {
        return computeChildMinAreaWidth(parent, child, -1, margin, -1, false);
    }

    public static double computeChildMinAreaWidth(Region parent, Node child, double baselineComplement, Insets margin, double height, boolean fillHeight) {
        final boolean snap = parent.isSnapToPixel();
        double left = margin != null? snapSpaceX(parent, margin.getLeft(), snap) : 0;
        double right = margin != null? snapSpaceX(parent, margin.getRight(), snap) : 0;
        double alt = -1;
        if (height != -1 && child.isResizable() && child.getContentBias() == Orientation.VERTICAL) { // width depends on height
            double top = margin != null? snapSpaceY(parent, margin.getTop(), snap) : 0;
            double bottom = (margin != null? snapSpaceY(parent, margin.getBottom(), snap) : 0);
            double bo = child.getBaselineOffset();
            final double contentHeight = bo == BASELINE_OFFSET_SAME_AS_HEIGHT && baselineComplement != -1 ?
                    height - top - bottom - baselineComplement :
                    height - top - bottom;
            if (fillHeight) {
                alt = parent.snapSizeY(boundedSize(
                        child.minHeight(-1), contentHeight,
                        child.maxHeight(-1)));
            } else {
                alt = parent.snapSizeY(boundedSize(
                        child.minHeight(-1),
                        child.prefHeight(-1),
                        Math.min(child.maxHeight(-1), contentHeight)));
            }
        }
        return left + parent.snapSizeX(child.minWidth(alt)) + right;
    }

    public static double computeChildMinAreaHeight(Region parent, Node child, Insets margin) {
        return computeChildMinAreaHeight(parent, child, -1, margin, -1);
    }

    public static double computeChildMinAreaHeight(Region parent, Node child, double minBaselineComplement, Insets margin, double width) {
        final boolean snap = parent.isSnapToPixel();
        double top = margin != null ? snapSpaceY(parent, margin.getTop(), snap) : 0;
        double bottom = margin != null ? snapSpaceY(parent, margin.getBottom(), snap) : 0;

        double alt = -1;
        if (child.isResizable() && child.getContentBias() == Orientation.HORIZONTAL) { // height depends on width
            double left = margin != null ? snapSpaceX(parent, margin.getLeft(), snap) : 0;
            double right = margin != null ? snapSpaceX(parent, margin.getRight(), snap) : 0;
            alt = parent.snapSizeX(width != -1 ? boundedSize(child.minWidth(-1), width - left - right, child.maxWidth(-1)) :
                    child.maxWidth(-1));
        }

        // For explanation, see computeChildPrefAreaHeight
        if (minBaselineComplement != -1) {
            double baseline = child.getBaselineOffset();
            if (child.isResizable() && baseline == BASELINE_OFFSET_SAME_AS_HEIGHT) {
                return top + parent.snapSizeY(child.minHeight(alt)) + bottom
                        + minBaselineComplement;
            } else {
                return baseline + minBaselineComplement;
            }
        } else {
            return top + parent.snapSizeY(child.minHeight(alt)) + bottom;
        }
    }


    /**
     * Computes the value based on the given min and max values. We encode in this
     * method the logic surrounding various edge cases, such as when the min is
     * specified as greater than the max, or the max less than the min, or a pref
     * value that exceeds either the max or min in their extremes.
     * <p/>
     * If the min is greater than the max, then we want to make sure the returned
     * value is the min. In other words, in such a case, the min becomes the only
     * acceptable return value.
     * <p/>
     * If the min and max values are well-ordered, and the pref is less than the min
     * then the min is returned. Likewise, if the values are well-ordered and the
     * pref is greater than the max, then the max is returned. If the pref lies
     * between the min and the max, then the pref is returned.
     *
     * @param min  The minimum bound
     * @param pref The value to be clamped between the min and max
     * @param max  the maximum bound
     * @return the size bounded by min, pref, and max.
     */
    public static double boundedSize(double min, double pref, double max) {
        double a = Math.max(pref, min);
        double b = Math.max(min, max);
        return Math.min(a, b);
    }

    /**
     * If snapToPixel is true, then the value is rounded using Math.round. Otherwise,
     * the value is simply returned. This method will surely be JIT'd under normal
     * circumstances, however on an interpreter it would be better to inline this
     * method. However, the use of Math.round here, and Math.ceil in snapSize is
     * not obvious, and so for code maintenance this logic is pulled out into
     * a separate method.
     *
     * @param value       The value that needs to be snapped
     * @param snapToPixel Whether to snap to pixel
     * @return value either as passed in or rounded based on snapToPixel
     */
    public static double snapSpaceX(Region parent, double value, boolean snapToPixel) {
        return snapToPixel ? scaledRound(value, getSnapScaleX(parent)) : value;
    }

    /**
     * If snapToPixel is true, then the value is rounded using Math.round. Otherwise,
     * the value is simply returned. This method will surely be JIT'd under normal
     * circumstances, however on an interpreter it would be better to inline this
     * method. However, the use of Math.round here, and Math.ceil in snapSize is
     * not obvious, and so for code maintenance this logic is pulled out into
     * a separate method.
     *
     * @param value       The value that needs to be snapped
     * @param snapToPixel Whether to snap to pixel
     * @return value either as passed in or rounded based on snapToPixel
     */
    public static double snapSpaceY(Region parent, double value, boolean snapToPixel) {
        return snapToPixel ? scaledRound(value, getSnapScaleY(parent)) : value;
    }

    public static double getSnapScaleY(Node n) {
        return getSnapScaleY(n.getScene());
    }

    public static double getSnapScaleX(Node n) {
        return getSnapScaleX(n.getScene());
    }

    private static double scaledRound(double value, double scale) {
        return Math.round(value * scale) / scale;
    }

    private static double getSnapScaleX(Scene scene) {
        if (scene == null) return 1.0;
        Window window = scene.getWindow();
        if (window == null) return 1.0;
        return window.getRenderScaleY();
    }

    private static double getSnapScaleY(Scene scene) {
        if (scene == null) return 1.0;
        Window window = scene.getWindow();
        if (window == null) return 1.0;
        return window.getRenderScaleY();
    }


    public static final double BASELINE_OFFSET_SAME_AS_HEIGHT = Double.NEGATIVE_INFINITY;

    public static void updateFontFamily(Node node, String family) {
        updateStyle(node, "-fx-font-family ", "'" + family + "'");
    }

    /**
     * Retrieves the node height if it isn't still laid out.
     *
     * @param node the Node of which to know the height
     * @return the calculated height
     */
    public static double getNodeHeight(Node node) {
        Group group = new Group(node);
        Scene scene = new Scene(group);
        group.applyCss();
        group.layout();

        double height = node.prefHeight(-1);
        group.getChildren().clear();
        return height;
    }

    /**
     * Retrieves the node width if it isn't still laid out.
     *
     * @param node the Node of which to know the width
     * @return the calculated width
     */
    public static double getNodeWidth(Node node) {
        Group group = new Group(node);
        Scene scene = new Scene(group);
        group.applyCss();
        group.layout();

        double width = node.prefWidth(-1);
        group.getChildren().clear();
        return width;
    }

    /**
     * Checks if the specified element is in the hierarchy of the specified node.
     */
    public static boolean inHierarchy(Node node, Node element) {
        if (element == null) {
            return true;
        }
        while (node != null) {
            if (node == element) {
                return true;
            }
            node = node.getParent();
        }
        return false;
    }

    /**
     * Checks if the pressed node is in the hierarchy of the specified node, {@link PickResult#getIntersectedNode()}.
     */
    public static boolean inHierarchy(Node node, MouseEvent event) {
        return inHierarchy(node, event.getPickResult().getIntersectedNode());
    }

    /**
     * Checks if the specified node is in hierarchy of the pressed node, {@link PickResult#getIntersectedNode()}.
     */
    public static boolean inHierarchy(MouseEvent event, Node node) {
        return inHierarchy(event.getPickResult().getIntersectedNode(), node);
    }

    public static void toggleVisibility(Node node, boolean on) {
        node.setVisible(on);
        node.setManaged(on);
    }

    public static Insets getMargin(Node child, String constraintKey) {
        Insets insets = getConstraint(child, constraintKey);
        if(insets == null) insets = getConstraint(child, "margin");
        return insets == null ? Insets.EMPTY : insets;
    }

    public static Background cloneBg(Background bg) {
        List<BackgroundFill> fills = bg.getFills();
        List<BackgroundImage> images = bg.getImages();
        return new Background(fills, images);
    }
}