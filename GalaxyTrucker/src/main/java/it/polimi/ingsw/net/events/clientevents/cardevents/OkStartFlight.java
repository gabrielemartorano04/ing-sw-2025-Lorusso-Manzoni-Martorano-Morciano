package it.polimi.ingsw.net.events.clientevents.cardevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Event sent to notify the possibility to start the flight phase
 */
public class OkStartFlight extends ClientEvent {

    /**
     * Event constructor
     */
    public OkStartFlight() {
    }

    /**
     * @return the message associated with the input to start the flight
     */
    @Override
    public String getMessage() {
        return "For me, flight can start";
    }

    /**
     * This method calls the server method that manages this event reception
     * @param visitor server class that manages client events
     * @param client client that sent the event
     * @return server event response based on client event reception
     */
    @Override
    public ServerEvent call(VirtualServer visitor, Client client) {
         return visitor.visit(this);
    }
}
