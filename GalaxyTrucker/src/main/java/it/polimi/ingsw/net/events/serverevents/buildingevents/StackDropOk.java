package it.polimi.ingsw.net.events.serverevents.buildingevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Represents a specific event triggered by the server, indicating that a stack drop action has been successfully processed. This event can be specified to target all clients or a particular client based on the forAll flag
 */
public class StackDropOk extends ServerEvent {

    /**
     * Event constructor
     * @param forAll specify if the message is only for a player or everyone
     */
    public StackDropOk(boolean forAll) {
        super(forAll);

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
