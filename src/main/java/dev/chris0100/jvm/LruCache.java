package dev.chris0100.jvm;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

/** Bounded cache that evicts the least recently used entry. Thread-safe via a synchronized wrapper. */
public final class LruCache<K, V> {

    private final Map<K, V> map;

    public LruCache(int maxEntries) {
        this.map = Collections.synchronizedMap(new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > maxEntries;
            }
        });
    }

    public V get(K key, Function<K, V> loader) {
        synchronized (map) {
            V value = map.get(key);
            if (value == null) {
                value = loader.apply(key);
                map.put(key, value);
            }
            return value;
        }
    }

    public boolean contains(K key) {
        return map.containsKey(key);
    }

    public int size() {
        return map.size();
    }
}
