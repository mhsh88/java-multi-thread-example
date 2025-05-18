package io.github.mhsh.notifyexample;

import java.util.LinkedList;
import java.util.Queue;

/**
 * This class demonstrates issues when trying to implement producer-consumer
 * without proper use of wait() and notify().
 */
public class NotifyExample {

    private final Queue<Integer> buffer = new LinkedList<>();
    private final int BUFFER_SIZE = 5;
    private final int MAX_ITEMS = 20;
    private boolean producerDone = false;

    public void runWithoutWaitNotify() {
        System.out.println("=== Running example WITHOUT proper wait/notify ===");

        // Producer thread - adds items to the buffer
        Thread producer = new Thread(() -> {
            System.out.println("Producer: Starting");
            
            for (int i = 0; i < MAX_ITEMS; i++) {
                // If buffer is full, we need to wait, but we'll use busy waiting
                while (buffer.size() >= BUFFER_SIZE) {
                    Thread.yield(); // Yield execution, but this is inefficient
                }
                
                // Add item to buffer
                synchronized (buffer) {
                    buffer.add(i);
                    System.out.println("Producer: Added item " + i + ", Buffer size: " + buffer.size());
                }
                
                // Simulate varying production speeds
                try {
                    Thread.sleep((int)(Math.random() * 100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
            producerDone = true;
            System.out.println("Producer: Finished producing all items");
        });

        // Consumer thread - removes items from the buffer
        Thread consumer = new Thread(() -> {
            System.out.println("Consumer: Starting");
            int itemsConsumed = 0;
            
            // Continue until we've consumed all items and producer is done
            while (!producerDone || !buffer.isEmpty()) {
                // If buffer is empty, we need to wait, but we'll use busy waiting
                if (buffer.isEmpty()) {
                    Thread.yield(); // Yield execution, but this is inefficient
                    continue;
                }
                
                // Remove item from buffer
                Integer item;
                synchronized (buffer) {
                    item = buffer.poll();
                }
                
                if (item != null) {
                    itemsConsumed++;
                    System.out.println("Consumer: Consumed item " + item + ", Buffer size: " + buffer.size() + 
                                      ", Total consumed: " + itemsConsumed);
                }
                
                // Simulate varying consumption speeds
                try {
                    Thread.sleep((int)(Math.random() * 200));
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
        System.out.println("Main: This approach uses busy waiting (Thread.yield()),");
        System.out.println("Main: which wastes CPU cycles and is inefficient.");
    }
}
