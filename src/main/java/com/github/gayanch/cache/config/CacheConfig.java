package com.github.gayanch.cache.config;

import com.github.gayanch.cache.strategy.EvictionStrategy;
import com.github.gayanch.cache.strategy.StorageStrategy;

public class CacheConfig {
    private String cacheName;
    private int maxSize;
    private EvictionStrategy evictionStrategy;
    private StorageStrategy storageStrategy;

    public CacheConfig(String cacheName, int maxSize, EvictionStrategy evictionStrategy, StorageStrategy storageStrategy) {
        this.cacheName = cacheName;
        this.maxSize = maxSize;
        this.evictionStrategy = evictionStrategy;
        this.storageStrategy = storageStrategy;
    }

    public StorageStrategy getStorageStrategy() {
        return storageStrategy;
    }

    public void setStorageStrategy(StorageStrategy storageStrategy) {
        this.storageStrategy = storageStrategy;
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public EvictionStrategy getEvictionStrategy() {
        return evictionStrategy;
    }

    public void setEvictionStrategy(EvictionStrategy evictionStrategy) {
        this.evictionStrategy = evictionStrategy;
    }
}
