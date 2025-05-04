package it.polimi.ingsw.net.socket.events;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEventType;

import java.io.Serializable;

/**
 * An event will be used to communicate between server and client, we will create an event with the view and then
 * send it using RMI or socket based on client's choice
 */

public abstract class Event implements Serializable {
    private final ClientEventType eventType;

    public Event(EventType eventType) {
        this.eventType = eventType;
    }
    public ClientEventType getEventType() {
        return eventType;
    }
}
