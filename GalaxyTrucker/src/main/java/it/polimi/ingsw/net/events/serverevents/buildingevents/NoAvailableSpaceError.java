package it.polimi.ingsw.net.events.serverevents.buildingevents;


import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Represents an error event indicating that there is no available space
 * on the server for a new player
 */
public class NoAvailableSpaceError extends ServerEvent {

    /**
     * Event constructor
     */
    public NoAvailableSpaceError(){
        super(false);
    }

    /**
     * @return the message associated with the error
     */
    @Override
    public String getMessage() {
        return "There is no available space";
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
