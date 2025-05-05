package it.polimi.ingsw.net.socket.events;

/**
 * Event from server to client when the component is correctly linked to the spaceship
 */
public class ComponentLinkedEvent extends Event{
    String message;
    public ComponentLinkedEvent() {
        super(EventType.COMPONENT_LINKED);
        message = "Component correctly linked";
    }
    public String getMessage() {
        return message;
    }
}
