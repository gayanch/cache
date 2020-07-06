package com.github.gayanch.cache.store;

import com.github.gayanch.cache.exception.StorageFullException;
import com.github.gayanch.cache.util.FileSystemUtils;

import java.util.UUID;

/**
 * Storage backend implementation for file system base cache
 * Each instance of FileSystemStorage gets its own directory defined as STORAGE_PREFIX to avoid collisions between
 * different cache levels
 * @param <K> The type of CacheKey
 * @param <V> The type of CacheValue
 */
public class FileSystemStorage<K, V> implements Storage<K, V> {
    private int maxSize;
    private final String STORAGE_PREFIX;

    public FileSystemStorage(int maxSize) {
        this.maxSize = maxSize;
        STORAGE_PREFIX = UUID.randomUUID().toString();
    }

    @Override
    public void store(K key, V value) throws StorageFullException {
        System.out.println("Strore FS: (" + key + "->" + value + ")" );
        if (size() >= maxSize) {
            throw new StorageFullException("Storage is full");
        }
        FileSystemUtils.saveToFile(STORAGE_PREFIX, String.valueOf(key.hashCode()), value);
    }

    /**
     * {@inheritDoc}
     * We know the return type of readFile operation to be V. So no need invoke instance of operation here.
     */
    @Override
    public V read(K key) {
        return (V) FileSystemUtils.readFile(STORAGE_PREFIX, String.valueOf(key.hashCode()));
    }

    /**
     * {@inheritDoc}
     * We know the return type of removeFile operation to be V. So no need invoke instance of operation here.
     */
    @Override
    public V remove(K key) {
        return (V) FileSystemUtils.removeFile(STORAGE_PREFIX, String.valueOf(key.hashCode()));
    }

    @Override
    public int size() {
        return FileSystemUtils.getFileCountForPrefix(STORAGE_PREFIX);
    }

    @Override
    public int getMaxSize() {
        return this.maxSize;
    }

    @Override
    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
}
