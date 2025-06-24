package it.polimi.ingsw.net.events.clientevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Event sent for the request of where to place aliens
 */
public class AlienCoordinatesRequest extends ClientEvent {

    /**
     * Event constructor
     */
    public AlienCoordinatesRequest() {}

    /**
     * @return the message associated with the request of coordinates
     */
    @Override
    public String getMessage() {
        return "Tell me where I can put aliens";
    }

    /**
     * This method calls the server method that manages this event reception
     * @param visitor server class that manages client events
     * @param client client that sent the event
     * @return server event response based on client event reception
     */
    @Override
    public ServerEvent call(VirtualServer visitor, Client client) {
         return visitor.visit(this, client);
    }
}
