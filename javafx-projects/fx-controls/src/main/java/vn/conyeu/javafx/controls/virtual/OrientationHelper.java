package vn.conyeu.javafx.controls.virtual;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.javafx.bindings.BindingBuilder;
import vn.conyeu.javafx.controls.VirtualFlow;
import vn.conyeu.javafx.events.LambdaListener;
import vn.conyeu.javafx.utils.FxUtils;
import vn.conyeu.javafx.utils.Layouts;

public interface OrientationHelper {

    static <T, C extends FlowCell<T>> OrientationHelper valueOf(VirtualFlow<T, C> flow, Orientation orientation) {
        return Orientation.HORIZONTAL == orientation ? new HorizontalHelper(flow) : new VerticalHelper(flow);
    }

    /**
     * @return the index of the first visible item
     */
    int firstVisible();

    /**
     * @return the index of the last visible item
     */
    int lastVisible();

    /**
     * @return the maximum number of cells the viewport can display, this also
     * includes one cell of overscan/buffer
     */
    int maxCells();

    /**
     * @return the maximum amount of pixels the virtual flow can scroll on the vertical direction
     */
    double maxVScroll();

    /**
     * @return the maximum amount of pixels the virtual flow can scroll on the horizontal direction
     */
    double maxHScroll();

    /**
     * @return the virtual length of the viewport, the {@code VirtualFlow#cellSizeProperty()} multiplied
     * by the number of items in the list
     */
    double computeEstimatedLength();

    /**
     * Holds the result of {@code #computeEstimatedLength()}.
     */
    DoubleProperty estimatedLengthProperty();

    /**
     * Responsible for computing the breadth of the given node.
     */
    double computeBreadth(Node node);

    /**
     * Specifies the maximum length, opposed to the virtual flow orientation, among the displayed cells.
     * So: VERTICAL -> max width, HORIZONTAL -> max height
     */
    DoubleProperty maxBreadthProperty();

    /**
     * This binding holds the horizontal position of the viewport.
     */
    DoubleBinding xPosBinding();

    /**
     * This binding holds the vertical position of the viewport.
     */
    DoubleBinding yPosBinding();

    /**
     * Invalidates both {@code VirtualFlow#hPosProperty()} and {@code VirtualFlow#vPosProperty()} in case changes
     * occurred in the viewport and the current position are no longer valid.
     */
    void invalidatePos();

    /**
     * Scrolls in the viewport by the given amount of pixels.
     */
    void scrollBy(double pixels);

    /**
     * Scrolls in the viewport to the given pixel value.
     */
    void scrollToPixel(double pixel);

    /**
     * Scrolls in the viewport to the given item's index.
     */
    void scrollToIndex(int index);

    /**
     * Responsible for calling {@code Node#resizeRelocate(double, double, double, double)} with the needed parameters,
     * this is used to position and resize cells.
     */
    void layout(Node node, double pos, double breadth);

    /**
     * Disposes bindings/listeners that are not required anymore, for example
     * when changing orientation and helper as well, the old is disposed.
     */
    void dispose();

    /**
     * Abstract implementation of {@code OrientationHelper}, base class for both {@code HorizontalHelper} and
     * {@code VerticalHelper} to extract common properties.
     */
    abstract class Base implements OrientationHelper {
        protected final VirtualFlow virtualFlow;
        protected final FlowManager manager;
        protected final LambdaListener listener = new LambdaListener();
        protected DoubleBinding xPosBinding;
        protected DoubleBinding yPosBinding;

        public Base(VirtualFlow virtualFlow) {
            this.virtualFlow = Asserts.notNull(virtualFlow);
            this.manager = virtualFlow.getViewportManager();
        }

        protected void initializeListener() {
            listener.registerChangeListener(virtualFlow.widthProperty(), (obs, ov, width) -> changeFlowWidth(width));
            listener.registerChangeListener(virtualFlow.heightProperty(), (obs, ov, height) -> changeFlowHeight(height));
        }

        protected void changeFlowHeight(Number height) {
            virtualFlow.requestViewportLayout();
        }

        protected void changeFlowWidth(Number width) {
            if (width.doubleValue() > 0) manager.init();
        }

        @Override
        public DoubleProperty estimatedLengthProperty() {
            return virtualFlow.estimatedLengthProperty();
        }

        @Override
        public DoubleProperty maxBreadthProperty() {
            return virtualFlow.maxBreadthProperty();
        }

        public double getMaxBreadth() {
            return maxBreadthProperty().get();
        }

        public double getEstimatedLength() {
            return estimatedLengthProperty().get();
        }

        @Override
        public void dispose() {
            listener.dispose();
        }
    }

    /**
     * Concrete implementation of {@code AbstractOrientationHelper} for managing the virtual flow when its orientation
     * is HORIZONTAL.
     * <p></p>
     * This helper adds the following listeners:
     * <p> - A listener to the virtual flow's width to re-initialize the viewport, {@code FlowManager#init()}
     * <p> - A listener to the virtual flow's height to re-layout, {@code VirtualFlow#requestViewportLayout()}
     * <p> - A listener on the virtual flow's hPos property to process the scroll, {@code FlowManager#onScroll()}
     */
    class HorizontalHelper extends Base {


        public HorizontalHelper(VirtualFlow<?, ?> virtualFlow) {
            super(virtualFlow);
            initializeListener();
        }

        @Override
        protected void initializeListener() {
            listener.registerInvalidationListener(virtualFlow.hPosProperty(), e -> manager.onScroll());
        }

        @Override
        public int firstVisible() {
            final double val = Math.floor(virtualFlow.getHPos() / virtualFlow.getCellSize());
            return FxUtils.clamp(val , 0, virtualFlow.getItems().size() - 1);
        }

        @Override
        public int lastVisible() {
            final double val = firstVisible() + maxCells() - 1;
            return FxUtils.clamp(val, 0, virtualFlow.getItems().size() - 1);
        }

        @Override
        public int maxCells() {
            return (int) (Math.ceil(virtualFlow.getWidth() / virtualFlow.getCellSize()) + 1);
        }

        /**
         * {@inheritDoc}
         * <p>
         * The value is given by: {@cde maxBreadth - virtualFlow.getHeight()}
         */
        @Override
        public double maxVScroll() {
            return maxBreadthProperty().get() - virtualFlow.getHeight();
        }

        /**
         * {@inheritDoc}
         * <p>
         * The value is given by: {@code estimatedLength - virtualFlow.getWidth()}
         */
        @Override
        public double maxHScroll() {
            return estimatedLengthProperty().get() - virtualFlow.getWidth();
        }

        @Override
        public double computeEstimatedLength() {
            double val = virtualFlow.getItems().size() * virtualFlow.getCellSize();
            estimatedLengthProperty().set(val);
            return val;
        }

        /**
         * {@inheritDoc}
         * <p>
         * The {@code HorizontalHelper} computes the values depending on {@code VirtualFlow#fitToBreadthProperty()}:
         * <p> - True: {@code virtualFlow.getHeight()}
         * <p> - False: {@code LayoutUtils#boundHeight(Node)}
         */
        @Override
        public double computeBreadth(Node node) {
            boolean fitToBreadth = virtualFlow.isFitToBreadth();
            return fitToBreadth ? virtualFlow.getHeight() : Layouts.boundHeight(node);
        }

        /**
         * {@inheritDoc}
         * <p>
         * In the {@code HorizontalHelper} this is the direction along the estimated length. However, the implementation
         * makes it so that the position of the viewport is virtual. This binding which depends on both {@code VirtualFlow#hPosProperty()}
         * and {@code VirtualFlow#cellSizeProperty()} will always return a value that is greater or equal to 0 and lesser
         * than the cell size.
         * <p>
         * This is the formula: {@code -virtualFlow.getHPos() % virtualFlow.getCellSize()}.
         * <p>
         * Think about this. We have cells of size 64. and we scroll 15px on each gesture. When we reach 60px, we can still
         * see the cell for 4px, but once we scroll again it makes to sense to go to 75px because the first cell won't be
         * visible anymore, so we go back at 11px. Now the top cell will be visible for 53px. Keep in mind that cells
         * are always positioned from the end to 0 (exceptions documented here {@code FlowState#computePositions()}).
         * <p>
         * Long story short, scrolling is just an illusion, the viewport just scroll by a little to give this illusion and
         * when needed the cells are just repositioned from the end. This is important because the estimated length
         * could, in theory, reach very high values, so we don't want the viewport to scroll by thousands of pixels.
         */
        @Override
        public DoubleBinding xPosBinding() {
            if (xPosBinding == null) {
                xPosBinding = BindingBuilder.doubleBuilder()
                        .setMapper( () -> -virtualFlow.getHPos() % virtualFlow.getCellSize())
                        .addSources(virtualFlow.hPosProperty(), virtualFlow.cellSizeProperty()).get();
            }
            return xPosBinding;
        }

        /**
         * {@inheritDoc}
         * <p>
         * In the {@code HorizontalHelper} this is the direction along the max breadth, this depends on
         * {@code VirtualFlow#vPosProperty()}, {@code VirtualFlow#heightProperty()} and {@code #maxBreadthProperty()}.
         * <p>
         * This is computed as: {@code -NumberUtils.clamp(virtualFlow.getVPos(), 0.0, maxBreadth.get() - virtualFlow.getHeight())}
         */
        @Override
        public DoubleBinding yPosBinding() {
            if (yPosBinding == null) {
                yPosBinding = BindingBuilder.doubleBuilder()
                        .setMapper( ()->-FxUtils.clamp(virtualFlow.getVPos(), 0.0,getMaxBreadth() - virtualFlow.getHeight()))
                        .addSources(virtualFlow.vPosProperty(), virtualFlow.heightProperty(), maxBreadthProperty()).get();
            }
            return yPosBinding;
        }

        @Override
        public void invalidatePos() {
            virtualFlow.setVPos(Math.min(virtualFlow.getVPos(), getMaxBreadth()));
            virtualFlow.setHPos(Math.min(virtualFlow.getHPos(), getEstimatedLength()));
        }

        @Override
        public void scrollBy(double pixels) {
            double newVal = FxUtils.clamp(virtualFlow.getHPos() + pixels, 0, maxHScroll());
            virtualFlow.setHPos(newVal);
        }

        @Override
        public void scrollToPixel(double pixel) {
            double clampedVal = FxUtils.clamp(pixel, 0, maxHScroll());
            virtualFlow.setHPos(clampedVal);
        }

        @Override
        public void scrollToIndex(int index) {
            double val = index * virtualFlow.getCellSize();
            double clampedVal = FxUtils.clamp(val, 0, maxHScroll());
            virtualFlow.setHPos(clampedVal);
        }

        /**
         * {@inheritDoc}
         *
         * @param node    the node to layout
         * @param pos     the x position
         * @param breadth the node's height
         */
        @Override
        public void layout(Node node, double pos, double breadth) {
            double size = virtualFlow.getCellSize();
            node.resizeRelocate(pos, 0, size, breadth);
        }

        @Override
        public void dispose() {
            if (xPosBinding != null) {
                xPosBinding.dispose();
                xPosBinding = null;
            }

            if (yPosBinding != null) {
                yPosBinding.dispose();
                yPosBinding = null;
            }

            super.dispose();
        }
    }

    /**
     * Concrete implementation of {@code AbstractOrientationHelper} for managing the virtual flow when its orientation
     * is VERTICAL.
     * <p></p>
     * This helper adds the following listeners:
     * <p> - A listener to the virtual flow's height to re-initialize the viewport, {@code FlowManager#init()}
     * <p> - A listener to the virtual flow's width to re-layout, {@code VirtualFlow#requestViewportLayout()}
     * <p> - A listener on the virtual flow's vPos property to process the scroll, {@code FlowManager#onScroll()}
     */
    class VerticalHelper extends Base {

        public VerticalHelper(VirtualFlow<?, ?> virtualFlow) {
            super(virtualFlow);
            initializeListener();
        }

        @Override
        protected void initializeListener() {
            super.initializeListener();
            listener.registerChangeListener(virtualFlow.vPosProperty(), (obs, ov, vpos) -> manager.onScroll());
        }

        @Override
        protected void changeFlowHeight(Number height) {
            if (height.doubleValue() > 0) manager.init();
        }

        @Override
        protected void changeFlowWidth(Number width) {
            virtualFlow.requestViewportLayout();
        }

        @Override
        public int firstVisible() {
            return FxUtils.clamp(
                    (int) Math.floor(virtualFlow.getVPos() / virtualFlow.getCellSize()),
                    0,
                    virtualFlow.getItems().size() - 1
            );
        }

        @Override
        public int lastVisible() {
            return FxUtils.clamp(firstVisible() + maxCells() - 1, 0, virtualFlow.getItems().size() - 1);
        }

        @Override
        public int maxCells() {
            return (int) (Math.ceil(virtualFlow.getHeight() / virtualFlow.getCellSize()) + 1);
        }

        /**
         * {@inheritDoc}
         * <p>
         * The value is given by: {@code estimatedLength - virtualFlow.getHeight()}
         */
        @Override
        public double maxVScroll() {
            return estimatedLengthProperty().get() - virtualFlow.getHeight();
        }

        /**
         * {@inheritDoc}
         * <p>
         * The value is given by: {@cde maxBreadth - virtualFlow.getWidth()}
         */
        @Override
        public double maxHScroll() {
            return maxBreadthProperty().get() - virtualFlow.getWidth();
        }

        @Override
        public double computeEstimatedLength() {
            double val = virtualFlow.getItems().size() * virtualFlow.getCellSize();
            estimatedLengthProperty().set(val);
            return val;
        }

        /**
         * {@inheritDoc}
         * <p>
         * The {@code VerticalHelper} computes the values depending on {@code VirtualFlow#fitToBreadthProperty()}:
         * <p> - True: {@code virtualFlow.getWidth()}
         * <p> - False: {@code LayoutUtils#boundWidth(Node)}
         */
        @Override
        public double computeBreadth(Node node) {
            boolean fitToBreadth = virtualFlow.isFitToBreadth();
            return fitToBreadth ? virtualFlow.getWidth() : Layouts.boundWidth(node);
        }

        /**
         * {@inheritDoc}
         * <p>
         * In the {@code VerticalHelper} this is the direction along the max breadth, this depends on
         * {@code VirtualFlow#hPosProperty()}, {@code VirtualFlow#widthProperty()} and {@code #maxBreadthProperty()}
         * <p>
         * This is computed as: {@code -FxUtils.clamp(virtualFlow.getHPos(), 0.0, maxBreadth.get() - virtualFlow.getWidth())}
         */
        @Override
        public DoubleBinding xPosBinding() {
            if (xPosBinding == null) {
                xPosBinding = Bindings.createDoubleBinding(
                        () -> -FxUtils.clamp(virtualFlow.getHPos(), 0.0, getMaxBreadth() - virtualFlow.getWidth()),
                        virtualFlow.hPosProperty(), virtualFlow.widthProperty(), maxBreadthProperty()
                );
            }
            return xPosBinding;
        }

        /**
         * {@inheritDoc}
         * <p>
         * In the {@code VerticalHelper} this is the direction along the estimated length. However, the implementation
         * makes it so that the position of the viewport is virtual. This binding which depends on both {@code VirtualFlow#vPosProperty()}
         * and {@code VirtualFlow#cellSizeProperty()} will always return a value that is greater or equal to 0 and lesser
         * than the cell size.
         * <p>
         * This is the formula: {@code -virtualFlow.getVPos() % virtualFlow.getCellSize()}.
         * <p>
         * Think about this. We have cells of size 64. and we scroll 15px on each gesture. When we reach 60px, we can still
         * see the cell for 4px, but once we scroll again it makes to sense to go to 75px because the first cell won't be
         * visible anymore, so we go back at 11px. Now the top cell will be visible for 53px. Keep in mind that cells
         * are always positioned from the end to 0 (exceptions documented here {@code FlowState#computePositions()}).
         * <p>
         * Long story short, scrolling is just an illusion, the viewport just scroll by a little to give this illusion and
         * when needed the cells are just repositioned from the end. This is important because the estimated length
         * could, in theory, reach very high values, so we don't want the viewport to scroll by thousands of pixels.
         */
        @Override
        public DoubleBinding yPosBinding() {
            if (yPosBinding == null) {
                yPosBinding = Bindings.createDoubleBinding(
                        () -> -virtualFlow.getVPos() % virtualFlow.getCellSize(),
                        virtualFlow.vPosProperty(), virtualFlow.cellSizeProperty()
                );
            }
            return yPosBinding;
        }

        @Override
        public void invalidatePos() {
            virtualFlow.setVPos(Math.min(virtualFlow.getVPos(), getEstimatedLength()));
            virtualFlow.setHPos(Math.min(virtualFlow.getHPos(), getMaxBreadth()));
        }

        @Override
        public void scrollBy(double pixels) {
            double newVal = FxUtils.clamp(virtualFlow.getVPos() + pixels, 0, maxVScroll());
            virtualFlow.setVPos(newVal);
        }

        @Override
        public void scrollToPixel(double pixel) {
            double clampedVal = FxUtils.clamp(pixel, 0, maxVScroll());
            virtualFlow.setVPos(clampedVal);
        }

        @Override
        public void scrollToIndex(int index) {
            double val = index * virtualFlow.getCellSize();
            double clampedVal = FxUtils.clamp(val, 0, maxVScroll());
            virtualFlow.setVPos(clampedVal);
        }

        /**
         * {@inheritDoc}
         *
         * @param node    the node to layout
         * @param pos     the y position
         * @param breadth the node's width
         */
        @Override
        public void layout(Node node, double pos, double breadth) {
            double size = virtualFlow.getCellSize();
            node.resizeRelocate(0, pos, breadth, size);
        }

        @Override
        public void dispose() {
            if (xPosBinding != null) {
                xPosBinding.dispose();
                xPosBinding = null;
            }

            if (yPosBinding != null) {
                yPosBinding.dispose();
                yPosBinding = null;
            }

            super.dispose();
        }
    }

}