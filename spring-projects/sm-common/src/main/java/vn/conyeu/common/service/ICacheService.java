package vn.conyeu.common.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public interface ICacheService {

    static ICacheService defaultService() {
        return new DefaultCacheService();
    }

    void clearAll();
    void clearAll(String cacheName);

    Collection<String> getCacheNames();

    class DefaultCacheService implements ICacheService {
        public void clearAll() {}

        public void clearAll(String cacheName) {

        }

        @Override
        public Collection<String> getCacheNames() {
            return new ArrayList<>();
        }
    }
}