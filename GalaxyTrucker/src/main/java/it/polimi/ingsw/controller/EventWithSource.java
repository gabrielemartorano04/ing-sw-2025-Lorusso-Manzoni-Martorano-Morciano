package it.polimi.ingsw.controller;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;

/**
 * This class links a ClientEvent with the Client sending it, represented by the instance of ClientHandlerServer.
 */

public class EventWithSource {

    /**
     * Client event
     */
    private final ClientEvent event;

    /**
     * Client that sent the event
     */
    private final Client client;

    /**
     * Class constructor
     * @param event client event
     * @param client client's clientHandlerSocket
     */
    public EventWithSource(ClientEvent event, Client client) {
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
     * @return the client
     */
    public Client getClient() {
        return client;
    }
}
