package io.github.mhsh.joinexample;

/**
 * This class demonstrates the issues that can occur 
 * when not using the join() method in multithreaded applications.
 */
public class JoinExample {
    
    public void runWithoutJoin() {
        System.out.println("=== Running example WITHOUT join() ===");
        
        final int NUM_THREADS = 5;
        final int WORK_ITEMS = 10;
        
        Thread[] workers = new Thread[NUM_THREADS];
        CounterHolder counterHolder = new CounterHolder();
        
        System.out.println("Main thread: Starting worker threads...");
        
        // Create and start worker threads
        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i;
            workers[i] = new Thread(() -> {
                System.out.println("Thread-" + threadId + ": Starting work");
                
                // Simulate some work
                for (int j = 0; j < WORK_ITEMS; j++) {
                    try {
                        Thread.sleep((int)(Math.random() * 10)); // Random sleep to simulate different work speeds
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    counterHolder.increment();
                    System.out.println("Thread-" + threadId + ": Completed item " + j);
                }
                
                System.out.println("Thread-" + threadId + ": Work completed");
            });
            
            workers[i].start();
        }
        
        // Main thread continues immediately without waiting for worker threads
        System.out.println("Main thread: All threads have been started");
        System.out.println("Main thread: Proceeding without waiting for workers to complete");
        
        // This will likely print an incomplete count because threads might still be running
        System.out.println("Main thread: Final counter value: " + counterHolder.getCount());
        System.out.println("Main thread: Expected counter value: " + (NUM_THREADS * WORK_ITEMS));
        System.out.println("Main thread: Program execution completing...");
    }
    
    // Simple class to hold the counter
    private static class CounterHolder {
        private int count = 0;
        
        public void increment() {
            count++;
        }
        
        public int getCount() {
            return count;
        }
    }
}
