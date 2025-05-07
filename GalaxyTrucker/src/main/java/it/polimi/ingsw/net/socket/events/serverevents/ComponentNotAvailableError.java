package it.polimi.ingsw.net.socket.events.serverevents;

/**
 * Error event from server to client. The selected component is not available
 */
public class ComponentNotAvailableError extends ServerEvent {
    private String message;
    public ComponentNotAvailableError() {
        super(ServerEventType.COMPONENT_NOT_AVAILABLE);
        message = "The component is not available";
    }
    @Override
    public String getMessage() {
        return message;
    }
}
