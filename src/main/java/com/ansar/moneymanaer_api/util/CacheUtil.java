package com.ansar.moneymanaer_api.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CacheUtil {
    @Autowired
    private CacheManager cacheManager;

    public void logCacheStats() {

        var cache = cacheManager.getCache("categoryCache");

        if (cache instanceof CaffeineCache caffeineCache) {
            log.info("Cache Stats → {}", caffeineCache.getNativeCache().stats());
        } else {
            log.warn("categoryCache not found or not a Caffeine cache");
        }
    }
}