package io.github.mhsh.multithread;

/**
 * This class demonstrates how the volatile keyword ensures visibility of changes across threads.
 * Changes to the volatile variable are immediately visible to all threads.
 */
public class VolatileDemo {
    private volatile boolean running = true; // Volatile flag
    
    public void start() {
        System.out.println("Starting volatile example...");
        
        // Thread that will check the running flag
        Thread workerThread = new Thread(() -> {
            long counter = 0;
            System.out.println("Volatile worker thread started");
            
            // This loop will terminate once the main thread changes the 'running' value
            // because volatile guarantees that changes are visible to all threads
            while (running) {
                counter++;
                // Worker thread does some work
                if (counter % 5_000_000_000L == 0) {
                    System.out.println("Still running with volatile... count: " + counter / 5_000_000_000L);
                }
            }
            
            System.out.println("Volatile worker thread terminated after: " + counter + " iterations");
        });
        
        workerThread.start();
        
        // Give the worker thread some time to start
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Stop the worker thread
        running = false;
        System.out.println("Set volatile running to false, worker should notice immediately");
        
        // Wait for the worker thread to finish
        try {
            workerThread.join(3000); // Wait up to 3 seconds
            if (workerThread.isAlive()) {
                System.out.println("Unexpected: Volatile worker thread is still running!");
            } else {
                System.out.println("Volatile worker thread terminated as expected");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
