package io.github.mhsh.notifyexample;

import java.util.LinkedList;
import java.util.Queue;

/**
 * This class demonstrates the proper use of wait() and notify() methods
 * to implement efficient producer-consumer coordination.
 */
public class NotifySolutionExample {

    private final Queue<Integer> buffer = new LinkedList<>();
    private final int BUFFER_SIZE = 5;
    private final int MAX_ITEMS = 20;
    private boolean producerDone = false;

    public void runWithWaitNotify() {
        System.out.println("=== Running example WITH proper wait/notify ===");

        // Producer thread - adds items to the buffer
        Thread producer = new Thread(() -> {
            System.out.println("Producer: Starting");
            
            for (int i = 0; i < MAX_ITEMS; i++) {
                synchronized (buffer) {
                    // Wait if buffer is full
                    while (buffer.size() >= BUFFER_SIZE) {
                        try {
                            System.out.println("Producer: Buffer full, waiting...");
                            buffer.wait(); // Releases lock and waits to be notified
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    
                    // Add item to buffer
                    buffer.add(i);
                    System.out.println("Producer: Added item " + i + ", Buffer size: " + buffer.size());
                    
                    // Notify consumer that an item is available
                    buffer.notify();
                }
                
                // Simulate varying production speeds
                try {
                    Thread.sleep((int)(Math.random() * 100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
            // Signal that production is complete
            synchronized (buffer) {
                producerDone = true;
                buffer.notify(); // Ensure consumer is notified even if buffer is empty
                System.out.println("Producer: Finished producing all items");
            }
        });

        // Consumer thread - removes items from the buffer
        Thread consumer = new Thread(() -> {
            System.out.println("Consumer: Starting");
            int itemsConsumed = 0;
            
            // Continue until we've consumed all items and producer is done
            while (!producerDone || !buffer.isEmpty()) {
                Integer item = null;
                
                synchronized (buffer) {
                    // Wait if buffer is empty and producer isn't done
                    while (buffer.isEmpty() && !producerDone) {
                        try {
                            System.out.println("Consumer: Buffer empty, waiting...");
                            buffer.wait(); // Releases lock and waits to be notified
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    
                    // If there are items in the buffer, consume one
                    if (!buffer.isEmpty()) {
                        item = buffer.poll();
                        
                        // Notify producer that space is available
                        buffer.notify();
                    }
                }
                
                // Process the item outside of the synchronized block
                if (item != null) {
                    itemsConsumed++;
                    System.out.println("Consumer: Consumed item " + item + ", Buffer size: " + buffer.size() + 
                                      ", Total consumed: " + itemsConsumed);
                    
                    // Simulate varying consumption speeds
                    try {
                        Thread.sleep((int)(Math.random() * 200));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            
            System.out.println("Consumer: Finished consuming all items. Total: " + itemsConsumed);
        });

        // Start threads
        producer.start();
        consumer.start();

        // Wait for both threads to finish
        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Main: Both producer and consumer have finished");
        System.out.println("Main: This approach uses wait/notify for efficient thread coordination");
        System.out.println("Main: Threads don't waste CPU cycles while waiting");
    }
}
