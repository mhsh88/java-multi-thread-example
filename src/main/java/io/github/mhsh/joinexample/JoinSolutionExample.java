package io.github.mhsh.joinexample;

/**
 * This class demonstrates the proper use of the join() method 
 * to ensure that the main thread waits for all worker threads to complete.
 */
public class JoinSolutionExample {
    
    public void runWithJoin() {
        System.out.println("=== Running example WITH join() ===");
        
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
        
        System.out.println("Main thread: All threads have been started");
        System.out.println("Main thread: Waiting for all worker threads to complete using join()...");
        
        // Wait for all threads to complete using join()
        for (Thread worker : workers) {
            try {
                worker.join(); // This causes the main thread to wait until this worker thread completes
                System.out.println("Main thread: A worker thread has completed");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        // Now that all threads have completed, the counter should have the expected value
        System.out.println("Main thread: All worker threads have completed");
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
