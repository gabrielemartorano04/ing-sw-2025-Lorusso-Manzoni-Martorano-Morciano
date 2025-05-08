package it.polimi.ingsw.net.socket.events.clientevents.buildingevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEventType;

/**
 * Event from client to server. The player wants to select a component through a given index
 */
public class SelectTileEvent extends ClientEvent {

    private int index;

    public SelectTileEvent( int index) {
        super(ClientEventType.SELECT_TILE, false);
        this.index = index;
    }
    public int getIndex() {
        return index;
    }
    @Override
    public String getMessage() {
        return "I select component " + index;
    }
}
