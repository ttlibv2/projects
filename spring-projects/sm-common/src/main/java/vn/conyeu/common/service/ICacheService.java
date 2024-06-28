package vn.conyeu.common.service;

public interface ICacheService {

    static ICacheService defaultService() {
        return new DefaultCacheService();
    }

    void clearAll();
    void clearAll(String cacheName);

    class DefaultCacheService implements ICacheService {
        public void clearAll() {}

        public void clearAll(String cacheName) {

        }
    }
}