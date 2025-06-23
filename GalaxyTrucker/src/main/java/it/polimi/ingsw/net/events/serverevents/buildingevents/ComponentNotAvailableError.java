package it.polimi.ingsw.net.events.serverevents.buildingevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Error event from server to client. The selected component is not available
 */
public class ComponentNotAvailableError extends ServerEvent {
    private String message;
    public ComponentNotAvailableError() {
        super(false);
        message = "The component is not available";
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
