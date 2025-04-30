package it.polimi.ingsw.net.client;

import it.polimi.ingsw.net.events.Event;
import it.polimi.ingsw.view.SimpleView;

import java.io.IOException;

/**
 * Main client application
 */
public class MainClient {

    // Parametri di connessione del server
    private static final String SERVER_ADDRESS = "127.0.0.1"; // Indirizzo server (localhost)
    private static final int SERVER_PORT = 12345; // Porta server

    public static void main(String[] args) {
        // Crea il NetworkClient e si connette al server
        NetworkClient networkClient = new NetworkClient(SERVER_ADDRESS, SERVER_PORT);
        try {
            networkClient.connect(); // Connetti al server

            // Crea il ClientHandler per inviare eventi al server
            ClientHandler clientHandler = new ClientHandler(networkClient);

            // Crea la View che riceverà e mostrerà gli eventi dal server
            SimpleView view = new SimpleView(clientHandler);

            // Crea il NetworkListenerClient per ascoltare gli eventi dal server
            NetworkListenerClient listenerClient = new NetworkListenerClient(networkClient.getSocket(), new NetworkListenerClient.EventReceiver() {
                @Override
                public void onEventReceived(Event event) {
                    // Quando arriva un evento dal server, la view lo riceve e lo gestisce
                    view.onEventReceived(event);
                }
            });

            // Avvia il listener in un thread separato per ricevere eventi dal server
            listenerClient.start();

            // Avvia la view per gestire l'input dell'utente e inviare eventi al server
            view.start();

        } catch (IOException e) {
            System.err.println("Errore nella connessione al server: " + e.getMessage());
        }
    }
}
