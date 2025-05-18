package io.github.mhsh.joinexample;

/**
 * This class demonstrates the use of join() with a timeout parameter.
 * This is useful when you want to wait for threads to complete, but only up to a certain time limit.
 */
public class JoinTimeoutExample {
    
    public void runWithJoinTimeout() {
        System.out.println("=== Running example with join(timeout) ===");
        
        // Create a worker thread that will run for a long time
        Thread longRunningThread = new Thread(() -> {
            System.out.println("Long-running thread: Starting work");
            try {
                // Simulate a long-running task
                for (int i = 0; i < 10; i++) {
                    System.out.println("Long-running thread: Working... " + (i + 1) + "/10");
                    Thread.sleep(500); // Sleep for 500ms in each iteration
                }
            } catch (InterruptedException e) {
                System.out.println("Long-running thread: I was interrupted!");
                return; // Exit the thread if interrupted
            }
            System.out.println("Long-running thread: Work completed");
        });
        
        System.out.println("Main thread: Starting long-running thread");
        longRunningThread.start();
        
        System.out.println("Main thread: Waiting for thread to complete with a timeout of 2 seconds");
        try {
            // Wait for the thread to complete, but only for 2 seconds
            longRunningThread.join(2000); // 2000ms = 2 seconds
            
            if (longRunningThread.isAlive()) {
                System.out.println("Main thread: Thread did not complete within the timeout period");
                System.out.println("Main thread: Interrupting the thread");
                longRunningThread.interrupt(); // Request the thread to stop
            } else {
                System.out.println("Main thread: Thread completed within the timeout period");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Main thread: Continuing execution regardless of whether the thread completed");
        
        // Wait a bit to see if the interrupted thread completes
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Main thread: Program execution completing...");
        if (longRunningThread.isAlive()) {
            System.out.println("Main thread: Note that the long-running thread is still alive");
            System.out.println("Main thread: In a real application, you might need to handle this case");
        }
    }
}
