package it.polimi.ingsw.controller;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.server.ClientHandlerSocket;

/**
 * Links a ClientEvent with the Client sending it, represented by the istance of ClientHandlerServer.
 * Attributes:
 * - client event
 * - client that sent the event
 */

public class EventWithSource {
    private final ClientEvent event;
    private final ClientHandlerSocket client;

    /**
     * Class constructor
     * @param event client event
     * @param client client's clientHandlerSocket
     */
    public EventWithSource(ClientEvent event, ClientHandlerSocket client) {
        this.event = event;
        this.client = client;
    }

    /**
     * @return the client event
     */
    public ClientEvent getEvent() {
        return event;
    }

    /**
     * @return client
     */
    public ClientHandlerSocket getClient() {
        return client;
    }
}
