package com.github.gayanch.cache.state;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * AccessStateListener records the access timestamp for given CacheKey based on following rules
 * Cache put operation sets the current timestamp as the last accessed timestamp of the given key
 * Cache get operation updates the last accessed timestamp of the given key with current timestamp
 * @param <K> The type of the CacheKey
 */
public class AccessStateListener<K> implements StateListener<K> {
    private Map<K, Long> cacheKeyAccessTimeMap;

    public AccessStateListener() {
        cacheKeyAccessTimeMap = new HashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(CacheEvent event, K cacheKey) {
        if (null == cacheKey) {
            return;
        }

        switch (event) {
            case PUT:
                cacheKeyAccessTimeMap.put(cacheKey, System.currentTimeMillis());
                break;
            case READ:
                cacheKeyAccessTimeMap.computeIfPresent(cacheKey, (key, val) -> System.currentTimeMillis());
                break;
            case REMOVE:
                cacheKeyAccessTimeMap.remove(cacheKey);
                break;
        }
    }

    /**
     * Calculates the cacheKey to be evicted from cache storage based on the least recent access timestamp
     * @return CacheKey with the least recent access
     */
    @Override
    public K evict() {
        Optional<Map.Entry<K, Long>> evictEntry =
                cacheKeyAccessTimeMap.entrySet().stream().min(Map.Entry.comparingByValue());
        return evictEntry.map(Map.Entry::getKey).orElse(null);
    }

}
