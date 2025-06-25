package it.polimi.ingsw.net.events.serverevents;

import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;

/**
 * Represents a generic error event sent from the server to the client
 */
public class GenericErrorEvent extends ServerEvent{

    /**
     * Message associate with the error
     */
    private final String message;

    /**
     * Event constructor
     * @param message associated with the error
     * @param forAll specify if the error is for a player or everyone
     */
    public GenericErrorEvent(String message, boolean forAll) {
        super(forAll);
        this.message = message;
    }

    /**
     * @return the message associated with the error
     */
    public String getMessage() {
        return message;
    }

    /**
     * This method calls the client method that manages this event reception
     * @param visitor client class that manages server events
     */
    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
