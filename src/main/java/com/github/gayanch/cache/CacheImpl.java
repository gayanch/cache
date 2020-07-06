package com.github.gayanch.cache;

import com.github.gayanch.cache.exception.StorageFullException;
import com.github.gayanch.cache.state.AccessStateListener;
import com.github.gayanch.cache.state.CacheEvent;
import com.github.gayanch.cache.state.FrequencyStateListener;
import com.github.gayanch.cache.state.StateListener;
import com.github.gayanch.cache.store.Storage;
import com.github.gayanch.cache.strategy.EvictionStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main implementation of the {@link Cache}. Caches can be chained to create multi-level caches each having different
 * Storage backends and Eviction Strategies
 * @param <K> The type of CacheKey
 * @param <V> The type of CacheValue
 */
public class CacheImpl<K, V> implements Cache<K, V> {
    private static final Logger log = LoggerFactory.getLogger(CacheImpl.class);

    private Storage<K, V> storage;
    private StateListener<K> stateListener;
    private Cache<K, V> nextCache;
    private String name;

    public CacheImpl(String name, Storage<K, V> storage, EvictionStrategy evictionStrategy) {
        this.name = name;
        this.storage = storage;

        //Creates a state listener based on the Eviction Strategy
        if (evictionStrategy == EvictionStrategy.LFU) {
            stateListener = new FrequencyStateListener<>();
        } else {
            stateListener = new AccessStateListener<>();
        }
    }

    /**
     * Get CacheValue of given key. In a multi-level cache scenarios, this method will search in low level caches
     * recursively until a cache hit occurs or the cache mis occurs from the last level cache
     * @param key The CacheKey
     * @return The CacheValue of given key. Null if not found.
     */
    @Override
    public V get(K key) {
        V value = storage.read(key);
        stateListener.update(CacheEvent.READ, key);
        if (null == value) {
            log.warn("Key: {} is a cache miss for {} cache", key, name);

            if (null != nextCache) {
                value = nextCache.get(key);
            } else {
                log.warn("Cache miss! No more cache levels to search.");
            }
        } else {
            log.info("Key: {} is a cache hit for {} cache.", key, name);
        }

        return value;
    }

    /**
     * Add CacheValue to Cache store with given key. In a multi-level cache scenario following rules apply,
     * <ul>
     *     <li>
     *     If the high-level cache is full, a cache entry will be removed from the high-level cache based on the
     *     {@link com.github.gayanch.cache.strategy.EvictionStrategy} and new record will be inserted to high-level cache
     *     </li>
     *
     *     <li>
     *     Evicted value from high-level cache will be inserted into immediate lower-level cache unless a cache full occurs
     *     again, in such a case first rule will apply.
     *     </li>
     * </u>
     * @param key The CacheKey
     * @param value The CacheValue
     */
    @Override
    public void put(K key, V value) {
        log.info("Inserting [{} -> {}] into {} cache", key, value, name);
        try {
            storage.store(key, value);
            stateListener.update(CacheEvent.PUT, key);
        } catch (StorageFullException e) {
            log.error("{} cache is full. Starting eviction...", name);
            K evictionKey = stateListener.evict();
            System.out.println("Eviction Key: " + evictionKey);
            V evictedValue = remove(evictionKey);
            log.info("Evicted [{} -> {}] from {} cache.", evictionKey, evictedValue, name);
            put(key, value);

            if (null != nextCache) {
                log.info("Insert evicted [{} -> {}] into {} cache", evictionKey, evictedValue, nextCache.getName());
                nextCache.put(evictionKey, evictedValue);
            } else {
                log.warn("No more cache levels found. Discarding [{} -> {}]", evictionKey, evictedValue);
            }
        }
    }

    @Override
    public V remove(K key) {
        System.out.println("Remove: " + key);
        stateListener.update(CacheEvent.REMOVE, key);
        return storage.remove(key);
    }

    @Override
    public void setMaxSize(int maxSize) {
        this.storage.setMaxSize(maxSize);
    }

    @Override
    public long getMaxSize() {
        return this.storage.getMaxSize();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void appendCache(Cache<K, V> cache) {
        this.nextCache = cache;
    }

    @Override
    public Cache<K, V> getNextLevelCache() {
        return nextCache;
    }
}
