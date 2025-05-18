package io.github.mhsh.multithread;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class demonstrates the correct solution for atomic operations using AtomicInteger.
 */
public class AtomicSolution {
    private final AtomicInteger counter = new AtomicInteger(0);
    private final int THREADS_COUNT = 10;
    private final int ITERATIONS = 1000;
    
    public void start() {
        System.out.println("Starting atomic solution example...");
        
        Thread[] threads = new Thread[THREADS_COUNT];
        
        // Create multiple threads that increment the atomic counter
        for (int i = 0; i < THREADS_COUNT; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < ITERATIONS; j++) {
                    // This operation is atomic
                    counter.incrementAndGet();
                }
            });
            threads[i].start();
        }
        
        // Wait for all threads to complete
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        // Expected value: THREADS_COUNT * ITERATIONS
        System.out.println("Final atomic counter value: " + counter.get());
        System.out.println("Expected counter value: " + (THREADS_COUNT * ITERATIONS));
        System.out.println("This demonstrates that AtomicInteger provides both visibility and atomicity");
    }
}
