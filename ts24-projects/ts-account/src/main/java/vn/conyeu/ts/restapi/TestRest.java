package vn.conyeu.ts.restapi;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.ts.dtocls.TsVar;
import vn.conyeu.ts.service.TemplateService;

import java.util.*;

@RestController
@PreAuthorize("permitAll()")
@RequestMapping(TsVar.Rest.tsTest)
public class TestRest {
    final CacheManager cacheManager;
    final TemplateService templateService;

    public TestRest(CacheManager cacheManager, TemplateService templateService) {
        this.cacheManager = cacheManager;
        this.templateService = templateService;
    }

    @GetMapping("/get-all")
    public Object getAll() {
        Collection<String> cacheNames = cacheManager.getCacheNames();
        Map<Object, Object> objects = new HashMap<>();
        for(String cacheName:cacheNames) {
            Cache cache = (Cache) Objects.requireNonNull(cacheManager.getCache(cacheName)).getNativeCache();
            objects.put(cacheName, cache.asMap());
        }

        return objects;
    }

    @GetMapping("/clear")
    public void clear() {
        templateService.clearCache();
    }

}