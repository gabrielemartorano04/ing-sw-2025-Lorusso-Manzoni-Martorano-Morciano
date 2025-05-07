package it.polimi.ingsw.net.socket.events.serverevents;

import java.io.Serializable;

/**
 * An event will be used to communicate between server and client, we will create an event with the view and then
 * send it using RMI or socket based on client's choice
 */

public abstract class ServerEvent implements Serializable {
    private final ServerEventType eventType;
    private String message;

    public ServerEvent(ServerEventType eventType) {
        this.eventType = eventType;
        message="Event: " + eventType.name();
    }
    public ServerEventType getEventType() {
        return eventType;
    }
    public String getMessage() {
        return message;
    }
}
