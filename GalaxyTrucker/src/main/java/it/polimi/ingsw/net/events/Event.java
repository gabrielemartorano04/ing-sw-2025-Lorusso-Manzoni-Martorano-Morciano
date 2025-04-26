package it.polimi.ingsw.net.events;
import it.polimi.ingsw.model.Game;

import java.io.Serializable;

/**
 * An event will be used to communicate between server and client, we will create an event with the view and then
 * send it using RMI or socket based on client's choice
 */

public abstract class Event implements Serializable {
    private final EventType eventType;

    public Event(EventType eventType) {
        this.eventType = eventType;
    }
    public EventType getEventType() {
        return eventType;
    }
}
