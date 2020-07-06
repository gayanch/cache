package com.github.gayanch.cache;

import com.github.gayanch.cache.config.CacheConfig;
import com.github.gayanch.cache.strategy.EvictionStrategy;
import com.github.gayanch.cache.strategy.StorageStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
public class CacheTest {
    @Test
    public void testCache() {
        Cache<String, String> cache = new CacheBuilder<String, String>()
                .addCache(new CacheConfig("Level1", 2, EvictionStrategy.LFU, StorageStrategy.IN_MEMORY))
                .addCache(new CacheConfig("Level2", 3, EvictionStrategy.LRU, StorageStrategy.FILE_SYSTEM))
                .build();

        cache.put("k1", "v1");
        cache.put("k2", "v2");
        cache.put("k3", "v3");
        cache.put("k4", "v4");
        cache.put("k5", "v5");
        cache.put("k6", "v6");

        //cache miss
        Assertions.assertNull(cache.get("k1"));

        //level1 cache hit
        Assertions.assertNotNull(cache.get("k6"));

        //level 2 cache hit
        Assertions.assertNotNull(cache.get("k3"));

        //This should evict k5 from level1 (Least frequently accessed entry)
        cache.put("k7", "v7");

        //level2 cache hit
        Assertions.assertNotNull(cache.get("k3"));

        //this should evict k4 from level 2 cache (Least recently accessed entry)
        cache.put("k8", "v8");
    }

}
