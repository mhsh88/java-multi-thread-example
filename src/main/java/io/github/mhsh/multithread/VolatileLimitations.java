package io.github.mhsh.multithread;

/**
 * This class demonstrates the limitations of volatile.
 * While volatile guarantees visibility, it does not provide atomicity for compound operations.
 */
public class VolatileLimitations {
    private volatile int counter = 0; // Volatile counter
    private final int THREADS_COUNT = 10;
    private final int ITERATIONS = 1000;
    
    public void start() {
        System.out.println("Starting volatile limitations example...");
        System.out.println("Demonstrating that volatile does not guarantee atomicity for compound operations");
        
        Thread[] threads = new Thread[THREADS_COUNT];
        
        // Create multiple threads that increment the counter
        for (int i = 0; i < THREADS_COUNT; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < ITERATIONS; j++) {
                    // This operation (read-modify-write) is not atomic even with volatile
                    counter++; // This is actually counter = counter + 1, which is not atomic
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
        
        // Expected value if operations were atomic: THREADS_COUNT * ITERATIONS
        System.out.println("Final counter value: " + counter);
        System.out.println("Expected counter value: " + (THREADS_COUNT * ITERATIONS));
        System.out.println("Difference (lost updates): " + ((THREADS_COUNT * ITERATIONS) - counter));
        System.out.println("This demonstrates that volatile ensures visibility but not atomicity");
        System.out.println("For atomic compound operations, use AtomicInteger or synchronized blocks");
    }
}
