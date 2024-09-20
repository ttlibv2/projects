package vn.conyeu.google.core;

import vn.conyeu.commons.utils.Asserts;

import java.util.LinkedList;
import java.util.function.Function;

public class FindList<E> extends LinkedList<E> {
    private final Function<Integer, E> functionNew;

    public FindList() {
        this(null);
    }

    public FindList(Function<Integer, E> functionNew) {
        this.functionNew = functionNew;
    }

    public E get(int index) {
        tryApplyFunctionNew(index);
        return super.get(index);
    }

    public E find(int index) {
        validateIndex(index);
        return super.get(index);
    }

    public E getFirst() {
        validateEmpty();
        return super.getFirst();
    }

    @Override
    public E getLast() {
        validateEmpty();
        return super.getLast();
    }

    @Override
    public void add(int index, E element) {
        super.add(index, element);
    }

    private void validateEmpty() {
        if (isEmpty()) throw new GoogleException("The list is empty.");
    }

    private void validateIndex(int index) {
        Asserts.validateIndex(index, 0, size());
    }

    private void tryApplyFunctionNew(int index) {
        if (functionNew != null && (index < 0 || index >= size())) {
            int numAdd = index < 0 ? 1 : index - size() + 1;
            for (int pos = 0; pos < numAdd; pos++) {
                int newIndex = size();
                E itemNew = functionNew.apply(newIndex);
                add(newIndex, itemNew);
            }
        }
    }
}