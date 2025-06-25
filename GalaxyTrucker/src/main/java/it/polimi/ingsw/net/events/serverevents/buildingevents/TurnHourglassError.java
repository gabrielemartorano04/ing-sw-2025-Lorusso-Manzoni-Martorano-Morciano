package it.polimi.ingsw.net.events.serverevents.buildingevents;


import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * The TurnHourglassError class represents a server event sent to indicate
 * that the hourglass cannot be turned at the current moment in the game
 */
public class TurnHourglassError extends ServerEvent {

    /**
     * Event constructor
     */
    public TurnHourglassError() {
        super(false);
    }

    /**
     * @return the message associated with the error
     */
    @Override
    public String getMessage() {
        return " You can't turn the hourglass at the moment";
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
