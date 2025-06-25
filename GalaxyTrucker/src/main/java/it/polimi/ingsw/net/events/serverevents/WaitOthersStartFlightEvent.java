package it.polimi.ingsw.net.events.serverevents;

import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;

/**
 * This class represents a server event indicating the client is waiting for other participants
 * to start the flight
 */
public class WaitOthersStartFlightEvent extends ServerEvent {

    /**
     * Event constructor
     */
    public WaitOthersStartFlightEvent() {
        super(false);

    }

    /**
     * @return the message associated with the waiting
     */
    @Override
    public String getMessage(){
        return "Waiting for other to start flight";
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
