package it.polimi.ingsw.net.socket.events;

/**
 * Error event from server to client. The selected component is not available
 */
public class ComponentNotAvailableError extends Event{
    private String message;
    public ComponentNotAvailableError() {
        super(EventType.COMPONENT_NOT_AVAILABLE);
        message = "The component is not available";
    }

    public String getMessage() {
        return message;
    }
}
