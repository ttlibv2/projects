package vn.conyeu.commons.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class Sets {

    @SafeVarargs
    public static <E> Set<E> newHashSet(Collection<E>... collections) {
        Asserts.notEmpty(collections, "@Collection<E>[]");
        HashSet<E> hashSet = new HashSet<>();
        for(Collection<E> collection:collections) {
            hashSet.addAll(collection);
        }
        return hashSet;
    }
}