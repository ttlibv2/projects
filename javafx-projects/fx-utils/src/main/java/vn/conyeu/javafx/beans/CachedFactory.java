package vn.conyeu.javafx.beans;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.function.Supplier;
import vn.conyeu.commons.utils.Objects;


public class CachedFactory<T> {
    private final Map<String, Supplier<T>> factories = new HashMap<>();
    private final Map<String, T> cache = new WeakHashMap<>();

    public CachedFactory() {
    }

    public void put(String key, Supplier<T> supplier) {
        if (key != null) {
            if (supplier == null)remove(key);
            else factories.put(key, supplier);
        }
    }

    public Optional<T> get(String key) {
        T value = cache.get(key);
        if(value == null) return Optional.empty();
        if(factories.containsKey(key)) {
            value = factories.get(key).get();
            cache.put(key, value);
        }
        return Optional.ofNullable(value);
    }

    public Optional<T> clearCache(String key) {
        if(Objects.isBlank(key)) return Optional.empty();
        else return Optional.ofNullable(cache.remove(key));
    }

    /**
     * Returns true if cache exists key
     * @see HashMap#containsKey(Object)
     * */
    public boolean containsKey(String key) {
        return factories.containsKey(key);
    }

    /**
     * Removes the mapping for the specified key from both factory and cache.
     * @param key - key whose mapping is to be removed from the map
     * */
    public void remove(String key) {
        cache.remove(key);
        factories.remove(key);
    }
}