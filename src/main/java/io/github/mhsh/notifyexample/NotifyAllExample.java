package io.github.mhsh.notifyexample;

import java.util.LinkedList;
import java.util.Queue;

/**
 * This class demonstrates the difference between notify() and notifyAll()
 * with multiple consumer threads.
 */
public class NotifyAllExample {

    private final Queue<Integer> buffer = new LinkedList<>();
    private final int BUFFER_SIZE = 5;
    private final int MAX_ITEMS = 30;
    private boolean producerDone = false;
    private int totalConsumed = 0;

    public void runWithMultipleConsumers() {
        System.out.println("=== Running example with notifyAll() and multiple consumers ===");

        final int CONSUMER_COUNT = 3;
        Thread[] consumers = new Thread[CONSUMER_COUNT];
        
        // Producer thread - adds items to the buffer and uses notifyAll()
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
                    
                    // Wake up ALL waiting consumers
                    buffer.notifyAll();
                }
                
                // Simulate varying production speeds
                try {
                    Thread.sleep((int)(Math.random() * 50));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
            // Signal that production is complete
            synchronized (buffer) {
                producerDone = true;
                buffer.notifyAll(); // Wake up all waiting consumers
                System.out.println("Producer: Finished producing all items");
            }
        });

        // Create multiple consumer threads
        for (int c = 0; c < CONSUMER_COUNT; c++) {
            final int consumerId = c;
            consumers[c] = new Thread(() -> {
                System.out.println("Consumer-" + consumerId + ": Starting");
                int itemsConsumed = 0;
                
                while (!producerDone || !buffer.isEmpty()) {
                    Integer item = null;
                    
                    synchronized (buffer) {
                        // Wait if buffer is empty and producer isn't done
                        while (buffer.isEmpty() && !producerDone) {
                            try {
                                System.out.println("Consumer-" + consumerId + ": Buffer empty, waiting...");
                                buffer.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        
                        // If there are items in the buffer, consume one
                        if (!buffer.isEmpty()) {
                            item = buffer.poll();
                            totalConsumed++;
                            
                            // Notify producer that space is available
                            buffer.notify(); // We could use notifyAll() here too
                        }
                    }
                    
                    // Process the item outside of the synchronized block
                    if (item != null) {
                        itemsConsumed++;
                        System.out.println("Consumer-" + consumerId + ": Consumed item " + item + 
                                          ", Items consumed by this consumer: " + itemsConsumed);
                        
                        // Simulate varying consumption speeds
                        try {
                            Thread.sleep((int)(Math.random() * 200));
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

        // Wait for all threads to finish
        try {
            producer.join();
            for (Thread consumer : consumers) {
                consumer.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Main: All threads have finished");
        System.out.println("Main: Total items produced: " + MAX_ITEMS);
        System.out.println("Main: Total items consumed: " + totalConsumed);
        System.out.println("Main: notifyAll() ensures all waiting threads get a chance to check their conditions");
    }
}
