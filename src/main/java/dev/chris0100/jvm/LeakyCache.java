package dev.chris0100.jvm;

import java.util.HashMap;
import java.util.Map;

/** Anti-pattern: a static map with no eviction. Every unique key stays in memory forever. */
public final class LeakyCache {

    private static final Map<String, byte[]> CACHE = new HashMap<>();

    private LeakyCache() {
    }

    public static byte[] get(String key) {
        return CACHE.computeIfAbsent(key, k -> new byte[1024]);
    }

    public static int size() {
        return CACHE.size();
    }

    static void clear() {
        CACHE.clear();
    }
}
