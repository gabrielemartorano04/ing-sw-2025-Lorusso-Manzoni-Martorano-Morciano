package it.polimi.ingsw.net.events.clientevents.buildingevents;

import it.polimi.ingsw.net.control.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;

import it.polimi.ingsw.net.control.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Event sent for discarding a tile during the building phase
 */
public class DiscardTileEvent extends ClientEvent {

    /**
     * Index of the tile in the list of available components
     */
    private final int index;

    /**
     * Event constructor
     * @param index of the tile in the list of available components
     */
    public DiscardTileEvent(int index) {
            this.index = index;
    }

    /**
     * @return the index of the tile in the list of available components
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return the message associated with the discard
     */
    @Override
    public String getMessage() {
        return "I want to discard the selected tile";
    }

    /**
     * This method calls the server method that manages this event reception
     * @param visitor server class that manages client events
     * @param client client that sent the event
     * @return server event response based on client event reception
     */
    @Override
    public ServerEvent call(VirtualServer visitor, Client client){
         return visitor.visit(this, client);
    }
}
