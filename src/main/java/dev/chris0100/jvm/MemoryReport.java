package dev.chris0100.jvm;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.List;

/** What a /health or metrics endpoint can expose about the JVM (Micrometer does this for you). */
public final class MemoryReport {

    private MemoryReport() {
    }

    public static List<String> lines() {
        List<String> out = new ArrayList<>();
        MemoryUsage heap = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        out.add(String.format("heap used=%d MB committed=%d MB max=%d MB",
                heap.getUsed() >> 20, heap.getCommitted() >> 20, heap.getMax() >> 20));
        for (GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans()) {
            out.add(String.format("gc %s collections=%d time=%d ms", gc.getName(), gc.getCollectionCount(), gc.getCollectionTime()));
        }
        return out;
    }

    public static void main(String[] args) {
        lines().forEach(System.out::println);
    }
}
