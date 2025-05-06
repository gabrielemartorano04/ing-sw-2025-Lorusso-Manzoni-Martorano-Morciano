package it.polimi.ingsw.view;

import java.util.Scanner;

public class InputReader {
    private final Scanner scanner;

    public InputReader() {
        this.scanner = new Scanner(System.in);
    }

    public String readLine() {
        System.out.print("Scrivi un comando: ");
        // uses .trim() to avoid problems with spaces before and after the input written by the user
        return scanner.nextLine().trim();
    }
}
