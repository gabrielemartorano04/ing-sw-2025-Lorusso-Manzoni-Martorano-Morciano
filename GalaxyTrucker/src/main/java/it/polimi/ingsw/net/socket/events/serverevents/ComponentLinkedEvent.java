package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.net.socket.events.EventType;

/**
 * Event from server to client when the component is correctly linked to the spaceship
 */
public class ComponentLinkedEvent extends ServerEvent {
    String message;
    public ComponentLinkedEvent() {
        super(ServerEventType.COMPONENT_LINKED);
        message = "Component correctly linked";
    }
    public String getMessage() {
        return message;
    }
}
