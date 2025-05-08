package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.socket.server.ClientHandlerServer;

/**
 * This class links an istance of ClientHandlerServer to the istance of the Trucker (which for us is the player)
 */

public class PlayerContext {
    private final ClientHandlerServer client;
    private final Trucker trucker;

    public PlayerContext(ClientHandlerServer client, Trucker trucker) {
        this.client = client;
        this.trucker = trucker;
    }

    public ClientHandlerServer getClient() {
        return client;
    }

    public Trucker getTrucker() {
        return trucker;
    }
}

