package io.github.mhsh.multithread;

/**
 * Main class to run all volatile examples and demonstrate their behavior.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Java Volatile Keyword Demonstration ===");
        System.out.println("This program demonstrates the features and limitations of the volatile keyword in Java");
        System.out.println();
        
        // Example 1: Demonstrating visibility issues without volatile
        System.out.println("===============================================");
        System.out.println("EXAMPLE 1: Non-Volatile Variable - Visibility Issues");
        System.out.println("===============================================");
        VolatileExample nonVolatileExample = new VolatileExample();
        nonVolatileExample.start();
        System.out.println();
        
        // Example 2: Demonstrating how volatile solves visibility issues
        System.out.println("===============================================");
        System.out.println("EXAMPLE 2: Volatile Variable - Solving Visibility Issues");
        System.out.println("===============================================");
        VolatileDemo volatileDemo = new VolatileDemo();
        volatileDemo.start();
        System.out.println();
        
        // Example 3: Demonstrating limitations of volatile (non-atomic operations)
        System.out.println("===============================================");
        System.out.println("EXAMPLE 3: Volatile Limitations - Non-Atomic Operations");
        System.out.println("===============================================");
        VolatileLimitations volatileLimitations = new VolatileLimitations();
        volatileLimitations.start();
        System.out.println();
        
        // Example 4: Demonstrating proper solution for atomic operations
        System.out.println("===============================================");
        System.out.println("EXAMPLE 4: Atomic Solution - Proper Way to Handle Atomic Operations");
        System.out.println("===============================================");
        AtomicSolution atomicSolution = new AtomicSolution();
        atomicSolution.start();
        
        System.out.println();
        System.out.println("=== Volatile Keyword Summary ===");
        System.out.println("1. Volatile guarantees VISIBILITY of changes across threads");
        System.out.println("2. Volatile does NOT guarantee ATOMICITY for compound operations");
        System.out.println("3. For atomic operations, use AtomicInteger, AtomicLong, etc.");
        System.out.println("4. Volatile prevents instruction reordering (memory barrier)");
    }
}
