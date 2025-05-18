package io.github.mhsh.multithread;

/**
 * This class demonstrates the visibility issues that can occur without the volatile keyword.
 * When a thread keeps running endlessly because it doesn't see the updated value of the 'running' flag.
 */
public class VolatileExample {
    private boolean running = true; // Non-volatile flag
    
    public void start() {
        System.out.println("Starting non-volatile example...");
        
        // Thread that will check the running flag
        Thread workerThread = new Thread(() -> {
            long counter = 0;
            System.out.println("Worker thread started");
            
            // This loop might run forever because 'running' might be cached in CPU registers
            // or thread-local cache and never see the updated value
            while (running) {
                counter++;
                // Worker thread does some work
                if (counter % 5_000_000_000L == 0) {
                    System.out.println("Still running... count: " + counter / 5_000_000_000L);
                }
            }
            
            System.out.println("Worker thread terminated after: " + counter + " iterations");
        });
        
        workerThread.start();
        
        // Give the worker thread some time to start
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Attempt to stop the worker thread
        running = false;
        System.out.println("Set running to false, waiting for worker to notice...");
        
        // Wait for the worker thread to finish
        try {
            workerThread.join(5000); // Wait up to 5 seconds
            if (workerThread.isAlive()) {
                System.out.println("Worker thread did NOT see the updated value and is still running!");
                // We don't interrupt for demonstration purposes
            } else {
                System.out.println("Worker thread terminated properly");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
