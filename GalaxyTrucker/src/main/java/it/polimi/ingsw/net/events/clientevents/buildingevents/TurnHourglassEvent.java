package it.polimi.ingsw.net.events.clientevents.buildingevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Event representing the action where a client requests to turn the hourglass.
 * This class extends the ClientEvent and provides a specific implementation of the
 * interaction between the client and server for the hourglass-turning action.
 */
public class TurnHourglassEvent extends ClientEvent {

    public TurnHourglassEvent() {
    }

    @Override
    public String getMessage() {
        return "I want to turn the hourglass";
    }

    /**
     * This method calls the server method that manages this event reception
     * @param visitor server class that manages client events
     * @param client client that sent the event
     * @return server event response based on client event reception
     */
    @Override
    public ServerEvent call(VirtualServer visitor, Client client) {
         return visitor.visit(this, client );
    }
}
