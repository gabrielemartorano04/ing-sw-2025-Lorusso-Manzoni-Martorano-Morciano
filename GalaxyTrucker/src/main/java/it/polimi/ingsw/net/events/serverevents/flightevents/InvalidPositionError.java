package it.polimi.ingsw.net.events.serverevents.flightevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;

/**
 * Error event from server to client. It signals that the player gave an invalid position
 */
public class InvalidPositionError extends ServerEvent {
    private String username, message;
    public InvalidPositionError(String username) {
        super(ServerEventType.INVALID_POSITION_ERROR, false);
        this.username = username;
        message = username + " gave an invalid position";
    }
    public String getUsername() {
        return username;
    }
    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
