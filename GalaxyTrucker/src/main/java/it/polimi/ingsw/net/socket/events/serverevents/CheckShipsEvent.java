package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.net.socket.events.EventType;

/**
 * Event from server to client. The spaceships are checked and correctly modified.
 */
public class CheckShipsEvent extends Event {
    private String message;
    public CheckShipsEvent() {
        super(EventType.CHECK_SHIPS);
        message = "Spaceships are checked and correctly modified";
    }
    public String getMessage() {
        return message;
    }
}
