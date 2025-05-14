package it.polimi.ingsw.net.socket.events.serverevents.buildingevents;

import it.polimi.ingsw.model.AvailableComponents;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

/**
 * Error event from server to client. The selected component is not available
 */
public class ComponentNotAvailableError extends ServerEvent {
    private String message;
    private AvailableComponents avComponents;
    public ComponentNotAvailableError(AvailableComponents components) {
        super(ServerEventType.COMPONENT_NOT_AVAILABLE, false);
        message = "The component is not available";
        this.avComponents = components;
    }

    public AvailableComponents getAvComponents() {
        return avComponents;
    }
    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
