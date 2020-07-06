package com.github.gayanch.cache.store;

import com.github.gayanch.cache.exception.StorageFullException;

/**
 * Storage interface defines the contract for various storage backends to be used for cache storage
 * @param <K> The type of CacheKey
 * @param <V> The type of CacheValue
 */
public interface Storage<K, V> {
    /**
     * Stores the given key, value combination in storage backend
     * @param key The CacheKey
     * @param value The CacheValue
     * @throws StorageFullException When the storage backend exceeded the configured max storage size
     */
    void store(K key, V value) throws StorageFullException;

    /**
     * Reads and returns the CacheValue for given key from storage backend
     * @param key The CacheKey
     * @return The CacheValue if found. Null otherwise.
     */
    V read(K key);

    /**
     * Removes the CacheEntry of given key
     * @param key The CacheKey
     * @return The CacheValue of given key if found in the storage backend. Null otherwise.
     */
    V remove(K key);

    /**
     * Get the current capacity of the storage backend
     * @return The current capacity
     */
    int size();

    /**
     * Get the configured max size of the storage backend
     * @return The maximum size of the storage backend
     */
    int getMaxSize();

    /**
     * Set maximum size of the storage backend
     * @param maxSize The maximum size
     */
    void setMaxSize(int maxSize);
}
