package it.polimi.ingsw.net.events.serverevents.setupevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Represents the first connection event for notifying the server of a client's initial connection
 */
public class FirstConnectionEvent extends ServerEvent {

    /**
     * Message associated with the first connection
     */
    private final String message;

    /**
     * Event constructor
     * @param message associated with the first connection
     */
    public FirstConnectionEvent(String message) {
        super(false);
        this.message = message;
    }

    /**
     * @return the message associated with the first connection
     */
    @Override
    public String getMessage() {
        return message;
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
