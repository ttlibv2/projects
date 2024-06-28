package vn.conyeu;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.github.benmanes.caffeine.cache.Scheduler;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CaffeineCacheConfig {

    @Bean
    public CacheManagerCustomizer cacheManagerCustomizer(CacheProperties cacheProperties) {
        return (CacheManagerCustomizer<CaffeineCacheManager>) cacheManager -> {
            String specification = cacheProperties.getCaffeine().getSpec();
            Caffeine caffeine = Caffeine.from(specification);
            caffeine.removalListener(new SimpleRemovalListener());
            caffeine.evictionListener(new SimpleEvictListener());
            caffeine.scheduler(Scheduler.systemScheduler());
            cacheManager.setCaffeine(caffeine);
        };
    }

    @Slf4j
    static class SimpleRemovalListener<K, V> implements RemovalListener<K, V> {

        @Override
        public void onRemoval(@Nullable K key, @Nullable V value, RemovalCause cause) {
            log.info(String.format("Key %s was removed (%s)%n", key, cause));
        }
    }

    @Slf4j
    static class SimpleEvictListener<K, V> implements RemovalListener<K, V> {

        @Override
        public void onRemoval(@Nullable K key, @Nullable V value, RemovalCause cause) {
            log.info(String.format("Key %s was evicted (%s)%n", key, cause));
        }
    }
}