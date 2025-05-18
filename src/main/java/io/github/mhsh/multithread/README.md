# Java Volatile Keyword Example

*This is a submodule of the [Java Multithreading Self-Improvement Project](../../../README.md).*

This project demonstrates the features, advantages, and limitations of the `volatile` keyword in Java multithreading.

## Examples Included

1. **VolatileExample**: Shows visibility issues that can occur without the `volatile` keyword.
2. **VolatileDemo**: Demonstrates how the `volatile` keyword solves visibility problems.
3. **VolatileLimitations**: Illustrates what `volatile` cannot do (non-atomic compound operations).
4. **AtomicSolution**: Shows the proper solution for atomic operations using `AtomicInteger`.

## Key Features of Volatile

1. **Memory Visibility**: Guarantees that changes made by one thread to a `volatile` variable are immediately visible to all other threads.
2. **Prevents Memory Reordering**: Provides a "happens-before" relationship, which prevents instruction reordering by the compiler or CPU.

## Limitations of Volatile

1. **No Atomicity for Compound Operations**: While individual reads and writes to `volatile` variables are atomic, compound operations (like increment `i++`) are not.
2. **No Mutual Exclusion**: Does not provide mutual exclusion like `synchronized` blocks do.

## When to Use Volatile

* Use `volatile` when you need visibility guarantees without the overhead of synchronization.
* Perfect for flag variables that control thread execution.
* Good for simple state variables that are written only once but read by multiple threads.

## When Not to Use Volatile

* When you need atomicity for compound operations (use `AtomicInteger`, etc. instead).
* When you need mutual exclusion (use `synchronized` or locks instead).
* For complex state management requiring several variables to be updated together.

## Running the Examples

Execute the `Main` class to see all examples in action:
