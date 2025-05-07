package it.polimi.ingsw.net.socket.events.serverevents;

/**
 * Event from server to client when the component is correctly linked to the spaceship
 */
public class ComponentLinkedEvent extends ServerEvent {
    String message;
    public ComponentLinkedEvent() {
        super(ServerEventType.COMPONENT_LINKED, false);
        message = "Component correctly linked";
    }
    @Override
    public String getMessage() {
        return message;
    }
}
