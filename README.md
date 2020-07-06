# Java Cache Implementation
Multi-level cache implementation in Java

## System Requirements
* OpenJDK 11
* maven 3+

## Test and Build
`mvn clean install`

## How to Use
## Initialize a cache
Initialize a multi-level cache using CacheBuilder
```java
Cache<KeyType, ValueType> cache = new CacheBuilder<KeyType, ValueType>()
     .addCache(new CacheConfig(cacheName, maxSize, EvictionStrategy, StorageStrategy))
     .addCache(new CacheConfig(cacheName, maxSize, EvictionStrategy, StorageStrategy))
     .build();
```
Example
```java
Cache<Integer, String> cache = new CacheBuilder<Integer, String>()
    .addCache(new CacheConfig("Level1", 2, EvictionStrategy.LFU, StorageStrategy.IN_MEMORY))
    .addCache(new CacheConfig("Level2", 3, EvictionStrategy.LRU, StorageStrategy.FILE_SYSTEM))
    .build();
```

## Eviction Strategies
Eviction Strategy defines which item to evict from cache when cache is full. 
Eviction strategies are defined in `com.github.gayanch.cache.strategy.EvictionStrategy` enum. Following strategies are supported by default,
### LRU (Least Recently Used Strategy)
If this strategy is used when building the cache, least recently used cache entry will be evicted in a situation where cache is full.
### LFU (Least Frequently Used Strategy)
If this strategy is used when building the cache, least frequently accessed cache entry will be evicted in a situation where cache is full.

## Storage Strategies
Storage Strategy defines the storage backend to be used by caches. Storage strategies are defines in
`com.github.gayanch.cache.strategy.StorageStrategy` enum. Following strategies are supported by default,
### IN_MEMORY
When cache is created with this strategy, cache entries will be stored in an in-memory structure.
### FILE_SYSTEM
When cache is created with this strategy, cache entries will be stored in the file system.

## Cache Propagation Behavior
* All `Cache#put` operations will store cache entries in First Level Cache until it becomes full
* If the First Level Cache is full,
    * A cache entry will be evicted from first level cache based on the defined eviction strategy
    * New cache entry will be inserted into first level cache
    * Evicted cache entry from first level cache will be inserted into next level cache
* This behavior is continued until the Second Level cache becomes full
* When the second level cache becomes full, same behavior as when the first level cache is full will be followed. 
If there is no more cache levels found, evicted value from second level cache will be discared.