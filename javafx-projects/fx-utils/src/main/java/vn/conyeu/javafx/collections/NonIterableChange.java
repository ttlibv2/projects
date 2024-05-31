package vn.conyeu.javafx.collections;


import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;

import java.util.Collections;
import java.util.List;

public abstract class NonIterableChange<E> extends Change<E> {

    private final int from;
    private final int to;
    private boolean invalid = true;

    protected NonIterableChange(int from, int to, ObservableList<E> list) {
        super(list);
        this.from = from;
        this.to = to;
    }

    @Override
    public int getFrom() {
        checkState();
        return from;
    }

    @Override
    public int getTo() {
        checkState();
        return to;
    }

    @Override
    protected int[] getPermutation() {
        checkState();
        return new int[0];
    }

    @Override
    public boolean next() {
        if (invalid) {
            invalid = false;
            return true;
        }
        return false;
    }

    @Override
    public void reset() {
        invalid = true;
    }

    public void checkState() {
        if (invalid) {
            throw new IllegalStateException("Invalid change state: Make sure to call next() before inspecting the change.");
        }
    }

    @Override
    public String toString() {
        boolean tempInvalid = invalid;
        invalid = false;
        String string;
        if (wasPermutated()) {
            string = ChangeHelper.permChangeToString(getPermutation());
        } else if (wasUpdated()) {
            string = ChangeHelper.updateChangeToString(from, to);
        } else {
            string = ChangeHelper.addRemoveChangeToString(from, to, getList(), getRemoved());
        }
        invalid = tempInvalid;
        return "{ " + string + " }";
    }

    public static class GenericAddRemoveChange<E> extends NonIterableChange<E> {

        private final List<E> removed;

        public GenericAddRemoveChange(int from, int to, List<E> removed, ObservableList<E> list) {
            super(from, to, list);
            this.removed = removed;
        }

        @Override
        public List<E> getRemoved() {
            checkState();
            return removed;
        }

    }

    public static class SimpleRemovedChange<E> extends NonIterableChange<E> {

        private final List<E> removed;

        public SimpleRemovedChange(int from, int to, E removed, ObservableList<E> list) {
            super(from, to, list);
            this.removed = Collections.singletonList(removed);
        }

        @Override
        public boolean wasRemoved() {
            checkState();
            return true;
        }

        @Override
        public List<E> getRemoved() {
            checkState();
            return removed;
        }

    }

    public static class SimpleAddChange<E> extends NonIterableChange<E> {

        public SimpleAddChange(int from, int to, ObservableList<E> list) {
            super(from, to, list);
        }

        @Override
        public boolean wasRemoved() {
            checkState();
            return false;
        }

        @Override
        public List<E> getRemoved() {
            checkState();
            return Collections.emptyList();
        }

    }
}