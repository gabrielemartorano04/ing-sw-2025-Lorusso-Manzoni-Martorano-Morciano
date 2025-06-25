package it.polimi.ingsw.view;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The InputReader class handles user inputs from the console in a separate thread.
 * It continuously reads input lines, processes them, and stores them in a thread-safe
 * queue for other parts of the application to consume
 */
public class InputReader implements Runnable {

    /**
     * A Scanner instance used to read input from the standard input stream
     */
    private final Scanner scanner;

    /**
     * A thread-safe queue used to store input lines read from the console
     */
    private final BlockingQueue<String> inputQueue;

    /**
     * Indicates whether the InputReader thread should continue running
     */
    private volatile boolean running;

    /**
     * Constructs an instance of the InputReader class
     */
    public InputReader() {
        this.scanner = new Scanner(System.in);
        this.inputQueue = new LinkedBlockingQueue<>();
        this.running = true;
    }

    /**
     * Stops the execution of the InputReader thread by setting the running flag to false
     */
    public void stop() {
        running = false;
    }

    /**
     * Executes the main logic for the InputReader thread. This method continuously reads lines
     * of input from the console using the Scanner instance as long as the `running` flag is true
     */
    @Override
    public void run() {
        while (running) {
            if (scanner.hasNextLine()) {
                String input = scanner.nextLine().trim();
                if (!input.isEmpty()) {
                    inputQueue.offer(input);
                }
            }
        }
    }

    /**
     * Retrieves and returns the next line of input from the queue, blocking if the queue is empty.
     * This method will wait until an input is available to return.
     * @return the next line of input as a String, taken from the input queue
     * @throws InterruptedException if the current thread is interrupted while waiting for input
     */
    public String nextLineBlocking() throws InterruptedException {
        return inputQueue.take();
    }
}
