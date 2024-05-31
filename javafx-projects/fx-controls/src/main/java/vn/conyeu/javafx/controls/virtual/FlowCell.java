package vn.conyeu.javafx.controls.virtual;

import javafx.scene.Node;

public interface FlowCell<T> {

    /**
     * Returns the cell's node.
     * The ideal way to implement a cell would be to extend a JavaFX's pane/region
     * and override this method to return "this".
     */
    Node getNode();

    /**
     * Automatically called by the virtualized control.
     * <p>
     * This method must be implemented to correctly
     * update the Cell's content on scroll.
     * <p>
     * <b>Note:</b> if the Cell's content is a Node this method should
     * also re-set the Cell's children because (quoting from JavaFX doc)
     * 'A node may occur at most once anywhere in the scene graph' and it's
     * possible that a Node may be removed from a Cell to be the content
     * of another Cell.
     */
    void updateItem(T item);

    /**
     * Automatically called by the virtualized control.
     * <p>
     * Cells are dumb, they have no logic, no state.
     * This method allow cells implementations to keep track of a cell's index.
     * <p>
     * Default implementation is empty.
     */
    default void updateIndex(int index) {
    }

    /**
     * Automatically called after the cell has been laid out.
     * <p>
     * Default implementation is empty.
     */
    default void afterLayout() {
    }

    /**
     * Automatically called before the cell is laid out.
     * <p>
     * Default implementation is empty.
     */
    default void beforeLayout() {
    }

    /**
     * Automatically called before the cell's node is removed from the container.
     * <p>
     * Default implementation is empty.
     */
    default void dispose() {
    }
}