package it.polimi.ingsw.net.client;

import java.util.Scanner;

public class InputReader {
    private final Scanner scanner;

    public InputReader() {
        this.scanner = new Scanner(System.in);
    }

    public String readLine() {
        System.out.print("Scrivi un comando (login <username> / hello <messaggio> / exit): ");
        // uses .trim() to avoid problems with spaces before and after the input written by the user
        return scanner.nextLine().trim();
    }
}
