package it.polimi.ingsw.net.socket.events.clientevents;

import java.io.Serializable;

/**
 * An event will be used to communicate between server and client, we will create an event with the view and then
 * send it using RMI or socket based on client's choice
 */

public abstract class ClientEvent implements Serializable {
    private final ClientEventType eventType;
    private String message;

    public ClientEvent(ClientEventType eventType) {
        this.eventType = eventType;
        message="Event: " + eventType.name();
    }
    public ClientEventType getEventType() {
        return eventType;
    }
    public String getMessage() {
        return message;
    }
}
