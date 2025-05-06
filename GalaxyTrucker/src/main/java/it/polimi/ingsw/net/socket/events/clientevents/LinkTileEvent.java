package it.polimi.ingsw.net.socket.events.clientevents;

import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.net.socket.events.EventType;

/**
 * Event from client to server. The player wants to link the selected component to the spaceship.
 * The player gives the coordinates.
 */
public class LinkTileEvent extends Event {
    private String  message;
    private int x,y;
    public LinkTileEvent( int x, int y) {
        super(EventType.LINK_TILE);
        this.x = x;
        this.y = y;
        message="I want to link the selected component in position ("+x+", "+y+")";
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public String getMessage() {
        return message;
    }
}
