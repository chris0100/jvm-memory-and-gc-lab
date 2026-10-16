# JVM Memory & GC Lab

Reproduce common memory problems on purpose, then learn how to find them.

| Example | Class | Lesson |
|---------|-------|--------|
| Unbounded cache = memory leak | `LeakyCache` | A static `HashMap` that only grows is the most common leak in backend code |
| Bounded LRU cache | `LruCache` | `LinkedHashMap` with `accessOrder=true` + `removeEldestEntry` |
| Memory report | `MemoryReport` | Read heap usage and GC stats from the `MemoryMXBean` / `GarbageCollectorMXBean` |
| Controlled OOM | `OomDemo` | Trigger `OutOfMemoryError` with a small heap and open the heap dump |

## Run

```bash
mvn test

# Reproduce an OOM and get a heap dump (open it with Eclipse MAT or VisualVM):
mvn -q compile
java -Xmx64m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./oom.hprof \
     -cp target/classes dev.chris0100.jvm.OomDemo
```

## Interview notes

**Memory areas**: heap (young: eden + survivors, old), metaspace (class metadata), thread stacks, direct/native memory.

**Kinds of OutOfMemoryError**
- `Java heap space`: too many live objects -> heap dump, look at the dominator tree and GC roots.
- `Metaspace`: too many classes (classloader leak, heavy proxies/reflection).
- `unable to create native thread`: too many threads (unbounded pools).
- Container killed with **exit 137**: the process exceeded the container limit; heap + metaspace + stacks + native must fit. Use `-XX:MaxRAMPercentage=75`.

**Garbage collectors**
- **G1** (default): balanced, region-based, target pause with `-XX:MaxGCPauseMillis`.
- **ZGC** (`-XX:+UseZGC`, generational in Java 21 with `-XX:+ZGenerational`): sub-millisecond pauses, large heaps.
- **Parallel**: max throughput for batch jobs, longer pauses.

**Tools**: `jcmd <pid> GC.heap_info`, `jcmd <pid> GC.heap_dump file.hprof`, `jstat -gcutil <pid> 1s`, GC logs with `-Xlog:gc*`, Eclipse MAT, VisualVM, JFR (`-XX:StartFlightRecording`).
