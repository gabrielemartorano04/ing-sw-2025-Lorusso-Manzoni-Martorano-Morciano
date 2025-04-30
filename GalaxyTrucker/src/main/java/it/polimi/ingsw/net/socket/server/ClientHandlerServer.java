package it.polimi.ingsw.net.socket.server;

import it.polimi.ingsw.net.socket.events.Event;

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
                    Event event = network.receiveMessage();
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

    public void sendEvent(Event event) {
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

