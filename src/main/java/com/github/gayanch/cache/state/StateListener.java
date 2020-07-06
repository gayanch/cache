package com.github.gayanch.cache.state;

/**
 * StateListener defines the contract for recording cache events (refer: {@link CacheEvent}) for the purpose of eviction.
 * All events are recorded against CacheKeys
 * @param <K> Type of the Cache Key
 */
public interface StateListener<K> {
    /**
     * Updates the listener state of the cacheKey
     * @param event The {@link CacheEvent}
     * @param cacheKey The cacheKey which state should be updated
     */
    void update(CacheEvent event, K cacheKey);

    /**
     * Calculates the cacheKey for eviction depending on the implemented strategy
     * @return CacheKey to be evicted
     */
    K evict();
}
