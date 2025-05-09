package it.polimi.ingsw.net.socket.client;

import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.view.ViewFlow;

import java.io.IOException;
import java.util.Scanner;

/**
 * Main client application
 */
public class MainClient {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Chiedi all'utente dove connettersi
        System.out.print("Do you want to connect to the localhost? (y/n): ");
        String answer = scanner.nextLine().trim().toLowerCase();

        String serverAddress;
        if (answer.equals("y") || answer.equals("yes")) {
            serverAddress = "127.0.0.1"; // Localhost
        } else {
            System.out.print("Insert the server address: ");
            serverAddress = scanner.nextLine().trim();
        }

        final int SERVER_PORT = 12345;

        // Crea il NetworkClient e si connette al server
        NetworkClient networkClient = new NetworkClient(serverAddress, SERVER_PORT);
        try {
            networkClient.connect(); // Connetti al server

            ClientHandler clientHandler = new ClientHandler(networkClient);
            ViewFlow view = new ViewFlow(clientHandler);

            NetworkListenerClient listenerClient = new NetworkListenerClient(networkClient.getSocket(), new NetworkListenerClient.EventReceiver() {
                @Override
                public void onEventReceived(ServerEvent event) {
                    view.onEventReceived(event);
                }
            });

            listenerClient.start();
            view.start();

        } catch (IOException e) {
            System.err.println("Errore nella connessione al server: " + e.getMessage());
        }
    }
}




