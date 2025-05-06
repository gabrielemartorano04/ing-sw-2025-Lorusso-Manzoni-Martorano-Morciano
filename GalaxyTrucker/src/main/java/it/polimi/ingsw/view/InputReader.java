package it.polimi.ingsw.view;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class InputReader implements Runnable {
    private final Scanner scanner;
    private final BlockingQueue<String> inputQueue;
    private volatile boolean running;

    public InputReader() {
        this.scanner = new Scanner(System.in);
        this.inputQueue = new LinkedBlockingQueue<>();
        this.running = true;
    }

    public void stop() {
        running = false;
    }

    public BlockingQueue<String> getInputQueue() {
        return inputQueue;
    }

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

    public String nextLineBlocking() throws InterruptedException {
        return inputQueue.take(); // blocca finché non c'è input
    }

    public String nextLineNonBlocking() {
        return inputQueue.poll(); // restituisce null se non c'è niente
    }
}
