package com.github.gayanch.cache.state;

import java.util.*;

/**
 * FrequencyStateListener records access frequencies of CacheKeys based on following rules,
 * Cache put operation sets the access frequency as 1 for the given Key
 * Cache get operation increases the access frequency by one for the given cacheKey
 * @param <K> Type of the CacheKey
 */
public class FrequencyStateListener<K> implements StateListener<K> {
    private Map<K, Long> cacheKeyAccessFrequencyMap;

    public FrequencyStateListener() {
        cacheKeyAccessFrequencyMap = new HashMap<>();
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
                cacheKeyAccessFrequencyMap.put(cacheKey, 1l);
                break;
            case READ:
                cacheKeyAccessFrequencyMap.computeIfPresent(cacheKey, (key, val) -> val +1);
                break;
            case REMOVE:
                cacheKeyAccessFrequencyMap.remove(cacheKey);
                break;
        }
    }

    /**
     * Calculates the cacheKey to be evicted from cache storage based on the least accessed frequency
     * @return CacheKey with the least access frequency
     */
    @Override
    public K evict() {
        Optional<Map.Entry<K, Long>> evictEntry =
                cacheKeyAccessFrequencyMap.entrySet().stream().min(Map.Entry.comparingByValue());
        return evictEntry.map(Map.Entry::getKey).orElse(null);
    }
}
