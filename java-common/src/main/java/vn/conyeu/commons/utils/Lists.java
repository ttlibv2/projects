package vn.conyeu.commons.utils;

import java.util.*;
import java.util.function.Function;
import java.util.random.RandomGenerator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Lists {

    public static <T extends Comparable<? super T>> void sort(List<T> list) {
        Collections.sort(list);
    }

    public static <T> void sort(List<T> list, Comparator<? super T> c) {
        Collections.sort(list, c);
    }

    public static <T> int binarySearch(List<? extends Comparable<? super T>> list, T key) {
        return Collections.binarySearch(list, key);
    }

    public static <T> int binarySearch(List<? extends T> list, T key, Comparator<? super T> c) {
        return Collections.binarySearch(list, key, c);
    }

    public static void reverse(List<?> list) {
        Collections.reverse(list);
    }

    public static void shuffle(List<?> list) {
        Collections.shuffle(list);
    }

    public static void shuffle(List<?> list, Random rnd) {
        Collections.shuffle(list, rnd);
    }

    public static void shuffle(List<?> list, RandomGenerator rnd) {
        Collections.shuffle(list, rnd);
    }

    public static void swap(List<?> list, int i, int j) {
        Collections.swap(list, i, j);
    }

    public static <T> void fill(List<? super T> list, T obj) {
        Collections.fill(list, obj);
    }

    public static <T> void copy(List<? super T> dest, List<? extends T> src) {
        Collections.copy(dest, src);
    }

    public static <T extends Comparable<? super T>> T min(Collection<? extends T> coll) {
        return Collections.min(coll);
    }

    public static <T> T min(Collection<? extends T> coll, Comparator<? super T> comp) {
        return Collections.min(coll, comp);
    }

    public static <T extends Comparable<? super T>> T max(Collection<? extends T> coll) {
        return Collections.max(coll);
    }

    public static <T> T max(Collection<? extends T> coll, Comparator<? super T> comp) {
        return Collections.max(coll, comp);
    }

    public static void rotate(List<?> list, int distance) {
        Collections.rotate(list, distance);
    }

    public static <T> boolean replaceAll(List<T> list, T oldVal, T newVal) {
        return Collections.replaceAll(list, oldVal, newVal);
    }

    public static int indexOfSubList(List<?> source, List<?> target) {
        return Collections.indexOfSubList(source, target);
    }

    public static int lastIndexOfSubList(List<?> source, List<?> target) {
        return Collections.lastIndexOfSubList(source, target);
    }

    public static <T> Collection<T> unmodifiableCollection(Collection<? extends T> c) {
        return Collections.unmodifiableCollection(c);
    }

    public static <T> SequencedCollection<T> unmodifiableSequencedCollection(SequencedCollection<? extends T> c) {
        return Collections.unmodifiableSequencedCollection(c);
    }

    public static <T> Set<T> unmodifiableSet(Set<? extends T> s) {
        return Collections.unmodifiableSet(s);
    }

    public static <T> SequencedSet<T> unmodifiableSequencedSet(SequencedSet<? extends T> s) {
        return Collections.unmodifiableSequencedSet(s);
    }

    public static <T> SortedSet<T> unmodifiableSortedSet(SortedSet<T> s) {
        return Collections.unmodifiableSortedSet(s);
    }

    public static <T> NavigableSet<T> unmodifiableNavigableSet(NavigableSet<T> s) {
        return Collections.unmodifiableNavigableSet(s);
    }

    public static <T> List<T> unmodifiableList(List<? extends T> list) {
        return Collections.unmodifiableList(list);
    }

    public static <K, V> Map<K, V> unmodifiableMap(Map<? extends K, ? extends V> m) {
        return Collections.unmodifiableMap(m);
    }

    public static <K, V> SequencedMap<K, V> unmodifiableSequencedMap(SequencedMap<? extends K, ? extends V> m) {
        return Collections.unmodifiableSequencedMap(m);
    }

    public static <K, V> SortedMap<K, V> unmodifiableSortedMap(SortedMap<K, ? extends V> m) {
        return Collections.unmodifiableSortedMap(m);
    }

    public static <K, V> NavigableMap<K, V> unmodifiableNavigableMap(NavigableMap<K, ? extends V> m) {
        return Collections.unmodifiableNavigableMap(m);
    }

    public static <T> Collection<T> synchronizedCollection(Collection<T> c) {
        return Collections.synchronizedCollection(c);
    }

    public static <T> Set<T> synchronizedSet(Set<T> s) {
        return Collections.synchronizedSet(s);
    }

    public static <T> SortedSet<T> synchronizedSortedSet(SortedSet<T> s) {
        return Collections.synchronizedSortedSet(s);
    }

    public static <T> NavigableSet<T> synchronizedNavigableSet(NavigableSet<T> s) {
        return Collections.synchronizedNavigableSet(s);
    }

    public static <T> List<T> synchronizedList(List<T> list) {
        return Collections.synchronizedList(list);
    }

    public static <K, V> Map<K, V> synchronizedMap(Map<K, V> m) {
        return Collections.synchronizedMap(m);
    }

    public static <K, V> SortedMap<K, V> synchronizedSortedMap(SortedMap<K, V> m) {
        return Collections.synchronizedSortedMap(m);
    }

    public static <K, V> NavigableMap<K, V> synchronizedNavigableMap(NavigableMap<K, V> m) {
        return Collections.synchronizedNavigableMap(m);
    }

    public static <E> Collection<E> checkedCollection(Collection<E> c, Class<E> type) {
        return Collections.checkedCollection(c, type);
    }

    public static <E> Queue<E> checkedQueue(Queue<E> queue, Class<E> type) {
        return Collections.checkedQueue(queue, type);
    }

    public static <E> Set<E> checkedSet(Set<E> s, Class<E> type) {
        return Collections.checkedSet(s, type);
    }

    public static <E> SortedSet<E> checkedSortedSet(SortedSet<E> s, Class<E> type) {
        return Collections.checkedSortedSet(s, type);
    }

    public static <E> NavigableSet<E> checkedNavigableSet(NavigableSet<E> s, Class<E> type) {
        return Collections.checkedNavigableSet(s, type);
    }

    public static <E> List<E> checkedList(List<E> list, Class<E> type) {
        return Collections.checkedList(list, type);
    }

    public static <K, V> Map<K, V> checkedMap(Map<K, V> m, Class<K> keyType, Class<V> valueType) {
        return Collections.checkedMap(m, keyType, valueType);
    }

    public static <K, V> SortedMap<K, V> checkedSortedMap(SortedMap<K, V> m, Class<K> keyType, Class<V> valueType) {
        return Collections.checkedSortedMap(m, keyType, valueType);
    }

    public static <K, V> NavigableMap<K, V> checkedNavigableMap(NavigableMap<K, V> m, Class<K> keyType, Class<V> valueType) {
        return Collections.checkedNavigableMap(m, keyType, valueType);
    }

    public static <T> Iterator<T> emptyIterator() {
        return Collections.emptyIterator();
    }

    public static <T> ListIterator<T> emptyListIterator() {
        return Collections.emptyListIterator();
    }

    public static <T> Enumeration<T> emptyEnumeration() {
        return Collections.emptyEnumeration();
    }

    public static <T> Set<T> emptySet() {
        return Collections.emptySet();
    }

    public static <E> SortedSet<E> emptySortedSet() {
        return Collections.emptySortedSet();
    }

    public static <E> NavigableSet<E> emptyNavigableSet() {
        return Collections.emptyNavigableSet();
    }

    public static <T> List<T> emptyList() {
        return Collections.emptyList();
    }

    public static <K, V> Map<K, V> emptyMap() {
        return Collections.emptyMap();
    }

    public static <K, V> SortedMap<K, V> emptySortedMap() {
        return Collections.emptySortedMap();
    }

    public static <K, V> NavigableMap<K, V> emptyNavigableMap() {
        return Collections.emptyNavigableMap();
    }

    public static <T> Set<T> singleton(T o) {
        return Collections.singleton(o);
    }

    public static <T> List<T> singletonList(T o) {
        return Collections.singletonList(o);
    }

    public static <K, V> Map<K, V> singletonMap(K key, V value) {
        return Collections.singletonMap(key, value);
    }

    public static <T> List<T> nCopies(int n, T o) {
        return Collections.nCopies(n, o);
    }

    public static <T> Comparator<T> reverseOrder() {
        return Collections.reverseOrder();
    }

    public static <T> Comparator<T> reverseOrder(Comparator<T> cmp) {
        return Collections.reverseOrder(cmp);
    }

    public static <T> Enumeration<T> enumeration(Collection<T> c) {
        return Collections.enumeration(c);
    }

    public static <T> ArrayList<T> list(Enumeration<T> e) {
        return Collections.list(e);
    }

    public static int frequency(Collection<?> c, Object o) {
        return Collections.frequency(c, o);
    }

    public static boolean disjoint(Collection<?> c1, Collection<?> c2) {
        return Collections.disjoint(c1, c2);
    }

    @SafeVarargs
    public static <T> boolean addAll(Collection<? super T> c, T... elements) {
        return Collections.addAll(c, elements);
    }

    public static <E> Set<E> newSetFromMap(Map<E, Boolean> map) {
        return Collections.newSetFromMap(map);
    }

    public static <E> SequencedSet<E> newSequencedSetFromMap(SequencedMap<E, Boolean> map) {
        return Collections.newSequencedSetFromMap(map);
    }

    public static <T> Queue<T> asLifoQueue(Deque<T> deque) {
        return Collections.asLifoQueue(deque);
    }



    /**
     * @param <T> the type of the input elements
     * @return a {@code Collector} which collects all the input elements into a
     * {@code List}, in encounter order
     */
    public static <T> Collector<T, ?, LinkedList<T>> toLinkedList() {
        return Collector.of(LinkedList::new, LinkedList::add, (l, r) -> {
            l.addAll(r);return l;
        }, Collector.Characteristics.IDENTITY_FINISH);
    }


    /**
     * Returns a new mutable, empty {@code ArrayList} instance.
     */
    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<E>();
    }

    /**
     * Creates an {@code ArrayList} instance backed by an array of the <i>exact</i> size specified;
     * equivalent to {@link ArrayList#ArrayList(int)}.
     *
     * @param initialArraySize the exact size of the initial backing array for the returned array list
     *                         ({@code ArrayList} documentation calls this value the "capacity")
     * @return a new, empty {@code ArrayList} which is guaranteed not to resize itself unless its size
     * reaches {@code initialArraySize + 1}
     * @throws IllegalArgumentException if {@code initialArraySize} is negative
     */
    public static <E> ArrayList<E> newArrayListWithCapacity(int initialArraySize) {
        return new ArrayList<E>(initialArraySize);
    }

    public static <E> ArrayList<E> newArrayListWithDefault(int initialArraySize, E defaultValue) {
        ArrayList arrayList = newArrayListWithCapacity(initialArraySize);
        for(int index=0;index < initialArraySize;index++)arrayList.add(defaultValue);
        return arrayList;
    }

    /**
     * Returns a new mutable {@code ArrayList} instance containing the given elements.
     *
     * @param elements the elements that the list should contain, in order
     * @return a new {@code ArrayList} containing those elements
     */
    public static <E> ArrayList<E> newArrayList(Iterable<? extends E> elements) {
        return elements instanceof Collection collection
                ? new ArrayList<E>(collection)
                : newArrayList(elements.iterator());
    }

    /**
     * Returns a new mutable {@code ArrayList} instance containing the given elements.
     *
     * @param elements the elements that the list should contain, in order
     * @return a new {@code ArrayList} containing those elements
     */
    public static <E> ArrayList<E> newArrayList(Iterator<? extends E> elements) {
        ArrayList<E> list = newArrayList();
        while (elements.hasNext()) {
            list.add(elements.next());
        }
        return list;
    }

    public static <E> List<E> newList(Collection<E> items, E...lastItems) {
        List<E> newList = newArrayList(items);
        newList.addAll(List.of(lastItems));
        return newList;
    }

    public static <E> List<E> newList(E first, Collection<E> items) {
        List<E> newList = newArrayList(items);
        newList.add(0, first);
        return newList;
    }

    public static <E> List<E> newList(E... data) {
        return new ArrayList<>(Arrays.asList(data));
    }


    public static <T> List<T> copyOf(Collection<T>... list) {
        return Stream.of(list).flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public static <T> List<T> copyOf(List<T> list) {
        if (isUnmodifiableType(list.getClass())) return list;
        Class cls = list.getClass();
        if(ArrayList.class.equals(cls)) return new ArrayList<>(list);
        if(LinkedList.class.equals(cls)) return new LinkedList<>(list);
        else return new LinkedList<>(list);
    }

    public static <T> Set<T> copyOfToSet(Collection<T>... list) {
        return Stream.of(list).flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public static <T> List<T> createList() {
        return new LinkedList<>();
    }

    /**
     * Convert object to list
     */
    public static <T> List<T> toList(Object object, Class<T> classType) {
        return toList(object, o -> MapperHelper.convert(o, classType));
    }


    /**
     * Convert object to list
     */
    public static <T> List<T> toList(Object object, Function<Object, T> itemMapper) {
        Asserts.notNull(itemMapper, "@itemMapper");
        if (Objects.isNull(object)) return null;

        //--Collection
        if (object instanceof Collection<?> cl) {
            return cl.stream().map(itemMapper).toList();
        }

        //--isArray
        if (object.getClass().isArray()) {
            return Stream.of(object).map(itemMapper).toList();
        } else {
            T value = itemMapper.apply(object);
            return Collections.singletonList(value);
        }
    }

    private static final Set<Class<?>> UNMODIFIABLE_TYPES;


    /**
     * Returns true if the type is a known unmodifiable type.
     *
     * @param clazz the type
     * @return true if the type is a known unmodifiable type
     */
    public static boolean isUnmodifiableType(Class<?> clazz) {
        for (; clazz != null; clazz = clazz.getSuperclass()) {
            if (UNMODIFIABLE_TYPES.contains(clazz)) {
                return true;
            }
        }
        return false;
    }


    static {
        Set<Class<?>> unmodifiableTypes = new HashSet<>();
        unmodifiableTypes.add(Collections.emptyList().getClass());
        unmodifiableTypes.add(Collections.emptySet().getClass());
        unmodifiableTypes.add(Collections.emptyNavigableSet().getClass());
        unmodifiableTypes.add(Collections.emptySortedSet().getClass());
        unmodifiableTypes.add(Collections.emptyMap().getClass());
        unmodifiableTypes.add(Collections.emptySortedMap().getClass());
        unmodifiableTypes.add(Collections.emptyNavigableMap().getClass());
        unmodifiableTypes.add(Collections.singleton(1).getClass());
        unmodifiableTypes.add(Collections.singletonList(1).getClass());
        unmodifiableTypes.add(Collections.singletonMap(1, 1).getClass());
        unmodifiableTypes.add(Collections.unmodifiableList(Collections.emptyList()).getClass());
        unmodifiableTypes.add(Collections.unmodifiableCollection(Collections.emptyList()).getClass());
        unmodifiableTypes.add(Collections.unmodifiableSet(Collections.emptySet()).getClass());
        unmodifiableTypes.add(Collections.unmodifiableNavigableSet(Collections.emptyNavigableSet()).getClass());
        unmodifiableTypes.add(Collections.unmodifiableSortedSet(Collections.emptySortedSet()).getClass());
        unmodifiableTypes.add(Collections.unmodifiableMap(Collections.emptyMap()).getClass());
        unmodifiableTypes.add(Collections.unmodifiableSortedMap(Collections.emptySortedMap()).getClass());
        unmodifiableTypes.add(Collections.unmodifiableNavigableMap(Collections.emptyNavigableMap()).getClass());

        try {
            unmodifiableTypes.add(Class.forName("com.google.common.collect.ImmutableSet"));
            unmodifiableTypes.add(Class.forName("com.google.common.collect.ImmutableList"));
            unmodifiableTypes.add(Class.forName("com.google.common.collect.ImmutableMap"));
        } catch (ClassNotFoundException e) {
            // Nothing happens
        }

        try {
            unmodifiableTypes.add(Class.forName("java.util.ImmutableCollections$AbstractImmutableCollection"));
            unmodifiableTypes.add(Class.forName("java.util.ImmutableCollections$AbstractImmutableMap"));
        } catch (ClassNotFoundException e) {
            // Nothing happens
        }

        UNMODIFIABLE_TYPES = Collections.unmodifiableSet(unmodifiableTypes);
    }

}