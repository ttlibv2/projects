package vn.conyeu.javafx.controls.skin;

import javafx.beans.property.DoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import vn.conyeu.javafx.beans.IntegerRange;
import vn.conyeu.javafx.controls.VirtualFlow;
import vn.conyeu.javafx.controls.virtual.*;

import java.util.*;

public class VirtualFlowSkin<T, C extends FlowCell<T>> extends SimpleSkinBase<VirtualFlow<T, C>> {
    private static final double DEFAULT_SIZE = 100.0;
    private final FlowManager<T, C> manager;
    private ViewPort viewport;

    public VirtualFlowSkin(VirtualFlow<T, C> control) {
        super(control);
        manager = control.getViewportManager();
        initialize(control);
    }

    @Override
    public void initializeUI(VirtualFlow<T, C> virtualFlow) {
        viewport = new ViewPort();
        viewport.getStyleClass().setAll("viewport");

        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(virtualFlow.widthProperty());
        clip.heightProperty().bind(virtualFlow.heightProperty());
        clip.arcWidthProperty().bind(virtualFlow.clipBorderRadiusProperty());
        clip.arcHeightProperty().bind(virtualFlow.clipBorderRadiusProperty());

        virtualFlow.setClip(clip);

        // Initialize bindings
        OrientationHelper helper = virtualFlow.getOrientationHelper();
        viewport.translateXProperty().bind(helper.xPosBinding());
        viewport.translateYProperty().bind(helper.yPosBinding());

        // End initialization
        helper.computeEstimatedLength();

        getChildren().setAll(viewport);
    }

    @Override
    public void initializeListener(VirtualFlow<T, C> c) {
        listener.registerChangeListener(c.cellFactoryProperty(), (obs, oldValue, newValue) -> onFactoryChanged());
        listener.registerChangeListener(c.orientationHelperProperty(), (obs, oldValue, newValue) -> onOrientationChanged(oldValue, newValue));
        listener.registerChangeListener(c.needsViewportLayoutProperty(), (obs, oldValue, newValue) -> onLayoutRequest(newValue));
        listener.registerChangeListener(c.stateProperty(), (obs, oldValue, newValue) -> onStateChanged(oldValue, newValue));
        listener.registerListChangeListener(c.itemsProperty(), this::onItemsChanged);
    }

    /**
     * Tells the flow's components what to do when the items in the list change.
     * By default, this causes the re-computation of the estimated length followed
     * by the computation of the new flow' state
     */
    protected void onItemsChanged(ListChangeListener.Change c) {
        manager.onListChange(c);
        getSkinnable().getOrientationHelper().computeEstimatedLength();
    }

    /**
     * Tells the {@link FlowManager} to {@link FlowManager#reset()} the viewport,
     * also resets the last range property of the {@link FlowManager}.
     */
    protected void onFactoryChanged() {
        manager.reset();
        manager.setLastRange(IntegerRange.of(-1));
    }

    /**
     * The default implementation is responsible for updating the viewport's children when the state changes.
     * The new state though must have the {@link FlowState#haveCellsChanged()} flag set to true for this to happen.
     * The nodes are collected through {@link FlowState#getNodes()}.
     */
    protected void onStateChanged(FlowState<T, C> oldValue, FlowState<T, C> newValue) {
        if (newValue == FlowState.EMPTY) viewport.getChildren().clear();
        else if (newValue.haveCellsChanged()) {
            List<Node> nodes = newValue.getNodes();
            viewport.getChildren().setAll(nodes);
        }
    }

    /**
     * The default implementation is responsible for re-binding the viewport's translateX and translateY properties
     * to the bindings created by the new {@link OrientationHelper}.
     *
     * @throws IllegalStateException if the new helper is null
     */
    protected void onOrientationChanged(OrientationHelper oldValue, OrientationHelper newValue) {
        if (newValue == null)
            throw new IllegalStateException("The new provided OrientationHelper is null, you will encounter problems");
        viewport.translateXProperty().bind(newValue.xPosBinding());
        viewport.translateYProperty().bind(newValue.yPosBinding());
    }

    /**
     * The default implementation is responsible for calling {@code Parent#requestLayout()} on the viewport when
     * the {@link VirtualFlow#needsViewportLayoutProperty()} has been set to true.
     */
    protected void onLayoutRequest(Boolean newValue) {
        if (newValue) viewport.requestLayout();
    }


    @Override
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return leftInset + DEFAULT_SIZE + rightInset;
    }

    @Override
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return topInset + DEFAULT_SIZE + bottomInset;
    }

    protected class ViewPort extends Pane {

        @Override
        protected void layoutChildren() {
            VirtualFlow<T, C> virtualFlow = getSkinnable();

            OrientationHelper helper = virtualFlow.getOrientationHelper();
            if (helper == null) {
                throw new IllegalStateException("OrientationHelper is null, cannot proceed with layout");
            }


            DoubleProperty maxBreadth = helper.maxBreadthProperty();
            FlowState<T, C> state = virtualFlow.getState();

            if (state == FlowState.EMPTY || state.isEmpty()) {
                helper.invalidatePos();
                maxBreadth.set(0.0); // TODO add reset breadth to helper?
                return;
            }

            if (state.getType() == UpdateType.CHANGE) {
                helper.invalidatePos();
            }

            Map<Integer, C> cells = state.getCells();
            Set<Double> positions = state.computePositions();

            double mBreadth = 0.0; // Max breadth

            ListIterator<Double> pIt = new ArrayList<>(positions).listIterator(positions.size());
            ListIterator<C> cIt = new ArrayList<>(cells.values()).listIterator(cells.size());
            while (pIt.hasPrevious() && cIt.hasPrevious()) {
                FlowCell<?> cell = cIt.previous();
                Double pos = pIt.previous();
                Node node = cell.getNode();
                cell.beforeLayout();
                double breadth = helper.computeBreadth(node);
                if (breadth > mBreadth) mBreadth = breadth;
                helper.layout(node, pos, breadth);
                cell.afterLayout();
            }

            maxBreadth.set(mBreadth);
            virtualFlow.setNeedsViewportLayout(false);
        }
    }

}