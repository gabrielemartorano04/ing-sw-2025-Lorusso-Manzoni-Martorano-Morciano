package it.polimi.ingsw.net.socket.server;

import it.polimi.ingsw.controller.ServerController;

import java.io.IOException;
import java.net.Socket;

public class ClientHandlerThread extends Thread {
    private final Socket clientSocket;
    private final ServerController serverController;

    public ClientHandlerThread(Socket clientSocket, ServerController serverController) {
        this.clientSocket = clientSocket;
        this.serverController = serverController;
    }

    @Override
    public void run() {
        try {
            NetworkServer networkServer = new NetworkServer(clientSocket);
            new ClientHandlerServer(networkServer, serverController); // Questo avvia già il listener nel costruttore
        } catch (IOException e) {
            System.err.println("Errore nella gestione del client: " + e.getMessage());
        }
    }
}