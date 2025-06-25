package it.polimi.ingsw.net.events.serverevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;

/**
 * Represents a ping event used for communication between the server and clients
 */
public class PingEvent extends ServerEvent {

    /**
     * Event constructor
     */
    public PingEvent() {
        super(true);
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
