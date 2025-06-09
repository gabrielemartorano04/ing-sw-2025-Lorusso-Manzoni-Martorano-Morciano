package it.polimi.ingsw.net.events.clientevents.buildingevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Event from client to server. The player wants to select a component through a given index
 */
public class SelectTileEvent extends ClientEvent {

    private int index;

    public SelectTileEvent( int index) {
        this.index = index;
    }
    public int getIndex() {
        return index;
    }
    @Override
    public String getMessage() {
        return "I select component " + index;
    }

    @Override
    public ServerEvent call(VirtualServer visitor, Client client) {
         return visitor.visit(this, client);
    }
}
