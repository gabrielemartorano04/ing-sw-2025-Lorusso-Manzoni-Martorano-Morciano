package it.polimi.ingsw.net.events.serverevents.buildingevents;

import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Represents an event triggered when a client provides invalid coordinates.
 */
public class InvalidCoordinatesError extends ServerEvent {

    /**
     * Nickname of the player who inserts invalid coordinates
     */
    private final String username;

    /**
     * Event constructor
     * @param username of the player who inserts invalid coordinates
     */
    public InvalidCoordinatesError(String username) {
        super(false);
        this.username = username;
    }

    /**
     * @return the message associated with the error
     */
    @Override
    public String getMessage() {
        return username+" inserted invalid coordinates";
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
