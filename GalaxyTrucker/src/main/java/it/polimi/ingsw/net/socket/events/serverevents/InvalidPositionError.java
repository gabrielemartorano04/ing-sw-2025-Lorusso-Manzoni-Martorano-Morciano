package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.net.socket.events.EventType;

/**
 * Error event from server to client. It signals that the player gave an invalid position
 */
public class InvalidPositionError extends ServerEvent {
    private String username, message;
    public InvalidPositionError(String username) {
        super(ServerEventType.INVALID_POSITION_ERROR);
        this.username = username;
        message = username + " gave an invalid position";
    }
    public String getUsername() {
        return username;
    }
    public String getMessage() {
        return message;
    }
}
