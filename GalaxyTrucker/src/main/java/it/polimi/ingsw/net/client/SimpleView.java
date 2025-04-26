package it.polimi.ingsw.net.client;

import it.polimi.ingsw.net.events.Event;
import it.polimi.ingsw.net.events.EventType;
import it.polimi.ingsw.net.events.HelloEvent;

import java.util.Scanner;

public class SimpleView implements NetworkListenerClient.EventReceiver {
    private final ClientHandler clientHandler;
    private final Scanner scanner;

    public SimpleView(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void onEventReceived(Event event) {
        System.out.println("[SERVER] Messaggio ricevuto: " + event);
    }

    public void start() {
        System.out.println("SimpleView avviata.");
        while (true) {
            System.out.print("Scrivi un messaggio da inviare al server: ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            Event event = new HelloEvent(input, EventType.HELLO_MESSAGE); // Creiamo un semplice evento di testo
            clientHandler.handleEvent(event);
        }
        System.out.println("Chiusura View.");
    }
}
