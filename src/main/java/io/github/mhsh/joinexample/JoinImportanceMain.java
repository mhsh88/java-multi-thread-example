package io.github.mhsh.joinexample;

/**
 * Main class to demonstrate why the join() method is important in multithreaded applications.
 */
public class JoinImportanceMain {
    
    public static void main(String[] args) {
        System.out.println("\n====== DEMONSTRATING THE IMPORTANCE OF join() IN MULTITHREADING ======\n");
        
        // Explanation of what join() does
        System.out.println("WHAT IS join() AND WHY DO WE NEED IT?\n");
        System.out.println("The join() method in Java allows one thread to wait for the completion of another.");
        System.out.println("When the main thread calls t.join(), it will wait until thread t completes its execution.");
        System.out.println("Without join(), the main thread continues execution independently of other threads.");
        System.out.println("\nCOMPARISON OF BEHAVIOR WITH AND WITHOUT join():\n");
        
        // Run example without join()
        JoinExample example1 = new JoinExample();
        example1.runWithoutJoin();
        
        System.out.println("\n-------------------------------------------------\n");
        
        // Give some time for the first example's threads to finish
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Run example with join()
        JoinSolutionExample example2 = new JoinSolutionExample();
        example2.runWithJoin();
        
        System.out.println("\n-------------------------------------------------\n");
        
        // Run example with join timeout
        JoinTimeoutExample example3 = new JoinTimeoutExample();
        example3.runWithJoinTimeout();
        
        System.out.println("\n-------------------------------------------------\n");
        
        // Summary of benefits of join()
        System.out.println("\nSUMMARY: BENEFITS OF USING join()\n");
        System.out.println("1. Coordination: Ensures that the main thread waits for worker threads to complete.");
        System.out.println("2. Data Integrity: Prevents accessing results before they're fully computed.");
        System.out.println("3. Resource Management: Ensures all threads finish before the program exits.");
        System.out.println("4. Sequential Execution: Allows for predictable behavior in operations that must follow thread completion.");
        System.out.println("5. Timeout Control: join(milliseconds) allows waiting for a maximum time period.");
        
        System.out.println("\n====== END OF DEMONSTRATION ======\n");
    }
}
