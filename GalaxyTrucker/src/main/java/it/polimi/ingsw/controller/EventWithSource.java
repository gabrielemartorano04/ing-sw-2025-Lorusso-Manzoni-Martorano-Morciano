package it.polimi.ingsw.controller;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.server.ClientHandlerSocket;

/**
 * Links a ClientEvent with the Client sending it, represented by the istance of ClientHandlerServer
 */

public class EventWithSource {
    private final ClientEvent event;
    private final ClientHandlerSocket client;

    public EventWithSource(ClientEvent event, ClientHandlerSocket client) {
        this.event = event;
        this.client = client;
    }

    public ClientEvent getEvent() {
        return event;
    }

    public ClientHandlerSocket getClient() {
        return client;
    }
}
