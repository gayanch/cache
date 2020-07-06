package com.github.gayanch.cache;

import com.github.gayanch.cache.config.CacheConfig;
import com.github.gayanch.cache.store.FileSystemStorage;
import com.github.gayanch.cache.store.InMemoryStorage;
import com.github.gayanch.cache.store.Storage;
import com.github.gayanch.cache.strategy.StorageStrategy;

import java.util.LinkedList;
import java.util.List;

public class CacheBuilder<K, V> {
    private List<CacheConfig> configList = new LinkedList<>();

    public CacheBuilder<K, V> addCache(CacheConfig config) {
        configList.add(config);
        return this;
    }

    public Cache<K, V> build() {
        Cache<K, V> cache = null;
        for (CacheConfig config: configList) {
            Storage<K, V> storage;
            if (config.getStorageStrategy() == StorageStrategy.FILE_SYSTEM) {
                storage = new FileSystemStorage<>(config.getMaxSize());
            } else {
                storage = new InMemoryStorage<>(config.getMaxSize());
            }

            if (null == cache) {
                cache = new CacheImpl<>(config.getCacheName(), storage, config.getEvictionStrategy());
            } else {
                Cache<K, V> currentCache = cache;
                while (currentCache.getNextLevelCache() != null) {
                    currentCache = currentCache.getNextLevelCache();
                }
                currentCache.appendCache(new CacheImpl<>(config.getCacheName(), storage, config.getEvictionStrategy()));
            }
        }

        return cache;
    }
}
