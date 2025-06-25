package it.polimi.ingsw.net.events.clientevents.buildingevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Represents a client event where a specific tile from the list of kept components is selected.
 */
public class SelectKeptTileEvent extends ClientEvent {

    /**
     * Index of the tile in the list of kept components
     */
    private final int index;

    /**
     * Event constructor
     * @param index of the tile in the list of kept components
     */
    public SelectKeptTileEvent(int index) {
        this.index = index;
    }

    /**
     * @return the index of the component in the list of kept components
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return the message associated with the selection of a kept component
     */
    @Override
    public String getMessage() {
        return "I want to select my kept component n: "+index;
    }

    /**
     * This method calls the server method that manages this event reception
     * @param visitor server class that manages client events
     * @param client client that sent the event
     * @return server event response based on client event reception
     */
    @Override
    public ServerEvent call(VirtualServer visitor, Client client) {
         return visitor.visit(this,client);
    }
}
