package it.polimi.ingsw.net.events.clientevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.util.ArrayList;

/**
 * Event sent for the coordinates of where to place aliens
 */
public class SendAlienCoord extends ClientEvent {

    /**
     * List of coordinates where to place aliens
     */
    private ArrayList<int[]> coord;

    /**
     * Event constructor
     * @param coord list of coordinates where to place aliens
     */
    public SendAlienCoord(ArrayList<int[]> coord) {
        this.coord = coord;
    }

    /**
     * @return the list of coordinates where to place aliens
     */
    public ArrayList<int[]> getCoord() {
        return coord;
    }

    /**
     * @return the message associated with choice of the coordinates
     */
    @Override
    public String getMessage() {
        return "Send AlienCoord";
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
