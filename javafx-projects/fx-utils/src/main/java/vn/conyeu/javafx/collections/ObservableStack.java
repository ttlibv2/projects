package vn.conyeu.javafx.collections;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.*;

public class ObservableStack<E> extends SimpleListProperty<E> {
    private final LinkedList<E> stack;

    public ObservableStack() {
        stack = new LinkedList<>();
        set(FXCollections.observableList(stack));
    }

    public ObservableStack(ObservableList<E> items) {
        this();
        addAll(items);
    }

    /**
     * Places the item at the top of the stack
     *
     * @param item the item
     * @return the item that was just pushed
     */
    public E push(E item) {
        stack.push(item);
        fireValueChangedEvent(new StackChange(get(),
                ChangeType.PUSH.setChangedObj(Collections.singletonList(item))));
        return item;
    }

    /**
     * @return the item at the top of the stack granted that the stack is not empty
     * @throws NoSuchElementException if the stack is empty
     */
    public E pop() throws NoSuchElementException {
        E temp = stack.pop();
        fireValueChangedEvent(new StackChange(this.get(),
                ChangeType.POP.setChangedObj(Collections.singletonList(temp))));
        return temp;
    }
    /**
     * Pushes the element to the top of the stack
     *
     * @param element the element to add
     * @return Always returns true
     * @see #push(Object)
     */
    @Override
    public boolean add(E element) {
        push(element);
        return true;
    }

    /**
     * Adds an element to the given index
     *
     * @param i       the index to add the element at
     * @param element the element to add to the stack
     * @throws IllegalArgumentException if the index specified is not 0. Only the top of the stack
     *                                  is accessible
     * @see #push(Object)
     */
    @Override
    public void add(int i, E element) throws IllegalArgumentException {
        if (0 == i) {
            push(element);
        }
        throw new IllegalArgumentException("Can only modify the top of the stack " + i);
    }

    /**
     * Adds the elements from the collection into the stack in the order they are specified
     *
     * @param elements the collection to be added to this stack
     * @return true
     * @throws NullPointerException if the collection is null
     */
    @Override
    public boolean addAll(Collection<? extends E> elements) throws NullPointerException {
        elements.forEach(stack::push);
        fireValueChangedEvent(new StackChange(this.get(), ChangeType.PUSH.setChangedObj(new ArrayList<>(elements))));
        return true;
    }

    /**
     * Adds the contents of the array into the stack
     *
     * @param elements the array of elements to add
     * @return true
     * @see #addAll(Collection)
     */
    @Override
    public boolean addAll(E... elements) {
        return addAll(Arrays.asList(elements));
    }

    @Override
    public boolean addAll(int i, Collection<? extends E> elements) {
        throw new UnsupportedOperationException();
    }

    /**
     * Attempt to remove an arbitrary object from the stack is not permitted
     *
     * @param obj The object to remove
     * @return Nothing
     * @throws UnsupportedOperationException Removing an arbitrary object is not permitted Use
     *                                       {@link #pop()}
     */
    @Override
    public boolean remove(Object obj) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Operation not allowed, use pop");
    }

    /**
     * Attempt to remove a range of objects from the stack, this is also not permitted
     * @param from Start removing from here
     * @param to   To here
     */
    @Override
    public void remove(int from, int to) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Operation not allowed, use pop");
    }

    @Override
    public boolean removeAll(E... elements) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> objects) {
        throw new UnsupportedOperationException();
    }

    private enum ChangeType {
        POP, PUSH;

        /**
         * The object that was changed
         */
        private List changedObj;

        /**
         * The changed object(s) are packaged as a list
         *
         * @return The list of changed objects
         */
        public List getChangedObj() {
            return changedObj;
        }

        /**
         * Method to accept the changed object
         *
         * @param obj the list of objects that were changed in the stack
         * @return this enum
         */
        public ChangeType setChangedObj(List obj) {
            this.changedObj = obj;
            return this;
        }
    }

    private final class StackChange extends ListChangeListener.Change<E> {
        private final ChangeType type;
        private boolean onChange;

        /**
         * Constructs a new Change instance on the given list.
         * @param list The list that was changed
         */
        public StackChange(ObservableList<E> list, ChangeType type) {
            super(list);
            this.type = type;
            onChange = false;
        }

        @Override
        public boolean wasAdded() {
            return type == ChangeType.PUSH;
        }

        @Override
        public boolean wasRemoved() {
            return type == ChangeType.POP;
        }

        @Override
        public boolean next() {
            if (!onChange) {
                onChange = true;
                return true;
            }
            return false;
        }

        @Override
        public void reset() {
            onChange = false;
        }

        private void checkState() {
            if (!onChange) {
                throw new IllegalStateException(
                        "Invalid Change state: next() must be called before inspecting the Change.");
            }
        }

        /**
         * Because this is a stack, all push and pop happen to the first item in the stack
         *
         * @return index of the first item
         */
        @Override
        public int getFrom() {
            checkState();
            return 0;
        }

        /**
         * @return the size of the list returned which indicates the end of the change
         */
        @Override
        public int getTo() {
            checkState();
            return type.getChangedObj().size();
        }

        @Override
        public List<E> getRemoved() {
            return wasRemoved() ? type.getChangedObj() :
                    Collections.emptyList();
        }

        @Override
        protected int[] getPermutation() {
            return new int[0];
        }
    }




}