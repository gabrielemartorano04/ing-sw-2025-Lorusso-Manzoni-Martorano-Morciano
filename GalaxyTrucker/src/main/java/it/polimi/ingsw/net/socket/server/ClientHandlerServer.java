package it.polimi.ingsw.net.socket.server;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;

import java.io.IOException;

public class ClientHandlerServer {
    private final NetworkServer network;
    private final ServerController controller;
    private Thread listenerThread;

    public ClientHandlerServer(NetworkServer network, ServerController controller) {
        this.network = network;
        this.controller = controller;
        startListener();
    }

    private void startListener() {
        listenerThread = new Thread(() -> {
            try {
                while (!network.isClosed()) {
                    ClientEvent event = network.receiveMessage();
                    controller.handleEvent(event, this); // Inoltra evento al ServerController
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Errore ricezione evento: " + e.getMessage());
                // TODO: notifica disconnessione
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    public void sendEvent(ServerEvent event) {
        try {
            network.sendMessage(event);
        } catch (IOException e) {
            System.err.println("Errore invio evento al client: " + e.getMessage());
        }
    }

    public void close() {
        try {
            network.close();
        } catch (IOException e) {
            System.err.println("Errore chiusura connessione: " + e.getMessage());
        }
    }
}

