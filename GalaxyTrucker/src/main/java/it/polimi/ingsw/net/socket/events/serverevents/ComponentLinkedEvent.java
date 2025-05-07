package it.polimi.ingsw.net.socket.events.serverevents;

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
