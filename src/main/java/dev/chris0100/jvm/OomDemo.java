package dev.chris0100.jvm;

import java.util.ArrayList;
import java.util.List;

/** Run with a small heap (-Xmx64m -XX:+HeapDumpOnOutOfMemoryError) and analyze the dump. */
public final class OomDemo {

    public static void main(String[] args) {
        List<byte[]> retained = new ArrayList<>(); // GC root keeps everything alive
        int mb = 0;
        while (true) {
            retained.add(new byte[1024 * 1024]);
            mb++;
            if (mb % 10 == 0) {
                System.out.println("retained " + mb + " MB");
            }
        }
    }
}
