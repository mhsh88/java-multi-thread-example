# Wait, Notify, and NotifyAll in Java Multithreading

*This is a submodule of the [Java Multithreading Self-Improvement Project](../../../../../../../README.md).*

This project demonstrates the importance of `wait()`, `notify()`, and `notifyAll()` methods in Java multithreading for inter-thread communication.

## What are wait(), notify(), and notifyAll()?

These methods, part of the `Object` class in Java, provide a mechanism for threads to communicate:

- **wait()**: Causes the current thread to release locks and wait until another thread notifies it
- **notify()**: Wakes up a single thread that is waiting on the object
- **notifyAll()**: Wakes up all threads that are waiting on the object

## Examples Included

### 1. Without wait()/notify()
Demonstrates issues when using busy waiting (Thread.yield()) for thread coordination:
- High CPU usage from constant checking
- Inefficient resource utilization
- No guaranteed responsiveness

### 2. With wait()/notify()
Shows the proper way to use wait() and notify() in a producer-consumer scenario:
- Efficient thread coordination
- Threads sleep when they can't proceed
- Better CPU utilization

### 3. With notifyAll() and Multiple Consumers
Demonstrates when and why notifyAll() is needed:
- Wakes up all waiting threads
- Prevents potential deadlocks
- Useful when threads are waiting on different conditions

### 4. Deadlock Scenario
Shows how incorrect use of notify() can lead to deadlocks:
- The wrong thread might be awakened
- Some threads might never be notified
- System gets stuck indefinitely

## Key Concepts Illustrated

### 1. Producer-Consumer Pattern
- Shared buffer with bounded capacity
- Producer adds items, consumer removes them
- Synchronization prevents race conditions

### 2. Thread Synchronization
- Synchronized blocks protect shared resources
- Object monitors control thread access
- Wait sets hold threads until notification

### 3. Avoiding Common Pitfalls
- Always call wait() in a loop (for spurious wakeups)
- Always use wait()/notify() within synchronized blocks
- Use notifyAll() when multiple threads may be waiting

## Rules for Using wait()/notify()

1. Always call wait(), notify(), and notifyAll() from synchronized blocks
2. Always check wait conditions in a loop
3. Prefer notifyAll() over notify() when multiple threads might be waiting
4. Consider modern alternatives from java.util.concurrent

## How to Run

Execute the `NotifyImportanceMain` class to see all examples in action:

```
