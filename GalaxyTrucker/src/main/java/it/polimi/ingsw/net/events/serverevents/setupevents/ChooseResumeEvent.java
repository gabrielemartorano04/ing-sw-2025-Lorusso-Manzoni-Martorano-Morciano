package it.polimi.ingsw.net.events.serverevents.setupevents;

import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Represents a server event sent to the client to inquire whether the user wants to resume a previously unfinished game
 */
public class ChooseResumeEvent extends ServerEvent {

    /**
     * Event constructor
     */
    public ChooseResumeEvent() {
        super(true);
    }

    /**
     * @return the message associated with the resumption of an unfinished game
     */
    @Override
    public String getMessage() {
        return "an old game isn't finished yet, do you want to continue? (y/n)";
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
