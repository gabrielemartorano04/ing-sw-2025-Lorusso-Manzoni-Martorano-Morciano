package it.polimi.ingsw.net.events.serverevents.buildingevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * The ComponentNotAvailableError class represents a server-side event triggered when
 * a specific component becomes unavailable.
 */
public class ComponentNotAvailableError extends ServerEvent {

    /**
     * Event constructor
     */
    public ComponentNotAvailableError() {
        super(false);
    }

    /**
     * @return the message associated with the error
     */
    @Override
    public String getMessage() {
        return "The component is not available";
    }

    /**
     * This method calls the client method that manages this event reception
     * @param visitor client class that manages server events
     */
    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
