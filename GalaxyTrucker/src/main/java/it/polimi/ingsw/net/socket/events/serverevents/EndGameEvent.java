package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.net.socket.events.EventType;

/**
 * Server to client
 */
public class EndGameEvent extends ServerEvent {
    private String message;
    public EndGameEvent() {
        super(ServerEventType.END_GAME);
        message = "The game ended";
    }

    public String getMessage() {
        return message;
    }
}
