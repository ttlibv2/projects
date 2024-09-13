package vn.conyeu.google.drives;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class GPage<E> implements Iterable<E> {
    final List<E> list;
    final String pageToken;

    public GPage(List<E> list, String pageToken) {
        this.list = list;
        this.pageToken = pageToken;
    }

    public String getPageToken() {
        return pageToken;
    }

    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }

    /**
     * Returns the list
     */
    public List<E> getData() {
        return List.copyOf(list);
    }

    public <C> GPage<C> mapTo(Function<E, C> mapper) {
        List<C> listC = list.stream().map(mapper).toList();
        return new GPage<>(listC, pageToken);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public E get(int index) {
        return list.get(index);
    }

    public int getSize() {
        return list.size();
    }
}