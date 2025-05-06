package it.polimi.ingsw.net.socket.events.clientevents;

import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.net.socket.events.EventType;

/**
 * Event from client to server. The player wants to select a component through a given index
 */
public class SelectTileEvent extends Event {

    private String message;
    private int index;

    public SelectTileEvent( int index) {
        super(EventType.SELECT_TILE);
        this.index = index;
        message = "I select component " + index;
    }

    public String getMessage() {
        return message;
    }
    public int getIndex() {
        return index;
    }
}
