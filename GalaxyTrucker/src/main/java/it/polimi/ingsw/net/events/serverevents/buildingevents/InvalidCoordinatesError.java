package it.polimi.ingsw.net.events.serverevents.buildingevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Error event from server to client. It signals that the player inserted invalid coordinates.
 */
public class InvalidCoordinatesError extends ServerEvent {
    private String username, message;
    public InvalidCoordinatesError(String username) {
        super(false);
        this.username = username;
        message = username+" inserted invalid coordinates";
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
