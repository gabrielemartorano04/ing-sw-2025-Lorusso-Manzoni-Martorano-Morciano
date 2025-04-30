package it.polimi.ingsw.view;

import it.polimi.ingsw.net.client.ClientHandler;
import it.polimi.ingsw.net.client.NetworkListenerClient;
import it.polimi.ingsw.net.events.Event;

public class SimpleView implements NetworkListenerClient.EventReceiver {
    private final ClientHandler clientHandler;
    private final InputReader inputReader;
    private final InputParser inputParser;

    public SimpleView(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
        this.inputReader = new InputReader();
        this.inputParser = new InputParser();
    }

    @Override
    public void onEventReceived(Event event) {
        System.out.println("[SERVER] Messaggio ricevuto: " + event);
    }

    public void start() {
        System.out.println("SimpleView avviata.");
        while (true) {
            String input = inputReader.readLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            Event event = inputParser.parse(input);
            if (event != null) {
                clientHandler.handleEvent(event);
            }
        }
        System.out.println("Chiusura View.");
    }
}

