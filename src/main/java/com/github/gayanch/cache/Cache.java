package com.github.gayanch.cache;

/**
 * Cache interface defines the contract for Cache implementations
 * @param <K> The type of CacheKey
 * @param <V> The type of CacheValue
 */
public interface Cache<K, V> {
   /**
    * Get CacheValue of given key
    * @param key The CacheKey
    * @return The CacheValue of given key. Null if not found.
    */
   V get(K key);

   /**
    * Add CacheValue to Cache store with given key
    * @param key The CacheKey
    * @param value The CacheValue
    */
   void put(K key, V value);

   /**
    * Removes the CacheValue of given CahceKey
    * @param key The CacheKey
    * @return The removed CacheValue. Null if the key is not found in the Cache Store
    */
   V remove(K key);

   /**
    * Set max size of the cache store
    * @param maxSize The max size
    */
   void setMaxSize(int maxSize);

   /**
    * Get the max configured size of the cache store
    * @return The max configured size of the cache store
    */
   long getMaxSize();

   /**
    * The cache name for easy identification
    * @return The cache name
    */
   String getName();

   /**
    * Appends an another instance of {@link Cache} in multi-level cache scenario
    * @param cache
    */
   void appendCache(Cache<K, V> cache);

   /**
    * Returns the next level cache in a multi-level cache scenario.
    * @return The next level cache. Null when the max level is reached.
    */
   Cache<K, V> getNextLevelCache();
}
