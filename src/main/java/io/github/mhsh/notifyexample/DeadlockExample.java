package io.github.mhsh.notifyexample;

import java.util.LinkedList;
import java.util.Queue;

/**
 * This class demonstrates how incorrect use of notify() can lead to deadlocks
 * in a producer-consumer scenario with multiple threads.
 */
public class DeadlockExample {

    private final Queue<Integer> buffer = new LinkedList<>();
    private final int BUFFER_SIZE = 2; // Small buffer size to make deadlock more likely
    private final int MAX_ITEMS = 10; 
    private boolean producerDone = false;

    public void runDeadlockScenario() {
        System.out.println("=== Running deadlock scenario with incorrect notify() usage ===");
        System.out.println("Note: This example may hang due to deadlock. If it does, that's the point!");

        final int CONSUMER_COUNT = 3;
        Thread[] consumers = new Thread[CONSUMER_COUNT];
        
        // Producer thread - adds items to the buffer
        Thread producer = new Thread(() -> {
            System.out.println("Producer: Starting");
            
            for (int i = 0; i < MAX_ITEMS; i++) {
                synchronized (buffer) {
                    // Wait if buffer is full
                    while (buffer.size() >= BUFFER_SIZE) {
                        try {
                            System.out.println("Producer: Buffer full, waiting...");
                            buffer.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    
                    // Add item to buffer
                    buffer.add(i);
                    System.out.println("Producer: Added item " + i + ", Buffer size: " + buffer.size());
                    
                    // Wake up only ONE waiting consumer
                    // This can lead to deadlock if the wrong consumer is awakened
                    buffer.notify(); 
                }
                
                try {
                    Thread.sleep(100); 
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
            // Signal that production is complete
            synchronized (buffer) {
                producerDone = true;
                // Using notify() instead of notifyAll() here might leave some consumers waiting forever
                buffer.notify(); 
                System.out.println("Producer: Finished producing all items");
            }
        });

        // Create multiple consumer threads with different consumption conditions
        for (int c = 0; c < CONSUMER_COUNT; c++) {
            final int consumerId = c;
            final int waitThreshold = consumerId; // Different thresholds to demonstrate the issue
            
            consumers[c] = new Thread(() -> {
                System.out.println("Consumer-" + consumerId + ": Starting (waits for at least " + waitThreshold + " items)");
                int itemsConsumed = 0;
                
                while (!producerDone || !buffer.isEmpty()) {
                    Integer item = null;
                    
                    synchronized (buffer) {
                        // This consumer only processes items if there are enough in the buffer
                        // or if the producer is done
                        while ((buffer.size() <= waitThreshold) && !producerDone) {
                            try {
                                System.out.println("Consumer-" + consumerId + ": Not enough items, waiting...");
                                buffer.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        
                        // If there are items in the buffer, consume one
                        if (!buffer.isEmpty()) {
                            item = buffer.poll();
                            
                            // Notify a single waiting thread
                            buffer.notify(); // This might wake up another consumer instead of the producer!
                        }
                    }
                    
                    // Process the item outside of the synchronized block
                    if (item != null) {
                        itemsConsumed++;
                        System.out.println("Consumer-" + consumerId + ": Consumed item " + item + 
                                          ", Items consumed: " + itemsConsumed);
                        
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                
                System.out.println("Consumer-" + consumerId + ": Finished. Items consumed: " + itemsConsumed);
            });
            
            consumers[c].start();
        }

        // Start producer thread
        producer.start();

        // Wait for all threads to finish (with timeout to avoid hanging the demo)
        try {
            producer.join(10000); // Wait up to 10 seconds
            for (Thread consumer : consumers) {
                consumer.join(2000); // Wait up to 2 seconds
            }
            
            // Check if any threads are still running (potential deadlock)
            boolean deadlocked = false;
            if (producer.isAlive()) {
                System.out.println("WARNING: Producer is still running after timeout (potential deadlock)");
                deadlocked = true;
            }
            
            for (int i = 0; i < CONSUMER_COUNT; i++) {
                if (consumers[i].isAlive()) {
                    System.out.println("WARNING: Consumer-" + i + " is still running after timeout (potential deadlock)");
                    deadlocked = true;
                }
            }
            
            if (deadlocked) {
                System.out.println("\nDEADLOCK DETECTED! This demonstrates the danger of using notify() with multiple threads.");
                System.out.println("The solution is to use notifyAll() when multiple threads may be waiting for different conditions.");
            } else {
                System.out.println("All threads completed successfully (no deadlock this time).");
            }
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
