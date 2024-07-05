package vn.conyeu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import vn.conyeu.common.service.ICacheService;

import java.util.Collection;
import java.util.Set;

@Service
public class CacheService implements ICacheService {
    final CacheManager cacheManager;

    @Autowired
    public CacheService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public Collection<String> getCacheNames() {
        return cacheManager.getCacheNames();
    }

    @Override
    public void clearAll() {
        Collection<String> cacheNames = cacheManager.getCacheNames();
        for(String cacheName:cacheNames) clearAll(cacheName);
    }

    @Override
    public void clearAll(String cacheName) {
            Cache cache = this.cacheManager.getCache(cacheName);
            if(cache != null) cache.clear();
    }

}