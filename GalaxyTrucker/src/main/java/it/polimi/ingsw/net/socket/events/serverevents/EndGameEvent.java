package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.net.socket.events.EventType;

/**
 * Server to client
 */
public class EndGameEvent extends Event {
    private String message;
    public EndGameEvent() {
        super(EventType.END_GAME);
        message = "The game ended";
    }

    public String getMessage() {
        return message;
    }
}
