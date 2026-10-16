package dev.chris0100.jvm;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class MemoryTest {

    @Test
    void leakyCacheGrowsWithEveryUniqueKey() {
        LeakyCache.clear();
        IntStream.range(0, 10_000).forEach(i -> LeakyCache.get("user-" + i));
        assertEquals(10_000, LeakyCache.size());
        LeakyCache.clear();
    }

    @Test
    void lruCacheStaysBoundedAndEvictsTheLeastRecentlyUsed() {
        LruCache<String, String> cache = new LruCache<>(3);
        cache.get("a", String::toUpperCase);
        cache.get("b", String::toUpperCase);
        cache.get("c", String::toUpperCase);
        cache.get("a", k -> "should not reload");   // touch "a" -> "b" is now the eldest
        cache.get("d", String::toUpperCase);         // evicts "b"

        assertEquals(3, cache.size());
        assertTrue(cache.contains("a"));
        assertFalse(cache.contains("b"));
        assertEquals("A", cache.get("a", k -> "reloaded"));
    }

    @Test
    void memoryReportShowsHeapAndCollectors() {
        assertTrue(MemoryReport.lines().get(0).startsWith("heap used="));
        assertTrue(MemoryReport.lines().size() >= 2, "at least one garbage collector bean");
    }
}
