package it.polimi.ingsw.controller;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.server.ClientHandlerServer;

/**
 *
 */

public class EventWithSource {
    private final ClientEvent event;
    private final ClientHandlerServer client;

    public EventWithSource(ClientEvent event, ClientHandlerServer client) {
        this.event = event;
        this.client = client;
    }

    public ClientEvent getEvent() {
        return event;
    }

    public ClientHandlerServer getClient() {
        return client;
    }
}
