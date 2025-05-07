package it.polimi.ingsw.net.socket.client;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;

import java.io.IOException;

/**
 * riceve eventi dalla view e li invia al server
 */

public class ClientHandler {
    private final NetworkClient network;

    public ClientHandler(NetworkClient network) {
        this.network = network;
    }

    public void handleEvent(ClientEvent event) {
        try {
            network.sendMessage(event);
        } catch (IOException e) {
            System.err.println("Errore invio evento client: " + e.getMessage());
        }
    }
}

