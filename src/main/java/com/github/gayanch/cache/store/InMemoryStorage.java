package com.github.gayanch.cache.store;

import com.github.gayanch.cache.exception.StorageFullException;

import java.util.HashMap;
import java.util.Map;

/**
 * Storage backend implementation for in-memory cache
 * @param <K> The type of CacheKey
 * @param <V> The type of CacheValue
 */
public class InMemoryStorage<K, V> implements Storage<K, V> {
    private Map<K, V> store;
    private int maxSize;

    public InMemoryStorage(int maxSize) {
        store = new HashMap<>();
        this.maxSize = maxSize;
    }

    @Override
    public void store(K key, V value) throws StorageFullException {
        if (size() >= maxSize) {
            throw new StorageFullException("Storage is full");
        }
        store.put(key, value);
    }

    @Override
    public V read(K key) {
        return store.get(key);
    }

    @Override
    public V remove(K key) {
        return store.remove(key);
    }

    @Override
    public int size() {
        return store.size();
    }

    @Override
    public int getMaxSize() {
        return maxSize;
    }

    @Override
    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
}
