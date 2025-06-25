package it.polimi.ingsw.net.events.serverevents.buildingevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Represents an event sent by the server to notify a client that it is waiting for other players
 * to finish their building phase before proceeding. This event is used to synchronize clients during the game
 */
public class WaitOthersBuildingEvent extends ServerEvent {

    /**
     * Event constructor
     */
    public WaitOthersBuildingEvent() {
        super(false);
    }

    /**
     * @return the message associated with the waiting
     */
    @Override
    public String getMessage() {
        return "Waiting for others players to end building";
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
