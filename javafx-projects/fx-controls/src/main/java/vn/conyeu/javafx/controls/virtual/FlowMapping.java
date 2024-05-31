package vn.conyeu.javafx.controls.virtual;


import vn.conyeu.javafx.controls.VirtualFlow;

/**
 * Utility API used by the {@link FlowState} to map indexes from an old state to a new one.
 * <p>
 * There are three concrete implementations which extend the common abstract class {@code AbstractMapping}:
 * <p> {@link ValidMapping}: a valid mapping means that a cell can be removed from the old state and copied
 * as is to the new one
 * <p> {@link PartialMapping}: a partial mapping means that a cell should be removed from the old state
 * and copied to the new state after updating its index
 * <p> {@link FullMapping}: a full mapping means that a cell should be removed from the old state and
 * copied to the new state only after both its index and item have been updated.
 * Often a {@code FullMapping} can also receive a negative index as the "oldIndex" parameter of
 * {@link #manage(FlowState, FlowState)}, this means that no cells are left in the old state and a new one
 * must be created to reach {@link FlowState#getTargetSize()}
 */
public interface FlowMapping<T, C extends FlowCell<T>> {
    void manage(FlowState<T, C> oldState, FlowState<T, C> newState);

    int getOldIndex();

    int getNewIndex();

    static <T, C extends FlowCell<T>> FlowMapping<T, C> valid(int oldIndex, int newIndex) {
        return new ValidMapping<>(oldIndex, newIndex);
    }

    static <T, C extends FlowCell<T>> FlowMapping<T, C> partial(int oldIndex, int newIndex) {
        return new PartialMapping<>(oldIndex, newIndex);
    }

    static <T, C extends FlowCell<T>> FlowMapping<T, C> full(int oldIndex, int newIndex) {
        return new FullMapping<>(oldIndex, newIndex);
    }

    abstract class AbstractMapping<T, C extends FlowCell<T>> implements FlowMapping<T, C> {
        protected final int oldIndex;
        protected final int newIndex;

        public AbstractMapping(int oldIndex, int newIndex) {
            this.oldIndex = oldIndex;
            this.newIndex = newIndex;
        }

        @Override
        public int getOldIndex() {
            return oldIndex;
        }

        @Override
        public int getNewIndex() {
            return newIndex;
        }
    }

    class ValidMapping<T, C extends FlowCell<T>> extends AbstractMapping<T, C> {

        public ValidMapping(int oldIndex, int newIndex) {
            super(oldIndex, newIndex);
        }

        @Override
        public void manage(FlowState<T, C> oldState, FlowState<T, C> newState) {
            newState.addCell(newIndex, oldState.getCells().remove(newIndex));
        }
    }

    class PartialMapping<T, C extends FlowCell<T>> extends AbstractMapping<T, C> {

        public PartialMapping(int oldIndex, int newIndex) {
            super(oldIndex, newIndex);
        }

        @Override
        public void manage(FlowState<T, C> oldState, FlowState<T, C> newState) {
            C cell = oldState.getCells().remove(oldIndex);
            cell.updateIndex(newIndex);
            newState.addCell(newIndex, cell);
        }
    }

    class FullMapping<T, C extends FlowCell<T>> extends AbstractMapping<T, C> {

        public FullMapping(int oldIndex, int newIndex) {
            super(oldIndex, newIndex);
        }

        @Override
        public void manage(FlowState<T, C> oldState, FlowState<T, C> newState) {
            VirtualFlow<T, C> virtualFlow = newState.getVirtualFlow();
            T item = virtualFlow.getItems().get(newIndex);
            C cell = oldState.getCells().remove(oldIndex);
            if (cell != null) {
                cell.updateItem(item);
            } else {
                cell = virtualFlow.getCellFactory().apply(item);
            }
            cell.updateIndex(newIndex);
            newState.addCell(newIndex, cell);
        }
    }
}